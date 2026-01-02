package br.com.torqueos.model;

import javax.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_cliente")
  private Long idCliente;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @Column(nullable = false, length = 100)
  private String nome;

  @Column(length = 20)
  private String telefone;

  @Column(length = 100)
  private String email;

  @Column(length = 255)
  private String endereco;

  public Long getIdCliente() { return idCliente; }
  public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }

  public String getTelefone() { return telefone; }
  public void setTelefone(String telefone) { this.telefone = telefone; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getEndereco() { return endereco; }
  public void setEndereco(String endereco) { this.endereco = endereco; }
}
