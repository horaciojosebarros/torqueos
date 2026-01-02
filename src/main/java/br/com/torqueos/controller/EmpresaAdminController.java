package br.com.torqueos.controller;

import br.com.torqueos.repository.EmpresaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/empresas")
public class EmpresaAdminController {

  private final EmpresaRepository empresaRepository;

  public EmpresaAdminController(EmpresaRepository empresaRepository) {
    this.empresaRepository = empresaRepository;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("empresas", empresaRepository.findAll());
    return "admin/empresas";
  }

  @PostMapping("/usar")
  public String usar(@RequestParam("empresaId") Long empresaId, HttpSession session) {
    session.setAttribute("EMPRESA_ID", empresaId);
    return "redirect:/";
  }

  @GetMapping("/limpar")
  public String limpar(HttpSession session) {
    session.setAttribute("EMPRESA_ID", null);
    return "redirect:/admin/empresas";
  }
}
