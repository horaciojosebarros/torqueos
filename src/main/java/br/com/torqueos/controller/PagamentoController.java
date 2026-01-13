package br.com.torqueos.controller;

import br.com.torqueos.model.Pagamento;
import br.com.torqueos.service.OrdemServicoService;
import br.com.torqueos.service.PagamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {

  private final PagamentoService pagamentoService;
  private final OrdemServicoService ordemServicoService;

  public PagamentoController(PagamentoService pagamentoService, OrdemServicoService ordemServicoService) {
    this.pagamentoService = pagamentoService;
    this.ordemServicoService = ordemServicoService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("pagamentos", pagamentoService.listarDaEmpresa());
    return "pagamento/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("pagamento", new Pagamento());
    model.addAttribute("ordens", ordemServicoService.findAll());
    return "pagamento/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("pagamento") Pagamento pagamento,
                       @RequestParam(value = "osId", required = false) Long osId) {
    if (pagamento.getIdPagamento() == null) {
      pagamentoService.create(pagamento, osId);
    } else {
      pagamentoService.update(pagamento.getIdPagamento(), pagamento, osId);
    }
    return "redirect:/pagamentos";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    Pagamento p = pagamentoService.findById(id);
    model.addAttribute("pagamento", p);
    model.addAttribute("ordens", ordemServicoService.findAll());

    // pré-selecionar a OS no combo
    if (p.getOrdemServico() != null) {
      model.addAttribute("osId", p.getOrdemServico().getIdOs());
    } else {
      model.addAttribute("osId", null);
    }
    return "pagamento/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    pagamentoService.delete(id);
    return "redirect:/pagamentos";
  }
}
