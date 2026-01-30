package br.com.torqueos.service;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.torqueos.model.OrdemServico;
import br.com.torqueos.model.OrdemServicoTecnico;
import br.com.torqueos.model.PecaOS;
import br.com.torqueos.model.ServicoOS;
import br.com.torqueos.model.Tecnico;
import br.com.torqueos.repository.OrdemServicoRepository;
import br.com.torqueos.repository.OrdemServicoTecnicoRepository;
import br.com.torqueos.repository.PecaOSRepository;
import br.com.torqueos.repository.ServicoOSRepository;
import br.com.torqueos.tenant.TenantContext;

@Service
public class OrdemServicoService {
	@PersistenceContext
	private EntityManager entityManager;

	private final OrdemServicoRepository ordemRepo;
	private final ServicoOSRepository servicoOSRepo;
	private final PecaOSRepository pecaOSRepo;

	private final ClienteService clienteService;
	private final VeiculoService veiculoService;

	// catálogos
	private final ServicoService servicoService;
	private final PecaService pecaService;

	// SaaS
	private final AssinaturaService assinaturaService;

	// ===== NOVO: técnicos =====
	private final TecnicoService tecnicoService;
	private final OrdemServicoTecnicoRepository osTecnicoRepo;

	public OrdemServicoService(OrdemServicoRepository ordemRepo, ServicoOSRepository servicoOSRepo,
			PecaOSRepository pecaOSRepo, ClienteService clienteService, VeiculoService veiculoService,
			ServicoService servicoService, PecaService pecaService, AssinaturaService assinaturaService,
			TecnicoService tecnicoService, OrdemServicoTecnicoRepository osTecnicoRepo) {
		this.ordemRepo = ordemRepo;
		this.servicoOSRepo = servicoOSRepo;
		this.pecaOSRepo = pecaOSRepo;
		this.clienteService = clienteService;
		this.veiculoService = veiculoService;
		this.servicoService = servicoService;
		this.pecaService = pecaService;
		this.assinaturaService = assinaturaService;

		this.tecnicoService = tecnicoService;
		this.osTecnicoRepo = osTecnicoRepo;
	}

	// =========================================================
	// LIST / GET
	// =========================================================

	@Transactional(readOnly = true)
	public List<OrdemServico> findAll() {
		Long emp = TenantContext.getEmpresaId();
		return ordemRepo.findAll().stream().filter(o -> o != null && emp != null && emp.equals(o.getIdEmpresa()))
				.sorted(Comparator.comparing(OrdemServico::getIdOs, Comparator.nullsLast(Comparator.naturalOrder()))
						.reversed())
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public OrdemServico findById(Long id) {
		return findPorIdDaEmpresa(id);
	}

	@Transactional(readOnly = true)
	public OrdemServico findPorIdDaEmpresa(Long id) {
		if (id == null)
			throw new RuntimeException("OS inválida");
		Long emp = TenantContext.getEmpresaId();

		Optional<OrdemServico> opt = ordemRepo.findById(id);
		OrdemServico os = opt.orElseThrow(() -> new RuntimeException("OS não encontrada: " + id));

		if (os.getIdEmpresa() == null || emp == null || !os.getIdEmpresa().equals(emp)) {
			throw new RuntimeException("OS não pertence à empresa do tenant");
		}
		return os;
	}

	@Transactional
	public void delete(Long idOs) {
		assinaturaService.validarAssinaturaAtiva();

		OrdemServico os = findPorIdDaEmpresa(idOs);

		// remove itens primeiro
		removerItens(os.getIdOs());

		// remove vínculos de técnicos (por segurança — ON DELETE CASCADE também ajuda)
		osTecnicoRepo.deleteVinculosDaOs(TenantContext.getEmpresaId(), idOs);


		ordemRepo.delete(os);
	}

	// =========================================================
	// CREATE/UPDATE - MODO ANTIGO (texto)
	// =========================================================

	@Transactional
	public OrdemServico create(OrdemServico novo, Long clienteId, Long veiculoId, List<String> servicoDescricao,
			List<String> servicoValor, List<String> pecaDescricao, List<String> pecaQuantidade,
			List<String> pecaValorUnitario) {

		assinaturaService.validarAssinaturaAtiva();

		Long emp = TenantContext.getEmpresaId();
		OrdemServico os = new OrdemServico();
		os.setIdEmpresa(emp);

		aplicarCamposEditaveis(os, novo);

		if (clienteId != null)
			os.setCliente(clienteService.find(clienteId));
		else
			os.setCliente(null);

		if (veiculoId != null)
			os.setVeiculo(veiculoService.find(veiculoId));
		else
			os.setVeiculo(null);

		OrdemServico saved = ordemRepo.save(os);

		salvarItensPorTexto(saved, servicoDescricao, servicoValor, pecaDescricao, pecaQuantidade, pecaValorUnitario);

		return saved;
	}

	@Transactional
	public OrdemServico update(Long idOs, OrdemServico novo, Long clienteId, Long veiculoId,
			List<String> servicoDescricao, List<String> servicoValor, List<String> pecaDescricao,
			List<String> pecaQuantidade, List<String> pecaValorUnitario) {

		assinaturaService.validarAssinaturaAtiva();

		OrdemServico os = findPorIdDaEmpresa(idOs);

		aplicarCamposEditaveis(os, novo);

		if (clienteId != null)
			os.setCliente(clienteService.find(clienteId));
		else
			os.setCliente(null);

		if (veiculoId != null)
			os.setVeiculo(veiculoService.find(veiculoId));
		else
			os.setVeiculo(null);

		OrdemServico saved = ordemRepo.save(os);

		removerItens(saved.getIdOs());
		salvarItensPorTexto(saved, servicoDescricao, servicoValor, pecaDescricao, pecaQuantidade, pecaValorUnitario);

		return saved;
	}

	// =========================================================
	// CREATE/UPDATE - MODO PROFISSIONAL (catálogo por ID)
	// =========================================================

	@Transactional
	public OrdemServico create(OrdemServico novo, Long clienteId, Long veiculoId, List<Long> servicoId,
			List<Long> pecaId, List<Integer> pecaQuantidade) {

		assinaturaService.validarAssinaturaAtiva();

		Long emp = TenantContext.getEmpresaId();
		OrdemServico os = new OrdemServico();
		os.setIdEmpresa(emp);

		aplicarCamposEditaveis(os, novo);

		if (clienteId != null)
			os.setCliente(clienteService.find(clienteId));
		else
			os.setCliente(null);

		if (veiculoId != null)
			os.setVeiculo(veiculoService.find(veiculoId));
		else
			os.setVeiculo(null);

		OrdemServico saved = ordemRepo.save(os);

		salvarItensPorCatalogo(saved, servicoId != null ? servicoId : Collections.emptyList(),
				pecaId != null ? pecaId : Collections.emptyList(), pecaQuantidade);

		return saved;
	}

	@Transactional
	public OrdemServico update(Long idOs, OrdemServico novo, Long clienteId, Long veiculoId, List<Long> servicoId,
			List<Long> pecaId, List<Integer> pecaQuantidade) {

		assinaturaService.validarAssinaturaAtiva();

		OrdemServico os = findPorIdDaEmpresa(idOs);

		aplicarCamposEditaveis(os, novo);

		if (clienteId != null)
			os.setCliente(clienteService.find(clienteId));
		else
			os.setCliente(null);

		if (veiculoId != null)
			os.setVeiculo(veiculoService.find(veiculoId));
		else
			os.setVeiculo(null);

		OrdemServico saved = ordemRepo.save(os);

		removerItens(saved.getIdOs());
		salvarItensPorCatalogo(saved, servicoId != null ? servicoId : Collections.emptyList(),
				pecaId != null ? pecaId : Collections.emptyList(), pecaQuantidade);

		return saved;
	}

	// =========================================================
	// ===== NOVO: overloads com técnicos =====
	// =========================================================

	@Transactional
	public OrdemServico create(OrdemServico ordem, Long clienteId, Long veiculoId, List<Long> servicoId,
			List<Long> pecaId, List<Integer> pecaQuantidade, List<Long> tecnicoId, List<String> tecnicoPapel,
			List<Integer> tecnicoPercentual) {

		OrdemServico os = create(ordem, clienteId, veiculoId, servicoId, pecaId, pecaQuantidade);

		salvarTecnicosDaOs(os.getIdOs(), tecnicoId, tecnicoPapel, tecnicoPercentual);

		return os;
	}

	@Transactional
	public OrdemServico update(Long idOs, OrdemServico ordem, Long clienteId, Long veiculoId, List<Long> servicoId,
			List<Long> pecaId, List<Integer> pecaQuantidade, List<Long> tecnicoId, List<String> tecnicoPapel,
			List<Integer> tecnicoPercentual) {

		OrdemServico os = update(idOs, ordem, clienteId, veiculoId, servicoId, pecaId, pecaQuantidade);

		salvarTecnicosDaOs(os.getIdOs(), tecnicoId, tecnicoPapel, tecnicoPercentual);

		return os;
	}

	// =========================================================
	// INTERNOS
	// =========================================================

	private void aplicarCamposEditaveis(OrdemServico destino, OrdemServico origem) {
		if (origem == null)
			return;

		destino.setStatus(origem.getStatus());
		destino.setObservacoes(origem.getObservacoes());

		if (destino.getStatus() == null || destino.getStatus().trim().isEmpty()) {
			destino.setStatus("ABERTA");
		}
	}

	private void removerItens(Long idOs) {
		Long emp = TenantContext.getEmpresaId();
		servicoOSRepo.deleteByIdEmpresaAndOrdemServico_IdOs(emp, idOs);
		pecaOSRepo.deleteByIdEmpresaAndOrdemServico_IdOs(emp, idOs);
	}

	// -------- itens por texto --------
	private void salvarItensPorTexto(OrdemServico os, List<String> servicoDescricao, List<String> servicoValor,
			List<String> pecaDescricao, List<String> pecaQuantidade, List<String> pecaValorUnitario) {

		Long emp = TenantContext.getEmpresaId();

		if (servicoDescricao != null) {
			for (int i = 0; i < servicoDescricao.size(); i++) {
				String desc = servicoDescricao.get(i);
				if (desc == null || desc.trim().isEmpty())
					continue;

				BigDecimal valor = parseMoney(getAt(servicoValor, i));

				ServicoOS item = new ServicoOS();
				item.setIdEmpresa(emp);
				item.setOrdemServico(os);
				item.setIdServicoCatalogo(null);
				item.setDescricao(desc.trim());
				item.setValor(valor);

				servicoOSRepo.save(item);
			}
		}

		if (pecaDescricao != null) {
			for (int i = 0; i < pecaDescricao.size(); i++) {
				String desc = pecaDescricao.get(i);
				if (desc == null || desc.trim().isEmpty())
					continue;

				Integer qtd = parseInt(getAt(pecaQuantidade, i), 1);
				if (qtd == null || qtd < 1)
					qtd = 1;

				BigDecimal vlr = parseMoney(getAt(pecaValorUnitario, i));

				PecaOS item = new PecaOS();
				item.setIdEmpresa(emp);
				item.setOrdemServico(os);
				item.setIdPecaCatalogo(null);
				item.setDescricao(desc.trim());
				item.setQuantidade(qtd);
				item.setValorUnitario(vlr);

				pecaOSRepo.save(item);
			}
		}
	}

	// -------- itens por catálogo --------
	private void salvarItensPorCatalogo(OrdemServico os, List<Long> servicoId, List<Long> pecaId,
			List<Integer> pecaQuantidade) {

		Long emp = TenantContext.getEmpresaId();

		if (servicoId != null) {
			for (Long idS : servicoId) {
				if (idS == null)
					continue;

				br.com.torqueos.model.Servico cat = servicoService.find(idS);

				ServicoOS item = new ServicoOS();
				item.setIdEmpresa(emp);
				item.setOrdemServico(os);
				item.setIdServicoCatalogo(cat.getIdServico());
				item.setDescricao(cat.getNome());
				item.setValor(cat.getValor());

				servicoOSRepo.save(item);
			}
		}

		if (pecaId != null) {
			for (int i = 0; i < pecaId.size(); i++) {
				Long idP = pecaId.get(i);
				if (idP == null)
					continue;

				Integer qtd = 1;
				if (pecaQuantidade != null && pecaQuantidade.size() > i && pecaQuantidade.get(i) != null) {
					qtd = pecaQuantidade.get(i);
				}
				if (qtd == null || qtd < 1)
					qtd = 1;

				br.com.torqueos.model.Peca cat = pecaService.find(idP);

				PecaOS item = new PecaOS();
				item.setIdEmpresa(emp);
				item.setOrdemServico(os);
				item.setIdPecaCatalogo(cat.getIdPeca());
				item.setDescricao(cat.getNome());
				item.setQuantidade(qtd);
				item.setValorUnitario(cat.getValorUnitario());

				pecaOSRepo.save(item);
			}
		}
	}

	// =========================================================
	// ===== NOVO: salva técnicos da OS =====
	// =========================================================
	private void salvarTecnicosDaOs(Long idOs, List<Long> tecnicoId, List<String> tecnicoPapel,
			List<Integer> tecnicoPercentual) {

		Long emp = TenantContext.getEmpresaId();
		if (emp == null)
			throw new RuntimeException("Tenant não definido (empresaId).");

		OrdemServico os = findPorIdDaEmpresa(idOs);

// ✅ Apaga de forma GERENCIADA (evita problemas de bulk delete + contexto do Hibernate)
		List<OrdemServicoTecnico> atuais = osTecnicoRepo.findByIdEmpresaAndOrdemServico_IdOsOrderByIdOsTecnicoAsc(emp,
				idOs);

		if (atuais != null && !atuais.isEmpty()) {
			osTecnicoRepo.deleteAll(atuais);
			osTecnicoRepo.flush(); // força executar o DELETE agora
			entityManager.clear(); // limpa o contexto (evita “fantasmas”/duplicidade)
		}

		if (tecnicoId == null || tecnicoId.isEmpty())
			return;

// ✅ evita duplicidade vinda do form (mesmo técnico repetido)
		java.util.Set<Long> jaInseridos = new java.util.HashSet<>();
		LocalDateTime now = LocalDateTime.now();

		for (int i = 0; i < tecnicoId.size(); i++) {
			Long idTec = tecnicoId.get(i);
			if (idTec == null)
				continue;

			if (!jaInseridos.add(idTec)) {
				continue; // repetido no POST
			}

			Tecnico t = tecnicoService.findById(idTec);

			String papel = safeAt(tecnicoPapel, i);
			papel = (papel == null ? "" : papel.trim().toUpperCase());

// ✅ só MECANICO/AUXILIAR
			if (!"MECANICO".equals(papel) && !"AUXILIAR".equals(papel)) {
				papel = (t.getTipo() != null ? t.getTipo().trim().toUpperCase() : "MECANICO");
				if (!"MECANICO".equals(papel) && !"AUXILIAR".equals(papel))
					papel = "MECANICO";
			}

			Integer pct = safeAtInt(tecnicoPercentual, i);
			if (pct == null)
				pct = (t.getPercentualComissao() != null ? t.getPercentualComissao() : 0);
			if (pct < 0)
				pct = 0;
			if (pct > 100)
				pct = 100;

			OrdemServicoTecnico ost = new OrdemServicoTecnico();
			ost.setIdEmpresa(emp);
			ost.setOrdemServico(os);
			ost.setTecnico(t);
			ost.setPapel(papel);
			ost.setPercentual(pct);
			ost.setCriadoEm(now);
			ost.setAtualizadoEm(now);

			osTecnicoRepo.save(ost);
		}

		osTecnicoRepo.flush(); // opcional, mas ajuda a “estourar” erro aqui se tiver
	}

	private static String safeAt(List<String> list, int idx) {
		if (list == null)
			return null;
		if (idx < 0 || idx >= list.size())
			return null;
		return list.get(idx);
	}

	private static Integer safeAtInt(List<Integer> list, int idx) {
		if (list == null)
			return null;
		if (idx < 0 || idx >= list.size())
			return null;
		return list.get(idx);
	}

	// =========================================================
	// Helpers
	// =========================================================

	private static String getAt(List<String> list, int idx) {
		if (list == null)
			return null;
		if (idx < 0 || idx >= list.size())
			return null;
		return list.get(idx);
	}

	private static Integer parseInt(String s, int def) {
		try {
			if (s == null)
				return def;
			String t = s.trim();
			if (t.isEmpty())
				return def;
			return Integer.parseInt(t);
		} catch (Exception e) {
			return def;
		}
	}

	private static BigDecimal parseMoney(String s) {
		try {
			if (s == null)
				return BigDecimal.ZERO;
			String t = s.trim();
			if (t.isEmpty())
				return BigDecimal.ZERO;

			t = t.replace("R$", "").trim();
			t = t.replace(".", "").replace(",", ".");
			return new BigDecimal(t);
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}
}
