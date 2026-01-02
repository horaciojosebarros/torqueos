package br.com.torqueos.controller;

import br.com.torqueos.model.ServicoOS;
import br.com.torqueos.service.ServicoOSService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/servicosOs")
public class ServicoOSController {

  private final ServicoOSService servicoOSService;

  public ServicoOSController(ServicoOSService servicoOSService) {
    this.servicoOSService = servicoOSService;
  }

  @GetMapping
  public String list(@RequestParam(value = "osId", required = false) Long osId, Model model) {
    if (osId != null) {
      model.addAttribute("servicos", servicoOSService.findByOs(osId));
      model.addAttribute("osId", osId);
    } else {
      model.addAttribute("servicos", servicoOSService.findAll());
    }
    return "servico/list";
  }

  @GetMapping("/novo")
  public String novo(@RequestParam(value = "osId", required = false) Long osId, Model model) {
    model.addAttribute("servico", new ServicoOS());
    model.addAttribute("osId", osId);
    return "servico/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("servico") ServicoOS servico,
                       @RequestParam(value = "osId", required = false) Long osId) {
    if (servico.getIdServicoOs() == null) servicoOSService.create(servico, osId);
    else servicoOSService.update(servico.getIdServicoOs(), servico, osId);

    if (osId != null) return "redirect:/ordens/ver/" + osId;
    return "redirect:/servicos";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    ServicoOS s = servicoOSService.findById(id);
    model.addAttribute("servico", s);
    model.addAttribute("osId", s.getOrdemServico() != null ? s.getOrdemServico().getIdOs() : null);
    return "servico/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id,
                        @RequestParam(value = "osId", required = false) Long osId) {
    servicoOSService.delete(id);
    if (osId != null) return "redirect:/ordens/ver/" + osId;
    return "redirect:/servicos";
  }
}
