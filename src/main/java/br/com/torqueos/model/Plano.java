package br.com.torqueos.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "planos")
public class Plano {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_plano")
  private Long idPlano;

  @Column(nullable = false, length = 80)
  private String nome;

  @Column(name = "preco_mensal", nullable = false, precision = 10, scale = 2)
  private BigDecimal precoMensal;

  @Column(name = "limite_usuarios", nullable = false)
  private Integer limiteUsuarios;

  @Column(name = "limite_os_mes", nullable = false)
  private Integer limiteOsMes;

  @Column(nullable = false)
  private Boolean ativo = true;

  public Long getIdPlano() { return idPlano; }
  public void setIdPlano(Long idPlano) { this.idPlano = idPlano; }

  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }

  public BigDecimal getPrecoMensal() { return precoMensal; }
  public void setPrecoMensal(BigDecimal precoMensal) { this.precoMensal = precoMensal; }

  public Integer getLimiteUsuarios() { return limiteUsuarios; }
  public void setLimiteUsuarios(Integer limiteUsuarios) { this.limiteUsuarios = limiteUsuarios; }

  public Integer getLimiteOsMes() { return limiteOsMes; }
  public void setLimiteOsMes(Integer limiteOsMes) { this.limiteOsMes = limiteOsMes; }

  public Boolean getAtivo() { return ativo; }
  public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
