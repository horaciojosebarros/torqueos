package br.com.torqueos.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordens_servico")
public class OrdemServico {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_os")
  private Long idOs;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_cliente")
  private Cliente cliente;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_veiculo")
  private Veiculo veiculo;

  @Column(name = "data_abertura")
  private LocalDateTime dataAbertura;

  @Column(name = "data_finalizacao")
  private LocalDateTime dataFinalizacao;

  @Column(length = 20)
  private String status;

  @Column(length = 2000)
  private String observacoes;

  public Long getIdOs() { return idOs; }
  public void setIdOs(Long idOs) { this.idOs = idOs; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public Cliente getCliente() { return cliente; }
  public void setCliente(Cliente cliente) { this.cliente = cliente; }

  public Veiculo getVeiculo() { return veiculo; }
  public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

  public LocalDateTime getDataAbertura() { return dataAbertura; }
  public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

  public LocalDateTime getDataFinalizacao() { return dataFinalizacao; }
  public void setDataFinalizacao(LocalDateTime dataFinalizacao) { this.dataFinalizacao = dataFinalizacao; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getObservacoes() { return observacoes; }
  public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
  
  

  @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrdemServicoTecnico> tecnicos = new ArrayList<>();

  public List<OrdemServicoTecnico> getTecnicos() { return tecnicos; }
  public void setTecnicos(List<OrdemServicoTecnico> tecnicos) { this.tecnicos = tecnicos; }

}
