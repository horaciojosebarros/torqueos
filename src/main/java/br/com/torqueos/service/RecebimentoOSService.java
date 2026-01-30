package br.com.torqueos.service;

import br.com.torqueos.model.OrdemServico;
import br.com.torqueos.model.PecaOS;
import br.com.torqueos.model.RecebimentoOS;
import br.com.torqueos.model.ServicoOS;
import br.com.torqueos.repository.RecebimentoOSRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecebimentoOSService {

  private final RecebimentoOSRepository repo;
  private final OrdemServicoService ordemServicoService;
  private final ServicoOSService servicoOSService;
  private final PecaOSService pecaOSService;
  private final AssinaturaService assinaturaService; // regra SaaS (gate de uso)

  public RecebimentoOSService(RecebimentoOSRepository repo,
                              OrdemServicoService ordemServicoService,
                              ServicoOSService servicoOSService,
                              PecaOSService pecaOSService,
                              AssinaturaService assinaturaService) {
    this.repo = repo;
    this.ordemServicoService = ordemServicoService;
    this.servicoOSService = servicoOSService;
    this.pecaOSService = pecaOSService;
    this.assinaturaService = assinaturaService;
  }

  public List<RecebimentoOS> listarDaEmpresa() {
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
    return repo.findByIdEmpresaOrderByDataVencimentoAsc(emp);
  }

  public RecebimentoOS findById(Long id) {
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
    return repo.findByIdRecebimentoAndIdEmpresa(id, emp)
        .orElseThrow(() -> new RuntimeException("Recebimento não encontrado: " + id));
  }

  public RecebimentoOS create(RecebimentoOS r, Long osId) {
    assinaturaService.validarAssinaturaAtiva();

    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");

    if (osId == null) throw new RuntimeException("Selecione uma Ordem de Serviço.");
    OrdemServico os = ordemServicoService.findById(osId); // já filtra tenant
    r.setOrdemServico(os);
    r.setIdEmpresa(emp);

    normalizarDefaults(r);

    // backend recalcula SEMPRE
    recalcularValorTotalDaOs(r);

    normalizarStatus(r);

    r.setCriadoEm(LocalDateTime.now());
    r.setAtualizadoEm(LocalDateTime.now());
    return repo.save(r);
  }

  public RecebimentoOS update(Long id, RecebimentoOS novo, Long osId) {
    assinaturaService.validarAssinaturaAtiva();

    RecebimentoOS r = findById(id);

    boolean jaPago = isPago(r);

    // Se já está pago, NÃO pode trocar OS
    if (!jaPago) {
      if (osId == null) throw new RuntimeException("Selecione uma Ordem de Serviço.");
      r.setOrdemServico(ordemServicoService.findById(osId));
    } else {
      // se vier diferente, ignora (mantém a atual)
      // regra: só troca excluindo o recebimento
    }

    r.setTipoPagamento(novo.getTipoPagamento());
    r.setStatus(novo.getStatus());

    r.setDesconto(nz(novo.getDesconto()));
    r.setJuros(nz(novo.getJuros()));
    r.setMulta(nz(novo.getMulta()));
    r.setParcelas((novo.getParcelas() == null || novo.getParcelas() <= 0) ? 1 : novo.getParcelas());

    r.setDataVencimento(novo.getDataVencimento());
    r.setDataPagamento(novo.getDataPagamento());

    r.setReferencia(novo.getReferencia());
    r.setDocumentoRef(novo.getDocumentoRef());
    r.setObservacao(novo.getObservacao());

    normalizarDefaults(r);

    // backend recalcula SEMPRE
    recalcularValorTotalDaOs(r);

    normalizarStatus(r);

    r.setAtualizadoEm(LocalDateTime.now());
    return repo.save(r);
  }

  public void delete(Long id) {
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
    repo.deleteByIdRecebimentoAndIdEmpresa(id, emp);
  }

  // ===========================
  // Regras internas
  // ===========================

  private void normalizarDefaults(RecebimentoOS r) {
    if (r.getParcelas() == null || r.getParcelas() <= 0) r.setParcelas(1);
    if (r.getTipoPagamento() == null || r.getTipoPagamento().trim().isEmpty()) r.setTipoPagamento("PIX");
    if (r.getStatus() == null || r.getStatus().trim().isEmpty()) r.setStatus("PENDENTE");
    if (r.getDataVencimento() == null) r.setDataVencimento(LocalDate.now());

    if (r.getDesconto() == null) r.setDesconto(BigDecimal.ZERO);
    if (r.getJuros() == null) r.setJuros(BigDecimal.ZERO);
    if (r.getMulta() == null) r.setMulta(BigDecimal.ZERO);
  }

  private void normalizarStatus(RecebimentoOS r) {
    // Se informou data_pagamento, vira PAGO automaticamente
    if (r.getDataPagamento() != null && !"PAGO".equalsIgnoreCase(r.getStatus())) {
      r.setStatus("PAGO");
    }
    // Se status é PAGO e não informou data_pagamento, seta agora
    if ("PAGO".equalsIgnoreCase(r.getStatus()) && r.getDataPagamento() == null) {
      r.setDataPagamento(LocalDateTime.now());
    }
  }

  private void recalcularValorTotalDaOs(RecebimentoOS r) {
    Long idOs = (r.getOrdemServico() != null ? r.getOrdemServico().getIdOs() : null);
    if (idOs == null) throw new RuntimeException("Recebimento sem OS vinculada.");

    BigDecimal totalServ = BigDecimal.ZERO;
    List<ServicoOS> servicos = servicoOSService.findByOs(idOs);
    if (servicos != null) {
      for (ServicoOS s : servicos) totalServ = totalServ.add(nz(s.getValor()));
    }

    BigDecimal totalPecas = BigDecimal.ZERO;
    List<PecaOS> pecas = pecaOSService.findByOs(idOs);
    if (pecas != null) {
      for (PecaOS p : pecas) {
        BigDecimal unit = nz(p.getValorUnitario());
        int qtd = (p.getQuantidade() == null ? 0 : p.getQuantidade());
        totalPecas = totalPecas.add(unit.multiply(new BigDecimal(qtd)));
      }
    }

    BigDecimal base = totalServ.add(totalPecas);

    BigDecimal finalVal = base
        .subtract(nz(r.getDesconto()))
        .add(nz(r.getJuros()))
        .add(nz(r.getMulta()));

    if (finalVal.compareTo(BigDecimal.ZERO) < 0) finalVal = BigDecimal.ZERO;

    r.setValorTotal(finalVal.setScale(2, BigDecimal.ROUND_HALF_UP));
  }

  private boolean isPago(RecebimentoOS r) {
    return r.getDataPagamento() != null || "PAGO".equalsIgnoreCase(r.getStatus());
  }

  private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }

  // aliases
  public List<RecebimentoOS> list() { return listarDaEmpresa(); }
  public RecebimentoOS find(Long id) { return findById(id); }
  
  public List<RecebimentoOS> listarPagosDaEmpresa() {
	  Long emp = TenantContext.getEmpresaId();
	  if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
	  return repo.findByIdEmpresaAndStatusIgnoreCase(emp, "PAGO");
	}

}
