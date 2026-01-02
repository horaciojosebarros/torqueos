package br.com.torqueos.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.torqueos.model.OrdemServico;
import br.com.torqueos.model.PecaOS;
import br.com.torqueos.model.ServicoOS;
import br.com.torqueos.repository.OrdemServicoRepository;
import br.com.torqueos.repository.PecaOSRepository;
import br.com.torqueos.repository.ServicoOSRepository;
import br.com.torqueos.tenant.TenantContext;

@Service
public class OrdemServicoService {

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

  public OrdemServicoService(
      OrdemServicoRepository ordemRepo,
      ServicoOSRepository servicoOSRepo,
      PecaOSRepository pecaOSRepo,
      ClienteService clienteService,
      VeiculoService veiculoService,
      ServicoService servicoService,
      PecaService pecaService,
      AssinaturaService assinaturaService
  ) {
    this.ordemRepo = ordemRepo;
    this.servicoOSRepo = servicoOSRepo;
    this.pecaOSRepo = pecaOSRepo;
    this.clienteService = clienteService;
    this.veiculoService = veiculoService;
    this.servicoService = servicoService;
    this.pecaService = pecaService;
    this.assinaturaService = assinaturaService;
  }

  // =========================================================
  // LIST / GET (aliases que seu controller usa)
  // =========================================================

  @Transactional(readOnly = true)
  public List<OrdemServico> findAll() {
    // tenant-safe mesmo usando JpaRepository#findAll()
    Long emp = TenantContext.getEmpresaId();
    return ordemRepo.findAll().stream()
      .filter(o -> o != null && emp != null && emp.equals(o.getIdEmpresa()))
      .sorted(Comparator.comparing(OrdemServico::getIdOs, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public OrdemServico findById(Long id) {
    return findPorIdDaEmpresa(id);
  }

  @Transactional(readOnly = true)
  public OrdemServico findPorIdDaEmpresa(Long id) {
    if (id == null) throw new RuntimeException("OS inválida");
    Long emp = TenantContext.getEmpresaId();

    Optional<OrdemServico> opt = ordemRepo.findById(id);
    OrdemServico os = opt.orElseThrow(() -> new RuntimeException("OS não encontrada: " + id));

    if (os.getIdEmpresa() == null || !os.getIdEmpresa().equals(emp)) {
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

    ordemRepo.delete(os);
  }

  // =========================================================
  // CREATE/UPDATE - MODO ANTIGO (texto) - COMPATÍVEL COM SEU CONTROLLER
  // =========================================================

  @Transactional
  public OrdemServico create(OrdemServico novo,
                            Long clienteId,
                            Long veiculoId,
                            List<String> servicoDescricao,
                            List<String> servicoValor,
                            List<String> pecaDescricao,
                            List<String> pecaQuantidade,
                            List<String> pecaValorUnitario) {

    assinaturaService.validarAssinaturaAtiva();

    Long emp = TenantContext.getEmpresaId();
    OrdemServico os = new OrdemServico();
    os.setIdEmpresa(emp);

    aplicarCamposEditaveis(os, novo);

    if (clienteId != null) os.setCliente(clienteService.find(clienteId));
    else os.setCliente(null);

    if (veiculoId != null) os.setVeiculo(veiculoService.find(veiculoId));
    else os.setVeiculo(null);

    OrdemServico saved = ordemRepo.save(os);

    // grava itens por texto (id_catalogo = null)
    salvarItensPorTexto(saved, servicoDescricao, servicoValor, pecaDescricao, pecaQuantidade, pecaValorUnitario);

    return saved;
  }

  @Transactional
  public OrdemServico update(Long idOs,
                            OrdemServico novo,
                            Long clienteId,
                            Long veiculoId,
                            List<String> servicoDescricao,
                            List<String> servicoValor,
                            List<String> pecaDescricao,
                            List<String> pecaQuantidade,
                            List<String> pecaValorUnitario) {

    assinaturaService.validarAssinaturaAtiva();

    OrdemServico os = findPorIdDaEmpresa(idOs);

    aplicarCamposEditaveis(os, novo);

    if (clienteId != null) os.setCliente(clienteService.find(clienteId));
    else os.setCliente(null);

    if (veiculoId != null) os.setVeiculo(veiculoService.find(veiculoId));
    else os.setVeiculo(null);

    OrdemServico saved = ordemRepo.save(os);

    // remove e recria itens
    removerItens(saved.getIdOs());
    salvarItensPorTexto(saved, servicoDescricao, servicoValor, pecaDescricao, pecaQuantidade, pecaValorUnitario);

    return saved;
  }

  // =========================================================
  // CREATE/UPDATE - MODO PROFISSIONAL (catálogo por ID)
  // (você vai usar quando seu form mandar servicoId/pecaId/pecaQuantidade)
  // =========================================================

  @Transactional
  public OrdemServico create(OrdemServico novo,
                            Long clienteId,
                            Long veiculoId,
                            List<Long> servicoId,
                            List<Long> pecaId,
                            List<Integer> pecaQuantidade) {

    assinaturaService.validarAssinaturaAtiva();

    Long emp = TenantContext.getEmpresaId();
    OrdemServico os = new OrdemServico();
    os.setIdEmpresa(emp);

    aplicarCamposEditaveis(os, novo);

    if (clienteId != null) os.setCliente(clienteService.find(clienteId));
    else os.setCliente(null);

    if (veiculoId != null) os.setVeiculo(veiculoService.find(veiculoId));
    else os.setVeiculo(null);

    OrdemServico saved = ordemRepo.save(os);

    salvarItensPorCatalogo(saved,
        servicoId != null ? servicoId : Collections.emptyList(),
        pecaId != null ? pecaId : Collections.emptyList(),
        pecaQuantidade);

    return saved;
  }

  @Transactional
  public OrdemServico update(Long idOs,
                            OrdemServico novo,
                            Long clienteId,
                            Long veiculoId,
                            List<Long> servicoId,
                            List<Long> pecaId,
                            List<Integer> pecaQuantidade) {

    assinaturaService.validarAssinaturaAtiva();

    OrdemServico os = findPorIdDaEmpresa(idOs);

    aplicarCamposEditaveis(os, novo);

    if (clienteId != null) os.setCliente(clienteService.find(clienteId));
    else os.setCliente(null);

    if (veiculoId != null) os.setVeiculo(veiculoService.find(veiculoId));
    else os.setVeiculo(null);

    OrdemServico saved = ordemRepo.save(os);

    removerItens(saved.getIdOs());
    salvarItensPorCatalogo(saved,
        servicoId != null ? servicoId : Collections.emptyList(),
        pecaId != null ? pecaId : Collections.emptyList(),
        pecaQuantidade);

    return saved;
  }

  // =========================================================
  // INTERNOS
  // =========================================================

  private void aplicarCamposEditaveis(OrdemServico destino, OrdemServico origem) {
    if (origem == null) return;

    // ajuste conforme sua entidade:
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

  // -------- itens por texto (modo antigo) --------
  private void salvarItensPorTexto(OrdemServico os,
                                  List<String> servicoDescricao,
                                  List<String> servicoValor,
                                  List<String> pecaDescricao,
                                  List<String> pecaQuantidade,
                                  List<String> pecaValorUnitario) {

    Long emp = TenantContext.getEmpresaId();

    // Serviços
    if (servicoDescricao != null) {
      for (int i = 0; i < servicoDescricao.size(); i++) {
        String desc = servicoDescricao.get(i);
        if (desc == null || desc.trim().isEmpty()) continue;

        BigDecimal valor = parseMoney(getAt(servicoValor, i));

        ServicoOS item = new ServicoOS();
        item.setIdEmpresa(emp);
        item.setOrdemServico(os);

        // catálogo ainda não existe nesse modo:
        item.setIdServicoCatalogo(null);

        item.setDescricao(desc.trim());
        item.setValor(valor);

        servicoOSRepo.save(item);
      }
    }

    // Peças
    if (pecaDescricao != null) {
      for (int i = 0; i < pecaDescricao.size(); i++) {
        String desc = pecaDescricao.get(i);
        if (desc == null || desc.trim().isEmpty()) continue;

        Integer qtd = parseInt(getAt(pecaQuantidade, i), 1);
        if (qtd == null || qtd < 1) qtd = 1;

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

  // -------- itens por catálogo (modo profissional) --------
  private void salvarItensPorCatalogo(OrdemServico os,
                                     List<Long> servicoId,
                                     List<Long> pecaId,
                                     List<Integer> pecaQuantidade) {

    Long emp = TenantContext.getEmpresaId();

    // Serviços (selecionados)
    if (servicoId != null) {
      for (Long idS : servicoId) {
        if (idS == null) continue;

        br.com.torqueos.model.Servico cat = servicoService.find(idS);

        ServicoOS item = new ServicoOS();
        item.setIdEmpresa(emp);
        item.setOrdemServico(os);

        // ✅ grava vínculo com catálogo
        item.setIdServicoCatalogo(cat.getIdServico());

        // snapshot
        item.setDescricao(cat.getNome());
        item.setValor(cat.getValor());

        servicoOSRepo.save(item);
      }
    }

    // Peças (selecionadas)
    if (pecaId != null) {
      for (int i = 0; i < pecaId.size(); i++) {
        Long idP = pecaId.get(i);
        if (idP == null) continue;

        Integer qtd = 1;
        if (pecaQuantidade != null && pecaQuantidade.size() > i && pecaQuantidade.get(i) != null) {
          qtd = pecaQuantidade.get(i);
        }
        if (qtd == null || qtd < 1) qtd = 1;

        br.com.torqueos.model.Peca cat = pecaService.find(idP);

        PecaOS item = new PecaOS();
        item.setIdEmpresa(emp);
        item.setOrdemServico(os);

        // ✅ grava vínculo com catálogo
        item.setIdPecaCatalogo(cat.getIdPeca());

        // snapshot
        item.setDescricao(cat.getNome());
        item.setQuantidade(qtd);
        item.setValorUnitario(cat.getValorUnitario());

        pecaOSRepo.save(item);
      }
    }
  }

  // =========================================================
  // Helpers
  // =========================================================

  private static String getAt(List<String> list, int idx) {
    if (list == null) return null;
    if (idx < 0 || idx >= list.size()) return null;
    return list.get(idx);
  }

  private static Integer parseInt(String s, int def) {
    try {
      if (s == null) return def;
      String t = s.trim();
      if (t.isEmpty()) return def;
      return Integer.parseInt(t);
    } catch (Exception e) {
      return def;
    }
  }

  private static BigDecimal parseMoney(String s) {
    try {
      if (s == null) return BigDecimal.ZERO;
      String t = s.trim();
      if (t.isEmpty()) return BigDecimal.ZERO;

      // suporta "1.234,56" e "1234.56"
      t = t.replace("R$", "").trim();
      t = t.replace(".", "").replace(",", ".");
      return new BigDecimal(t);
    } catch (Exception e) {
      return BigDecimal.ZERO;
    }
  }
}
