package br.com.torqueos.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pecas_os")
public class PecaOS {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_peca_os")
  private Long idPecaOs;

  @Column(name = "id_empresa", nullable = false)
  private Long idEmpresa;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_os")
  private OrdemServico ordemServico;

  @Column(nullable = false, length = 2000)
  private String descricao;

  @Column(nullable = false)
  private Integer quantidade;

  @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
  private BigDecimal valorUnitario;
  
  @javax.persistence.Column(name = "id_peca_catalogo")
  private Long idPecaCatalogo;

  public Long getIdPecaCatalogo() {
    return idPecaCatalogo;
  }

  public void setIdPecaCatalogo(Long idPecaCatalogo) {
    this.idPecaCatalogo = idPecaCatalogo;
  }

  public Long getIdPecaOs() { return idPecaOs; }
  public void setIdPecaOs(Long idPecaOs) { this.idPecaOs = idPecaOs; }

  public Long getIdEmpresa() { return idEmpresa; }
  public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }

  public OrdemServico getOrdemServico() { return ordemServico; }
  public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }

  public String getDescricao() { return descricao; }
  public void setDescricao(String descricao) { this.descricao = descricao; }

  public Integer getQuantidade() { return quantidade; }
  public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

  public BigDecimal getValorUnitario() { return valorUnitario; }
  public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
}
