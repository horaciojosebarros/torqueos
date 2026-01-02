package br.com.torqueos.controller;

import br.com.torqueos.model.Cliente;
import br.com.torqueos.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

  private final ClienteService clienteService;

  public ClienteController(ClienteService clienteService) {
    this.clienteService = clienteService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("clientes", clienteService.findAll());
    return "cliente/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("cliente", new Cliente());
    return "cliente/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("cliente") Cliente cliente) {
    if (cliente.getIdCliente() == null) clienteService.create(cliente);
    else clienteService.update(cliente.getIdCliente(), cliente);
    return "redirect:/clientes";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("cliente", clienteService.findById(id));
    return "cliente/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    clienteService.delete(id);
    return "redirect:/clientes";
  }
}
