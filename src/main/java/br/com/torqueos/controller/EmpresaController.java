package br.com.torqueos.controller;

import br.com.torqueos.model.Empresa;
import br.com.torqueos.service.EmpresaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

  private final EmpresaService empresaService;

  public EmpresaController(EmpresaService empresaService) {
    this.empresaService = empresaService;
  }

  @GetMapping
  public String form(Model model) {
    Empresa emp = empresaService.getEmpresaAtual();
    model.addAttribute("empresa", emp);
    return "empresa/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("empresa") Empresa form, RedirectAttributes ra) {
    empresaService.atualizarEmpresaAtual(form);
    ra.addFlashAttribute("msg", "Dados da empresa atualizados com sucesso.");
    return "redirect:/empresa";
  }
}
