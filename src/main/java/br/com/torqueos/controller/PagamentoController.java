package br.com.torqueos.controller;

import br.com.torqueos.model.Pagamento;
import br.com.torqueos.service.PagamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {

  private final PagamentoService pagamentoService;

  public PagamentoController(PagamentoService pagamentoService) {
    this.pagamentoService = pagamentoService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("pagamentos", pagamentoService.listarDaEmpresa());
    return "pagamento/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("pagamento", new Pagamento());
    return "pagamento/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("pagamento") Pagamento pagamento) {
    if (pagamento.getIdPagamento() == null) pagamentoService.criar(pagamento);
    else pagamentoService.update(pagamento.getIdPagamento(), pagamento);
    return "redirect:/pagamentos";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("pagamento", pagamentoService.findById(id));
    return "pagamento/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    pagamentoService.delete(id);
    return "redirect:/pagamentos";
  }
}
