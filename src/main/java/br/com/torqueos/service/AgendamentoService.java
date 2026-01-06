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

  public AgendamentoService(AgendamentoRepository repo, UsuarioService usuarioService, AssinaturaService assinaturaService) {
    this.repo = repo;
    this.usuarioService = usuarioService;
    this.assinaturaService = assinaturaService;
  }

  public List<Agendamento> findAll() {
    return repo.findByIdEmpresaOrderByInicioDesc(TenantContext.getEmpresaId());
  }

  public Agendamento findById(Long id) {
    return repo.findByIdAgendamentoAndIdEmpresa(id, TenantContext.getEmpresaId())
        .orElseThrow(() -> new RuntimeException("Agendamento não encontrado: " + id));
  }

  public Agendamento create(Agendamento a, Long idUsuarioResponsavel) {
    assinaturaService.validarAssinaturaAtiva();

    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");

    if (idUsuarioResponsavel == null) throw new RuntimeException("Responsável é obrigatório.");
    Usuario u = usuarioService.findById(idUsuarioResponsavel); // se não tiver, crie esse alias no UsuarioService

    if (a.getInicio() == null) throw new RuntimeException("Data/hora inicial é obrigatória.");
    if (a.getDuracaoMinutos() == null || a.getDuracaoMinutos() <= 0) a.setDuracaoMinutos(60);
    if (a.getStatus() == null || a.getStatus().trim().isEmpty()) a.setStatus("AGENDADO");

    // opcional: bloquear agendamento no passado
    // if (a.getInicio().isBefore(LocalDateTime.now())) throw new RuntimeException("Agendamento deve ser no futuro.");

    // opcional: conflito
    LocalDateTime fimNovo = a.getInicio().plusMinutes(a.getDuracaoMinutos());
    boolean conflito = repo.existeConflito(emp, u.getIdUsuario(), a.getInicio(), fimNovo, null);
    if (conflito) throw new RuntimeException("Conflito de horário para o responsável no intervalo informado.");

    a.setIdEmpresa(emp);
    a.setUsuarioResponsavel(u);
    a.setCriadoEm(LocalDateTime.now());
    a.setAtualizadoEm(LocalDateTime.now());

    return repo.save(a);
  }

  public Agendamento update(Long id, Agendamento novo, Long idUsuarioResponsavel) {
    assinaturaService.validarAssinaturaAtiva();

    Agendamento a = findById(id);

    if (idUsuarioResponsavel == null) throw new RuntimeException("Responsável é obrigatório.");
    Usuario u = usuarioService.findById(idUsuarioResponsavel);

    if (novo.getInicio() == null) throw new RuntimeException("Data/hora inicial é obrigatória.");
    Integer dur = (novo.getDuracaoMinutos() == null || novo.getDuracaoMinutos() <= 0) ? 60 : novo.getDuracaoMinutos();

    // opcional: conflito
    LocalDateTime fimNovo = novo.getInicio().plusMinutes(dur);
    boolean conflito = repo.existeConflito(a.getIdEmpresa(), u.getIdUsuario(), novo.getInicio(), fimNovo, a.getIdAgendamento());
    if (conflito) throw new RuntimeException("Conflito de horário para o responsável no intervalo informado.");

    a.setUsuarioResponsavel(u);
    a.setInicio(novo.getInicio());
    a.setDuracaoMinutos(dur);
    a.setDescricao(novo.getDescricao());
    a.setStatus((novo.getStatus() == null || novo.getStatus().trim().isEmpty()) ? a.getStatus() : novo.getStatus());
    a.setAtualizadoEm(LocalDateTime.now());

    return repo.save(a);
  }

  public void delete(Long id) {
    Agendamento a = findById(id);
    repo.delete(a);
  }

  // ===== aliases (igual seu padrão atual) =====
  public List<Agendamento> list() { return findAll(); }
  public Agendamento find(Long id) { return findById(id); }
}
