package br.com.torqueos.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "servicos")
public class Servico {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_servico")
  private Long idServico;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal valor;

  @Column(nullable = false)
  private Boolean ativo = true;

  // getters/setters
  public Long getIdServico() { return idServico; }
  public void setIdServico(Long idServico) { this.idServico = idServico; }
  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }
  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }
  public BigDecimal getValor() { return valor; }
  public void setValor(BigDecimal valor) { this.valor = valor; }
  public Boolean getAtivo() { return ativo; }
  public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
