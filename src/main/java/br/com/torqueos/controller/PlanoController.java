package br.com.torqueos.controller;

import br.com.torqueos.model.Plano;
import br.com.torqueos.service.PlanoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/planos")
public class PlanoController {

  private final PlanoService planoService;

  public PlanoController(PlanoService planoService) {
    this.planoService = planoService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("planos", planoService.findAll());
    return "plano/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("plano", new Plano());
    return "plano/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("plano") Plano plano) {
    planoService.save(plano);
    return "redirect:/planos";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("plano", planoService.findById(id));
    return "plano/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    planoService.delete(id);
    return "redirect:/planos";
  }
}
