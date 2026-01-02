package br.com.torqueos.service;

import br.com.torqueos.model.Assinatura;
import br.com.torqueos.model.Pagamento;
import br.com.torqueos.repository.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {
  private final PagamentoRepository repo;
  private final AssinaturaService assinaturaService;

  public PagamentoService(PagamentoRepository repo, AssinaturaService assinaturaService) {
    this.repo = repo;
    this.assinaturaService = assinaturaService;
  }

  public List<Pagamento> listarDaEmpresa() {
    Assinatura a = assinaturaService.getDaEmpresaAtual();
    return repo.findByAssinatura_IdAssinatura(a.getIdAssinatura());
  }

  public Pagamento criar(Pagamento p) {
    Assinatura a = assinaturaService.getDaEmpresaAtual();
    p.setAssinatura(a);
    if (p.getDataPagamento() == null) p.setDataPagamento(LocalDateTime.now());
    if (p.getStatus() == null || p.getStatus().trim().isEmpty()) p.setStatus("PAGO");
    return repo.save(p);
  }

  public Pagamento findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new RuntimeException("Pagamento não encontrado: " + id));
  }

  public Pagamento update(Long id, Pagamento novo) {
    Pagamento p = findById(id);
    p.setValor(novo.getValor());
    p.setStatus(novo.getStatus());
    p.setReferencia(novo.getReferencia());
    p.setDataPagamento(novo.getDataPagamento());
    return repo.save(p);
  }

  public void delete(Long id) { repo.deleteById(id); }
}
