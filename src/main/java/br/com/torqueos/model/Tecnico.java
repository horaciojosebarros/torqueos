package br.com.torqueos.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tecnicos")
public class Tecnico {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_tecnico")
  private Long idTecnico;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(length = 20)
  private String telefone;

  @Column(length = 120)
  private String email;

  @Column(length = 14)
  private String cpf;

  @Column(nullable = false, length = 20)
  private String tipo; // MECANICO | AUXILIAR

  @Column(name = "percentual_comissao", nullable = false)
  private Integer percentualComissao = 0; // 0..100

  @Column(nullable = false)
  private Boolean ativo = true;

  @Column(length = 500)
  private String observacao;

  @Column(name = "criado_em")
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  public Long getIdTecnico() { return idTecnico; }
  public void setIdTecnico(Long idTecnico) { this.idTecnico = idTecnico; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }

  public String getTelefone() { return telefone; }
  public void setTelefone(String telefone) { this.telefone = telefone; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getCpf() { return cpf; }
  public void setCpf(String cpf) { this.cpf = cpf; }

  public String getTipo() { return tipo; }
  public void setTipo(String tipo) { this.tipo = tipo; }

  public Integer getPercentualComissao() { return percentualComissao; }
  public void setPercentualComissao(Integer percentualComissao) { this.percentualComissao = percentualComissao; }

  public Boolean getAtivo() { return ativo; }
  public void setAtivo(Boolean ativo) { this.ativo = ativo; }

  public String getObservacao() { return observacao; }
  public void setObservacao(String observacao) { this.observacao = observacao; }

  public LocalDateTime getCriadoEm() { return criadoEm; }
  public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

  public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
  public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
