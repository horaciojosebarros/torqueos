package br.com.torqueos.controller;

import br.com.torqueos.model.Agendamento;
import br.com.torqueos.service.AgendamentoService;
import br.com.torqueos.service.UsuarioService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

  private final AgendamentoService agendamentoService;
  private final UsuarioService usuarioService;

  public AgendamentoController(AgendamentoService agendamentoService, UsuarioService usuarioService) {
    this.agendamentoService = agendamentoService;
    this.usuarioService = usuarioService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("agendamentos", agendamentoService.findAll());
    return "agendamento/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("agendamento", new Agendamento());
    model.addAttribute("usuarios", usuarioService.findall()); 
    return "agendamento/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("agendamento") Agendamento agendamento,
                       @RequestParam("usuarioId") Long usuarioId) {
    if (agendamento.getIdAgendamento() == null) {
      agendamentoService.create(agendamento, usuarioId);
    } else {
      agendamentoService.update(agendamento.getIdAgendamento(), agendamento, usuarioId);
    }
    return "redirect:/agendamentos";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    Agendamento a = agendamentoService.findById(id);
    model.addAttribute("agendamento", a);
    model.addAttribute("usuarios", usuarioService.findById(a.getUsuarioResponsavel().getIdUsuario()));
    return "agendamento/form";
  }

  @GetMapping("/ver/{id}")
  public String ver(@PathVariable Long id, Model model) {
    model.addAttribute("agendamento", agendamentoService.findById(id));
    return "agendamento/view";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    agendamentoService.delete(id);
    return "redirect:/agendamentos";
  }
}
