package br.com.torqueos.service;

import br.com.torqueos.model.Tecnico;
import br.com.torqueos.repository.TecnicoRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TecnicoService {

  private final TecnicoRepository repo;
  private final AssinaturaService assinaturaService;

  public TecnicoService(TecnicoRepository repo, AssinaturaService assinaturaService) {
    this.repo = repo;
    this.assinaturaService = assinaturaService;
  }

  public List<Tecnico> listarDaEmpresa() {
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
    return repo.findByIdEmpresaOrderByNomeAsc(emp);
  }

  public Tecnico findById(Long id) {
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
    return repo.findByIdTecnicoAndIdEmpresa(id, emp)
        .orElseThrow(() -> new RuntimeException("Técnico não encontrado: " + id));
  }

  public Tecnico create(Tecnico t) {
    assinaturaService.validarAssinaturaAtiva();

    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");

    t.setIdEmpresa(emp);

    validar(t, null);

    if (t.getAtivo() == null) t.setAtivo(true);
    if (t.getPercentualComissao() == null) t.setPercentualComissao(0);

    t.setCriadoEm(LocalDateTime.now());
    t.setAtualizadoEm(LocalDateTime.now());
    return repo.save(t);
  }

  public Tecnico update(Long id, Tecnico novo) {
    assinaturaService.validarAssinaturaAtiva();

    Tecnico t = findById(id);
    validar(novo, id);

    t.setNome(novo.getNome());
    t.setTelefone(novo.getTelefone());
    t.setEmail(novo.getEmail());
    t.setCpf(novo.getCpf());
    t.setTipo(novo.getTipo());
    t.setPercentualComissao(novo.getPercentualComissao());
    t.setAtivo(novo.getAtivo() != null ? novo.getAtivo() : true);
    t.setObservacao(novo.getObservacao());

    t.setAtualizadoEm(LocalDateTime.now());
    return repo.save(t);
  }

  public void delete(Long id) {
    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
    repo.deleteByIdTecnicoAndIdEmpresa(id, emp);
  }

  private void validar(Tecnico t, Long idEdicao) {
    if (t.getNome() == null || t.getNome().trim().isEmpty())
      throw new RuntimeException("Nome é obrigatório.");

    String tipo = (t.getTipo() == null ? "" : t.getTipo().trim());
    if (!"MECANICO".equalsIgnoreCase(tipo) && !"AUXILIAR".equalsIgnoreCase(tipo))
      throw new RuntimeException("Tipo inválido. Use MECANICO ou AUXILIAR.");

    Integer pct = t.getPercentualComissao();
    if (pct == null) pct = 0;
    if (pct < 0 || pct > 100)
      throw new RuntimeException("Percentual de comissão deve estar entre 0 e 100.");

    Long emp = TenantContext.getEmpresaId();

    // CPF opcional, mas se informar, não pode duplicar por empresa
    if (t.getCpf() != null && !t.getCpf().trim().isEmpty()) {
      String cpf = t.getCpf().trim();
      boolean duplicado = (idEdicao == null)
          ? repo.existsByIdEmpresaAndCpf(emp, cpf)
          : repo.existsByIdEmpresaAndCpfAndIdTecnicoNot(emp, cpf, idEdicao);
      if (duplicado) throw new RuntimeException("Já existe um técnico com este CPF nesta empresa.");
    }

    // normaliza tipo
    t.setTipo(tipo.toUpperCase());
  }

  // aliases
  public List<Tecnico> list() { return listarDaEmpresa(); }
  public Tecnico find(Long id) { return findById(id); }
}
