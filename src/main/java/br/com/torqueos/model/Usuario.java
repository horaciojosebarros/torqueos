package br.com.torqueos.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_usuario")
  private Long idUsuario;

  @Column(name = "id_empresa")
  private Long idEmpresa; // NULL para SUPERADMIN

  @Column(nullable = false, length = 120)
  private String nome;

  @Column(nullable = false, length = 160)
  private String email;

  @Column(name = "senha_hash", nullable = false, length = 128)
  private String senhaHash;

  @Column(nullable = false, length = 20)
  private String role; // ADMIN, OPERADOR

  @Column(nullable = false)
  private Boolean ativo = true;

  @Column(name = "criado_em")
  private LocalDateTime criadoEm;

  public Long getIdUsuario() { return idUsuario; }
  public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getSenhaHash() { return senhaHash; }
  public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }

  public Boolean getAtivo() { return ativo; }
  public void setAtivo(Boolean ativo) { this.ativo = ativo; }

  public LocalDateTime getCriadoEm() { return criadoEm; }
  public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
