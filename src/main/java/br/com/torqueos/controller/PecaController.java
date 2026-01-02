package br.com.torqueos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import br.com.torqueos.model.Peca;
import br.com.torqueos.service.PecaService;

@Controller
@RequestMapping("/pecas")
public class PecaController {

  private final PecaService service;

  public PecaController(PecaService service) {
    this.service = service;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("lista", service.list());
    return "peca/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("peca", new Peca());
    return "peca/form";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("peca", service.find(id));
    return "peca/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("peca") Peca peca) {
    if (peca.getIdPeca() == null) service.create(peca);
    else service.update(peca.getIdPeca(), peca);
    return "redirect:/pecas";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    service.delete(id);
    return "redirect:/pecas";
  }
}
