package br.com.torqueos.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assinaturas")
public class Assinatura {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_assinatura")
  private Long idAssinatura;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_plano", nullable = false)
  private Plano plano;

  @Column(nullable = false, length = 20)
  private String status;

  @Column(name = "data_inicio")
  private LocalDate dataInicio;

  @Column(name = "proximo_ciclo")
  private LocalDate proximoCiclo;

  @Column(name = "gateway_ref", length = 120)
  private String gatewayRef;

  public Long getIdAssinatura() { return idAssinatura; }
  public void setIdAssinatura(Long idAssinatura) { this.idAssinatura = idAssinatura; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public Plano getPlano() { return plano; }
  public void setPlano(Plano plano) { this.plano = plano; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public LocalDate getDataInicio() { return dataInicio; }
  public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

  public LocalDate getProximoCiclo() { return proximoCiclo; }
  public void setProximoCiclo(LocalDate proximoCiclo) { this.proximoCiclo = proximoCiclo; }

  public String getGatewayRef() { return gatewayRef; }
  public void setGatewayRef(String gatewayRef) { this.gatewayRef = gatewayRef; }
}
