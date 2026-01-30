package br.com.torqueos.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "empresas")
public class Empresa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_empresa")
  private Long idEmpresa;

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(length = 20)
  private String cnpj;

  // NOVOS CAMPOS
  @Column(length = 20)
  private String telefone;

  @Column(length = 200)
  private String endereco;

  @Column(length = 120)
  private String bairro;

  @Column(length = 120)
  private String cidade;

  @Column(length = 2)
  private String uf;

  @Column(nullable = false, length = 20)
  private String status;

  @Column(name = "criado_em")
  private LocalDateTime criadoEm;

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }

  public String getCnpj() { return cnpj; }
  public void setCnpj(String cnpj) { this.cnpj = cnpj; }

  public String getTelefone() { return telefone; }
  public void setTelefone(String telefone) { this.telefone = telefone; }

  public String getEndereco() { return endereco; }
  public void setEndereco(String endereco) { this.endereco = endereco; }

  public String getBairro() { return bairro; }
  public void setBairro(String bairro) { this.bairro = bairro; }

  public String getCidade() { return cidade; }
  public void setCidade(String cidade) { this.cidade = cidade; }

  public String getUf() { return uf; }
  public void setUf(String uf) { this.uf = uf; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public LocalDateTime getCriadoEm() { return criadoEm; }
  public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
