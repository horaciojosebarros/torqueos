package br.com.torqueos.service;

import br.com.torqueos.model.Assinatura;
import br.com.torqueos.model.Plano;
import br.com.torqueos.repository.AssinaturaRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AssinaturaService {

  private final AssinaturaRepository repo;
  private final PlanoService planoService;

  public AssinaturaService(AssinaturaRepository repo, PlanoService planoService) {
    this.repo = repo;
    this.planoService = planoService;
  }

  public Assinatura getDaEmpresaAtual() {
    Long emp = TenantContext.getEmpresaId();
    return repo.findByIdEmpresa(emp).orElseThrow(() -> new RuntimeException("Empresa sem assinatura configurada."));
  }

  public Plano getPlanoAtual() {
    return getDaEmpresaAtual().getPlano();
  }

  public void validarAssinaturaAtiva() {
    Assinatura a = getDaEmpresaAtual();
    if (!"ATIVA".equalsIgnoreCase(a.getStatus())) {
      throw new RuntimeException("Assinatura não está ATIVA (status=" + a.getStatus() + ").");
    }
    // regra simplificada: se proximo_ciclo já passou, considerar "INADIMPLENTE"
    if (a.getProximoCiclo() != null && a.getProximoCiclo().isBefore(LocalDate.now())) {
      throw new RuntimeException("Assinatura vencida (proximo_ciclo=" + a.getProximoCiclo() + ").");
    }
  }

  public Assinatura atualizarPlano(Long planoId) {
    Assinatura a = getDaEmpresaAtual();
    a.setPlano(planoService.findById(planoId));
    return repo.save(a);
  }

  public Assinatura save(Assinatura a) {
    // somente da empresa atual
    a.setIdEmpresa(TenantContext.getEmpresaId());
    return repo.save(a);
  }
}
