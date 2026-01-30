package br.com.torqueos.controller;

import br.com.torqueos.model.Tecnico;
import br.com.torqueos.service.TecnicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tecnicos")
public class TecnicoController {

  private final TecnicoService tecnicoService;

  public TecnicoController(TecnicoService tecnicoService) {
    this.tecnicoService = tecnicoService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("tecnicos", tecnicoService.listarDaEmpresa());
    return "tecnico/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    Tecnico t = new Tecnico();
    t.setTipo("MECANICO");
    t.setPercentualComissao(0);
    t.setAtivo(true);
    model.addAttribute("tecnico", t);
    return "tecnico/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("tecnico") Tecnico tecnico) {
    if (tecnico.getIdTecnico() == null) {
      tecnicoService.create(tecnico);
    } else {
      tecnicoService.update(tecnico.getIdTecnico(), tecnico);
    }
    return "redirect:/tecnicos";
  }

  @GetMapping("/ver/{id}")
  public String ver(@PathVariable Long id, Model model) {
    model.addAttribute("tecnico", tecnicoService.findById(id));
    return "tecnico/view";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("tecnico", tecnicoService.findById(id));
    return "tecnico/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    tecnicoService.delete(id);
    return "redirect:/tecnicos";
  }
}
