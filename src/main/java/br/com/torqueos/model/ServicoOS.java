package br.com.torqueos.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicos_os")
public class ServicoOS {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_servico_os")
	private Long idServicoOs;

	@Column(name = "id_empresa", nullable = false)
	private Long idEmpresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_os")
	private OrdemServico ordemServico;

	@Column(nullable = false, length = 2000)
	private String descricao;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal valor;

//referência ao catálogo (servicos)
	@javax.persistence.Column(name = "id_servico_catalogo")
	private Long idServicoCatalogo;

	public Long getIdServicoCatalogo() {
		return idServicoCatalogo;
	}

	public void setIdServicoCatalogo(Long idServicoCatalogo) {
		this.idServicoCatalogo = idServicoCatalogo;
	}

	public Long getIdServicoOs() {
		return idServicoOs;
	}

	public void setIdServicoOs(Long idServicoOs) {
		this.idServicoOs = idServicoOs;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public OrdemServico getOrdemServico() {
		return ordemServico;
	}

	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
