package br.com.torqueos.controller;

import br.com.torqueos.model.Assinatura;
import br.com.torqueos.service.AssinaturaService;
import br.com.torqueos.service.PlanoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/assinatura")
public class AssinaturaController {

  private final AssinaturaService assinaturaService;
  private final PlanoService planoService;

  public AssinaturaController(AssinaturaService assinaturaService, PlanoService planoService) {
    this.assinaturaService = assinaturaService;
    this.planoService = planoService;
  }

  @GetMapping
  public String view(Model model) {
    model.addAttribute("assinatura", assinaturaService.getDaEmpresaAtual());
    model.addAttribute("planos", planoService.findAll());
    return "assinatura/view";
  }

  @PostMapping("/alterar-plano")
  public String alterarPlano(@RequestParam("planoId") Long planoId) {
    assinaturaService.atualizarPlano(planoId);
    return "redirect:/assinatura";
  }
}
