package br.com.torqueos.model;

import javax.persistence.*;

@Entity
@Table(name = "veiculos")
public class Veiculo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_veiculo")
  private Long idVeiculo;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_cliente")
  private Cliente cliente;

  @Column(nullable = false, length = 10)
  private String placa;

  @Column(length = 50)
  private String marca;

  @Column(length = 50)
  private String modelo;

  private Integer ano;

  @Column(length = 50)
  private String chassi;

  public Long getIdVeiculo() { return idVeiculo; }
  public void setIdVeiculo(Long idVeiculo) { this.idVeiculo = idVeiculo; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public Cliente getCliente() { return cliente; }
  public void setCliente(Cliente cliente) { this.cliente = cliente; }

  public String getPlaca() { return placa; }
  public void setPlaca(String placa) { this.placa = placa; }

  public String getMarca() { return marca; }
  public void setMarca(String marca) { this.marca = marca; }

  public String getModelo() { return modelo; }
  public void setModelo(String modelo) { this.modelo = modelo; }

  public Integer getAno() { return ano; }
  public void setAno(Integer ano) { this.ano = ano; }

  public String getChassi() { return chassi; }
  public void setChassi(String chassi) { this.chassi = chassi; }
}
