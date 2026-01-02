package br.com.torqueos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.torqueos.model.Peca;
import br.com.torqueos.repository.PecaRepository;
import br.com.torqueos.tenant.TenantContext;

@Service
public class PecaService {

  private final PecaRepository repo;

  public PecaService(PecaRepository repo) {
    this.repo = repo;
  }

  // ====== métodos "base" (já existiam) ======
  public List<Peca> listAtivosDaEmpresa() {
    return repo.findByIdEmpresaAndAtivoTrueOrderByNomeAsc(TenantContext.getEmpresaId());
  }

  public Peca findPorIdDaEmpresa(Long id) {
    return repo.findByIdPecaAndIdEmpresa(id, TenantContext.getEmpresaId())
      .orElseThrow(() -> new RuntimeException("Peça inválida"));
  }

  // ====== CRUD do catálogo ======
  public Peca create(Peca p) {
    p.setIdEmpresa(TenantContext.getEmpresaId());
    if (p.getAtivo() == null) p.setAtivo(true);
    return repo.save(p);
  }

  public Peca update(Long id, Peca novo) {
    Peca p = findPorIdDaEmpresa(id);
    p.setNome(novo.getNome());
    p.setValorUnitario(novo.getValorUnitario());
    p.setAtivo(novo.getAtivo() != null ? novo.getAtivo() : true);
    return repo.save(p);
  }

  public void delete(Long id) {
    // delete físico (simples)
    repo.delete(findPorIdDaEmpresa(id));
  }

  // ====== aliases (pra usar no cadastro de OS / controllers novos) ======
  public List<Peca> list() {
    return listAtivosDaEmpresa();
  }

  public Peca find(Long id) {
    return findPorIdDaEmpresa(id);
  }
}
