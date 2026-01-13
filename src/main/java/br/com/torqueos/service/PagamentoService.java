package br.com.torqueos.service;

import br.com.torqueos.model.Assinatura;
import br.com.torqueos.model.Pagamento;
import br.com.torqueos.repository.PagamentoRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

  private final PagamentoRepository repo;
  private final OrdemServicoService ordemServicoService;
  private final AssinaturaService assinaturaService;

  public PagamentoService(PagamentoRepository repo,
                          OrdemServicoService ordemServicoService,
                          AssinaturaService assinaturaService) {
    this.repo = repo;
    this.ordemServicoService = ordemServicoService;
    this.assinaturaService = assinaturaService;
  }

  // usado no seu controller atual
  public List<Pagamento> listarDaEmpresa() {
    return findAll();
  }

  public List<Pagamento> findAll() {
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
    return repo.findByIdEmpresaOrderByDataVencimentoAsc(emp);
  }

  public Pagamento findById(Long id) {
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
    return repo.findByIdPagamentoAndIdEmpresa(id, emp)
      .orElseThrow(() -> new RuntimeException("Pagamento não encontrado: " + id));
  }

  public Pagamento create(Pagamento p, Long idOs) {
    assinaturaService.validarAssinaturaAtiva();
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");

    p.setIdEmpresa(emp);

    if (idOs != null) {
      p.setOrdemServico(ordemServicoService.findById(idOs)); // já filtra por tenant
    } else {
      p.setOrdemServico(null);
    }

    normalizarStatusDatas(p);

    if (p.getDataVencimento() == null) {
      // padrão: vence hoje (ou +7 dias se preferir)
      p.setDataVencimento(LocalDate.now());
    }

    p.setCriadoEm(LocalDateTime.now());
    p.setAtualizadoEm(LocalDateTime.now());
    return repo.save(p);
  }

  public Pagamento update(Long id, Pagamento novo, Long idOs) {
    assinaturaService.validarAssinaturaAtiva();

    Pagamento p = findById(id);

    if (idOs != null) p.setOrdemServico(ordemServicoService.findById(idOs));
    else p.setOrdemServico(null);

    // mantém assinatura e empresa originais (segurança)
    // p.setIdEmpresa(p.getIdEmpresa());
    // p.setAssinatura(p.getAssinatura());

    p.setStatus(novo.getStatus());
    p.setTipoPagamento(novo.getTipoPagamento());
    p.setDataVencimento(novo.getDataVencimento());
    p.setDataPagamento(novo.getDataPagamento());
    p.setValor(novo.getValor());
    p.setDesconto(novo.getDesconto());
    p.setJuros(novo.getJuros());
    p.setMulta(novo.getMulta());
    p.setParcelas(novo.getParcelas());
    p.setObservacao(novo.getObservacao());
    p.setDocumentoRef(novo.getDocumentoRef());
    p.setReferencia(novo.getReferencia());

    normalizarStatusDatas(p);

    p.setAtualizadoEm(LocalDateTime.now());
    return repo.save(p);
  }

  public void delete(Long id) {
    repo.delete(findById(id));
  }

  private void normalizarStatusDatas(Pagamento p) {
    if (p.getStatus() == null || p.getStatus().trim().isEmpty()) {
      p.setStatus("PENDENTE");
    }

    if (p.getTipoPagamento() == null || p.getTipoPagamento().trim().isEmpty()) {
      p.setTipoPagamento("PIX");
    }

    // se informou data_pagamento e status não é PAGO, ajusta
    if (p.getDataPagamento() != null && !"PAGO".equalsIgnoreCase(p.getStatus())) {
      p.setStatus("PAGO");
    }

    // se status é PAGO mas não tem data_pagamento, seta hoje
    if ("PAGO".equalsIgnoreCase(p.getStatus()) && p.getDataPagamento() == null) {
      p.setDataPagamento(LocalDate.now());
    }
  }

  // aliases
  public List<Pagamento> list() { return findAll(); }
  public Pagamento find(Long id) { return findById(id); }
}
