package br.com.torqueos.controller;

import br.com.torqueos.model.Usuario;
import br.com.torqueos.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private final UsuarioService usuarioService;

	public AuthController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "auth/login";
	}

	@PostMapping("/login")
	public String doLogin(
	    @RequestParam(value="empresaId", required=false) Long empresaId,
	    @RequestParam("email") String email,
	    @RequestParam("senha") String senha,
	    HttpSession session,
	    Model model
	) {
	  try {
	    Usuario u;

	    if (empresaId == null || email == null || email.trim().isEmpty()) {
	      u = usuarioService.autenticarGlobal(email, senha);
	      if (u == null) throw new RuntimeException("Login inválido (SUPERADMIN)");
	      session.setAttribute("EMPRESA_ID", null);
	      session.setAttribute("ROLE", u.getRole());
	      session.setAttribute("USER_ID", u.getIdUsuario());
	      session.setAttribute("USER_NOME", u.getNome());
	      return "redirect:/admin/empresas";
	    }

	    u = usuarioService.autenticar(empresaId, email, senha);
	    if (u == null) throw new RuntimeException("Login inválido");
	    session.setAttribute("EMPRESA_ID", u.getIdEmpresa());
	    session.setAttribute("ROLE", u.getRole());
	    session.setAttribute("USER_ID", u.getIdUsuario());
	    session.setAttribute("USER_NOME", u.getNome());
	    return "redirect:/";

	  } catch (Exception e) {
	    model.addAttribute("erro", e.getMessage());
	    return "auth/login";
	  }
	}


	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/auth/login";
	}
}
