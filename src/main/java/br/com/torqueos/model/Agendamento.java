package br.com.torqueos.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_agendamento")
  private Long idAgendamento;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_usuario_responsavel", nullable = false)
  private Usuario usuarioResponsavel;

  @Column(nullable = false)
  private LocalDateTime inicio;

  @Column(name = "duracao_minutos", nullable = false)
  private Integer duracaoMinutos = 60;

  @Column(length = 255)
  private String descricao;

  @Column(nullable = false, length = 20)
  private String status = "AGENDADO";

  @Column(name = "criado_em")
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  public Long getIdAgendamento() { return idAgendamento; }
  public void setIdAgendamento(Long idAgendamento) { this.idAgendamento = idAgendamento; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public Usuario getUsuarioResponsavel() { return usuarioResponsavel; }
  public void setUsuarioResponsavel(Usuario usuarioResponsavel) { this.usuarioResponsavel = usuarioResponsavel; }

  public LocalDateTime getInicio() { return inicio; }
  public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

  public Integer getDuracaoMinutos() { return duracaoMinutos; }
  public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

  public String getDescricao() { return descricao; }
  public void setDescricao(String descricao) { this.descricao = descricao; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public LocalDateTime getCriadoEm() { return criadoEm; }
  public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

  public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
  public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
