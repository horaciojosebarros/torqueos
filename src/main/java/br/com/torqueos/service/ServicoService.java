package br.com.torqueos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.torqueos.model.Servico;
import br.com.torqueos.repository.ServicoRepository;
import br.com.torqueos.tenant.TenantContext;

@Service
public class ServicoService {

  private final ServicoRepository repo;

  public ServicoService(ServicoRepository repo) {
    this.repo = repo;
  }

  // ====== métodos "base" (já existiam) ======
  public List<Servico> listAtivosDaEmpresa() {
    return repo.findByIdEmpresaAndAtivoTrueOrderByNomeAsc(TenantContext.getEmpresaId());
  }

  public Servico findPorIdDaEmpresa(Long id) {
    return repo.findByIdServicoAndIdEmpresa(id, TenantContext.getEmpresaId())
      .orElseThrow(() -> new RuntimeException("Serviço inválido"));
  }

  // ====== CRUD do catálogo ======
  public Servico create(Servico s) {
    s.setIdEmpresa(TenantContext.getEmpresaId());
    if (s.getAtivo() == null) s.setAtivo(true);
    return repo.save(s);
  }

  public Servico update(Long id, Servico novo) {
    Servico s = findPorIdDaEmpresa(id);
    s.setNome(novo.getNome());
    s.setValor(novo.getValor());
    s.setAtivo(novo.getAtivo() != null ? novo.getAtivo() : true);
    return repo.save(s);
  }

  public void delete(Long id) {
    repo.delete(findPorIdDaEmpresa(id));
  }

  // ====== aliases (pra usar no cadastro de OS / controllers novos) ======
  public List<Servico> list() {
    return listAtivosDaEmpresa();
  }

  public Servico find(Long id) {
    return findPorIdDaEmpresa(id);
  }
}
