package br.com.torqueos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import br.com.torqueos.model.Servico;
import br.com.torqueos.service.ServicoService;

@Controller
@RequestMapping("/servicos")
public class ServicoController {

  private final ServicoService service;

  public ServicoController(ServicoService service) {
    this.service = service;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("lista", service.list());
    return "servico/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("servico", new Servico());
    return "servico/form";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("servico", service.find(id));
    return "servico/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("servico") Servico servico) {
    if (servico.getIdServico() == null) service.create(servico);
    else service.update(servico.getIdServico(), servico);
    return "redirect:/servicos";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    service.delete(id);
    return "redirect:/servicos";
  }
}
