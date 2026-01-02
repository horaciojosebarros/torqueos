package br.com.torqueos.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "pecas")
public class Peca {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_peca")
  private Long idPeca;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal valorUnitario;

  @Column(nullable = false)
  private Boolean ativo = true;

  // getters/setters
  public Long getIdPeca() { return idPeca; }
  public void setIdPeca(Long idPeca) { this.idPeca = idPeca; }
  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }
  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }
  public BigDecimal getValorUnitario() { return valorUnitario; }
  public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
  public Boolean getAtivo() { return ativo; }
  public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
