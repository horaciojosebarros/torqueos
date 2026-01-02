package br.com.torqueos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

  @GetMapping("/")
  public String home(HttpSession session) {
    Object userId = session.getAttribute("USER_ID");
    if (userId == null) {
      return "redirect:/landing/index.html";
    }
    return "dashboard"; // rota principal do app (ajuste se necessário)
  }
}
