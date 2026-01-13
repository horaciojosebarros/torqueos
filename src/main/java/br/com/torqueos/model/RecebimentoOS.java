package br.com.torqueos.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recebimentos_os")
public class RecebimentoOS {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_recebimento")
  private Long idRecebimento;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_os", nullable = false)
  private OrdemServico ordemServico;

  @Column(name = "tipo_pagamento", nullable = false, length = 30)
  private String tipoPagamento = "PIX";

  @Column(nullable = false, length = 20)
  private String status = "PENDENTE";

  @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
  private BigDecimal valorTotal;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal desconto = BigDecimal.ZERO;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal juros = BigDecimal.ZERO;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal multa = BigDecimal.ZERO;

  @Column(nullable = false)
  private Integer parcelas = 1;

  @Column(name = "data_vencimento")
  private LocalDate dataVencimento;

  @Column(name = "data_pagamento")
  private LocalDateTime dataPagamento;

  @Column(length = 120)
  private String referencia;

  @Column(name = "documento_ref", length = 120)
  private String documentoRef;

  @Column(length = 500)
  private String observacao;

  @Column(name = "criado_em")
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  // ===== getters/setters =====

  public Long getIdRecebimento() { return idRecebimento; }
  public void setIdRecebimento(Long idRecebimento) { this.idRecebimento = idRecebimento; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public OrdemServico getOrdemServico() { return ordemServico; }
  public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

  public String getTipoPagamento() { return tipoPagamento; }
  public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public BigDecimal getValorTotal() { return valorTotal; }
  public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

  public BigDecimal getDesconto() { return desconto; }
  public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

  public BigDecimal getJuros() { return juros; }
  public void setJuros(BigDecimal juros) { this.juros = juros; }

  public BigDecimal getMulta() { return multa; }
  public void setMulta(BigDecimal multa) { this.multa = multa; }

  public Integer getParcelas() { return parcelas; }
  public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }

  public LocalDate getDataVencimento() { return dataVencimento; }
  public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

  public LocalDateTime getDataPagamento() { return dataPagamento; }
  public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

  public String getReferencia() { return referencia; }
  public void setReferencia(String referencia) { this.referencia = referencia; }

  public String getDocumentoRef() { return documentoRef; }
  public void setDocumentoRef(String documentoRef) { this.documentoRef = documentoRef; }

  public String getObservacao() { return observacao; }
  public void setObservacao(String observacao) { this.observacao = observacao; }

  public LocalDateTime getCriadoEm() { return criadoEm; }
  public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

  public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
  public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
