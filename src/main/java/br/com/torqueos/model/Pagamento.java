package br.com.torqueos.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_pagamento")
  private Long idPagamento;

  // multiempresa
  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_assinatura", nullable = false)
  private Assinatura assinatura;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_os")
  private OrdemServico ordemServico;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal valor = BigDecimal.ZERO;

  // contas a receber: vencimento / pagamento
  @Column(name="data_vencimento")
  private LocalDate dataVencimento;

  @Column(name = "data_pagamento")
  private LocalDate dataPagamento;

  @Column(nullable = false, length = 20)
  private String status = "PENDENTE"; // PENDENTE | PAGO | CANCELADO

  @Column(name="tipo_pagamento", nullable=false, length=30)
  private String tipoPagamento = "PIX"; // PIX | DINHEIRO | CARTAO_CREDITO | ...

  @Column(length = 120)
  private String referencia;

  @Column(name="desconto", precision=12, scale=2, nullable=false)
  private BigDecimal desconto = BigDecimal.ZERO;

  @Column(name="juros", precision=12, scale=2, nullable=false)
  private BigDecimal juros = BigDecimal.ZERO;

  @Column(name="multa", precision=12, scale=2, nullable=false)
  private BigDecimal multa = BigDecimal.ZERO;

  @Column(name="parcelas", nullable=false)
  private Integer parcelas = 1;

  @Column(name="observacao", length=500)
  private String observacao;

  @Column(name="documento_ref", length=120)
  private String documentoRef;

  @Column(name="criado_em")
  private LocalDateTime criadoEm;

  @Column(name="atualizado_em")
  private LocalDateTime atualizadoEm;

  public Long getIdPagamento() { return idPagamento; }
  public void setIdPagamento(Long idPagamento) { this.idPagamento = idPagamento; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public Assinatura getAssinatura() { return assinatura; }
  public void setAssinatura(Assinatura assinatura) { this.assinatura = assinatura; }

  public OrdemServico getOrdemServico() { return ordemServico; }
  public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

  public BigDecimal getValor() { return valor; }
  public void setValor(BigDecimal valor) { this.valor = valor; }

  public LocalDate getDataVencimento() { return dataVencimento; }
  public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

  public LocalDate getDataPagamento() { return dataPagamento; }
  public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getTipoPagamento() { return tipoPagamento; }
  public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }

  public String getReferencia() { return referencia; }
  public void setReferencia(String referencia) { this.referencia = referencia; }

  public BigDecimal getDesconto() { return desconto; }
  public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

  public BigDecimal getJuros() { return juros; }
  public void setJuros(BigDecimal juros) { this.juros = juros; }

  public BigDecimal getMulta() { return multa; }
  public void setMulta(BigDecimal multa) { this.multa = multa; }

  public Integer getParcelas() { return parcelas; }
  public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }

  public String getObservacao() { return observacao; }
  public void setObservacao(String observacao) { this.observacao = observacao; }

  public String getDocumentoRef() { return documentoRef; }
  public void setDocumentoRef(String documentoRef) { this.documentoRef = documentoRef; }

  public LocalDateTime getCriadoEm() { return criadoEm; }
  public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

  public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
  public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
