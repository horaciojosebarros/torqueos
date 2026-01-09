package br.com.torqueos.service;

import br.com.torqueos.model.Agendamento;
import br.com.torqueos.model.Usuario;
import br.com.torqueos.repository.AgendamentoRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

  private final AgendamentoRepository repo;
  private final UsuarioService usuarioService;
  private final AssinaturaService assinaturaService;

  public AgendamentoService(AgendamentoRepository repo, UsuarioService usuarioService,
                            AssinaturaService assinaturaService) {
    this.repo = repo;
    this.usuarioService = usuarioService;
    this.assinaturaService = assinaturaService;
  }

  public List<Agendamento> findAll() {
    return repo.findByIdEmpresaOrderByInicioDesc(TenantContext.getEmpresaId());
  }

  public List<Agendamento> findFrom(LocalDateTime inicioMin) {
	  Long emp = TenantContext.getEmpresaId();
	  if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");
	  return repo.findByIdEmpresaAndInicioGreaterThanEqualOrderByInicioAsc(emp, inicioMin);
	}
  
  public Agendamento findById(Long id) {
    return repo.findByIdAgendamentoAndIdEmpresa(id, TenantContext.getEmpresaId())
      .orElseThrow(() -> new RuntimeException("Agendamento não encontrado: " + id));
  }

  public Agendamento create(Agendamento a, Long idUsuarioResponsavel) {
    assinaturaService.validarAssinaturaAtiva();

    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");

    if (a.getInicio() == null) throw new RuntimeException("Data/hora inicial é obrigatória.");
    if (a.getInicio().isBefore(LocalDateTime.now())) throw new RuntimeException("Agendamento não pode ser no passado.");

    if (a.getDuracaoMinutos() == null || a.getDuracaoMinutos() <= 0) a.setDuracaoMinutos(60);
    if (a.getStatus() == null || a.getStatus().trim().isEmpty()) a.setStatus("AGENDADO");

      a.setUsuarioResponsavel(null);

    a.setIdEmpresa(emp);
    a.setCriadoEm(LocalDateTime.now());
    a.setAtualizadoEm(LocalDateTime.now());

    return repo.save(a);
  }

  public Agendamento update(Long id, Agendamento novo, Long idUsuarioResponsavel) {
    assinaturaService.validarAssinaturaAtiva();

    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");

    Agendamento a = findById(id);

    if (novo.getInicio() == null) throw new RuntimeException("Data/hora inicial é obrigatória.");
    if (novo.getInicio().isBefore(LocalDateTime.now())) throw new RuntimeException("Agendamento não pode ser no passado.");

    Integer dur = (novo.getDuracaoMinutos() == null || novo.getDuracaoMinutos() <= 0) ? 60 : novo.getDuracaoMinutos();

    a.setInicio(novo.getInicio());
    a.setDuracaoMinutos(dur);
    a.setDescricao(novo.getDescricao());
    a.setStatus((novo.getStatus() == null || novo.getStatus().trim().isEmpty()) ? a.getStatus() : novo.getStatus());

    // responsável opcional
      a.setUsuarioResponsavel(null);

    a.setAtualizadoEm(LocalDateTime.now());
    return repo.save(a);
  }

  public void delete(Long id) {
    Agendamento a = findById(id);
    repo.delete(a);
  }

  // ===== aliases =====
  public List<Agendamento> list() {
    return findAll();
  }

  public Agendamento find(Long id) {
    return findById(id);
  }
}
