package br.com.torqueos.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_pagamento")
  private Long idPagamento;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_assinatura", nullable = false)
  private Assinatura assinatura;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal valor;

  @Column(name = "data_pagamento")
  private LocalDateTime dataPagamento;

  @Column(nullable = false, length = 20)
  private String status;

  @Column(length = 120)
  private String referencia;

  public Long getIdPagamento() { return idPagamento; }
  public void setIdPagamento(Long idPagamento) { this.idPagamento = idPagamento; }

  public Assinatura getAssinatura() { return assinatura; }
  public void setAssinatura(Assinatura assinatura) { this.assinatura = assinatura; }

  public BigDecimal getValor() { return valor; }
  public void setValor(BigDecimal valor) { this.valor = valor; }

  public LocalDateTime getDataPagamento() { return dataPagamento; }
  public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getReferencia() { return referencia; }
  public void setReferencia(String referencia) { this.referencia = referencia; }
}
