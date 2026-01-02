package br.com.torqueos.controller;

import br.com.torqueos.model.PecaOS;
import br.com.torqueos.service.PecaOSService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pecasOs")
public class PecaOSController {

  private final PecaOSService pecaOSService;

  public PecaOSController(PecaOSService pecaOSService) {
    this.pecaOSService = pecaOSService;
  }

  @GetMapping
  public String list(@RequestParam(value = "osId", required = false) Long osId, Model model) {
    if (osId != null) {
      model.addAttribute("pecas", pecaOSService.findByOs(osId));
      model.addAttribute("osId", osId);
    } else {
      model.addAttribute("pecas", pecaOSService.findAll());
    }
    return "peca/list";
  }

  @GetMapping("/novo")
  public String novo(@RequestParam(value = "osId", required = false) Long osId, Model model) {
    model.addAttribute("peca", new PecaOS());
    model.addAttribute("osId", osId);
    return "peca/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("peca") PecaOS peca,
                       @RequestParam(value = "osId", required = false) Long osId) {
    if (peca.getIdPecaOs() == null) pecaOSService.create(peca, osId);
    else pecaOSService.update(peca.getIdPecaOs(), peca, osId);

    if (osId != null) return "redirect:/ordens/ver/" + osId;
    return "redirect:/pecas";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    PecaOS p = pecaOSService.findById(id);
    model.addAttribute("peca", p);
    model.addAttribute("osId", p.getOrdemServico() != null ? p.getOrdemServico().getIdOs() : null);
    return "peca/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id,
                        @RequestParam(value = "osId", required = false) Long osId) {
    pecaOSService.delete(id);
    if (osId != null) return "redirect:/ordens/ver/" + osId;
    return "redirect:/pecas";
  }
}
