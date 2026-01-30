package br.com.torqueos.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordem_servico_tecnicos",
       uniqueConstraints = @UniqueConstraint(name="ux_ost_empresa_os_tecnico",
                                             columnNames = {"id_empresa","id_os","id_tecnico"}))
public class OrdemServicoTecnico {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_os_tecnico")
  private Long idOsTecnico;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_os", nullable = false)
  private OrdemServico ordemServico;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_tecnico", nullable = false)
  private Tecnico tecnico;

  @Column(nullable = false, length = 20)
  private String papel; // MECANICO | AUXILIAR | RESPONSAVEL

  @Column(name = "percentual_comissao", nullable = false)
  private Integer percentual = 0; // 0..100

  @Column(name = "criado_em")
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  public Long getIdOsTecnico() { return idOsTecnico; }
  public void setIdOsTecnico(Long idOsTecnico) { this.idOsTecnico = idOsTecnico; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public OrdemServico getOrdemServico() { return ordemServico; }
  public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

  public Tecnico getTecnico() { return tecnico; }
  public void setTecnico(Tecnico tecnico) { this.tecnico = tecnico; }

  public String getPapel() { return papel; }
  public void setPapel(String papel) { this.papel = papel; }

  public Integer getPercentual() { return percentual; }
  public void setPercentual(Integer percentual) { this.percentual = percentual; }

  public LocalDateTime getCriadoEm() { return criadoEm; }
  public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

  public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
  public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
