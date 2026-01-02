package br.com.torqueos.controller;

import br.com.torqueos.model.Usuario;
import br.com.torqueos.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

  private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("usuarios", usuarioService.listar());
    return "usuario/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "usuario/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("usuario") Usuario usuario,
                       @RequestParam(value = "senha", required = false) String senha) {
    if (usuario.getIdUsuario() == null) usuarioService.criar(usuario, senha == null ? "" : senha);
    else usuarioService.update(usuario.getIdUsuario(), usuario, senha);
    return "redirect:/usuarios";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("usuario", usuarioService.findById(id));
    return "usuario/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    usuarioService.delete(id);
    return "redirect:/usuarios";
  }
}
