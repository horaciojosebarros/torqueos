package br.com.torqueos.service;

import br.com.torqueos.model.OrdemServico;
import br.com.torqueos.model.RecebimentoOS;
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
  private final AssinaturaService assinaturaService; // só pra bloquear uso sem assinatura ativa (regra SaaS)
  
  public RecebimentoOSService(RecebimentoOSRepository repo,
                              OrdemServicoService ordemServicoService,
                              AssinaturaService assinaturaService) {
    this.repo = repo;
    this.ordemServicoService = ordemServicoService;
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
    // Regra do seu SaaS: só deixa operar se assinatura estiver ok (isso NÃO é "pagamento de assinatura")
    assinaturaService.validarAssinaturaAtiva();

    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");

    if (osId == null) throw new RuntimeException("Selecione uma Ordem de Serviço.");
    OrdemServico os = ordemServicoService.findById(osId); // deve filtrar por tenant
    r.setOrdemServico(os);

    r.setIdEmpresa(emp);

    if (r.getValorTotal() == null) throw new RuntimeException("Valor total é obrigatório.");
    if (r.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("Valor total deve ser > 0.");

    if (r.getParcelas() == null || r.getParcelas() <= 0) r.setParcelas(1);
    if (r.getTipoPagamento() == null || r.getTipoPagamento().trim().isEmpty()) r.setTipoPagamento("PIX");
    if (r.getStatus() == null || r.getStatus().trim().isEmpty()) r.setStatus("PENDENTE");
    if (r.getDataVencimento() == null) r.setDataVencimento(LocalDate.now());

    normalizarStatus(r);

    r.setCriadoEm(LocalDateTime.now());
    r.setAtualizadoEm(LocalDateTime.now());
    return repo.save(r);
  }

  public RecebimentoOS update(Long id, RecebimentoOS novo, Long osId) {
    assinaturaService.validarAssinaturaAtiva();

    RecebimentoOS r = findById(id);

    if (osId == null) throw new RuntimeException("Selecione uma Ordem de Serviço.");
    r.setOrdemServico(ordemServicoService.findById(osId));

    r.setTipoPagamento(novo.getTipoPagamento());
    r.setStatus(novo.getStatus());
    r.setValorTotal(novo.getValorTotal());
    r.setDesconto(novo.getDesconto());
    r.setJuros(novo.getJuros());
    r.setMulta(novo.getMulta());
    r.setParcelas(novo.getParcelas());
    r.setDataVencimento(novo.getDataVencimento());
    r.setDataPagamento(novo.getDataPagamento());
    r.setReferencia(novo.getReferencia());
    r.setDocumentoRef(novo.getDocumentoRef());
    r.setObservacao(novo.getObservacao());

    if (r.getParcelas() == null || r.getParcelas() <= 0) r.setParcelas(1);
    if (r.getDataVencimento() == null) r.setDataVencimento(LocalDate.now());

    normalizarStatus(r);

    r.setAtualizadoEm(LocalDateTime.now());
    return repo.save(r);
  }

  public void delete(Long id) {
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
    repo.deleteByIdRecebimentoAndIdEmpresa(id, emp);
  }

  private void normalizarStatus(RecebimentoOS r) {
    // Se marcou data_pagamento, vira PAGO automaticamente
    if (r.getDataPagamento() != null && !"PAGO".equalsIgnoreCase(r.getStatus())) {
      r.setStatus("PAGO");
    }
    // Se status é PAGO e não informou data_pagamento, seta agora
    if ("PAGO".equalsIgnoreCase(r.getStatus()) && r.getDataPagamento() == null) {
      r.setDataPagamento(LocalDateTime.now());
    }
  }

  // aliases (padrão do seu projeto)
  public List<RecebimentoOS> list() { return listarDaEmpresa(); }
  public RecebimentoOS find(Long id) { return findById(id); }
}
