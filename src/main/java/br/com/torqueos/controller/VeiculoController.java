package br.com.torqueos.controller;

import br.com.torqueos.model.Veiculo;
import br.com.torqueos.service.ClienteService;
import br.com.torqueos.service.VeiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

  private final VeiculoService veiculoService;
  private final ClienteService clienteService;

  public VeiculoController(VeiculoService veiculoService, ClienteService clienteService) {
    this.veiculoService = veiculoService;
    this.clienteService = clienteService;
  }

  @GetMapping
  public String list(@RequestParam(value = "clienteId", required = false) Long clienteId, Model model) {
    if (clienteId != null) {
      model.addAttribute("veiculos", veiculoService.findByCliente(clienteId));
      model.addAttribute("clienteId", clienteId);
    } else {
      model.addAttribute("veiculos", veiculoService.findAll());
    }
    return "veiculo/list";
  }

  @GetMapping("/novo")
  public String novo(@RequestParam(value = "clienteId", required = false) Long clienteId, Model model) {
    model.addAttribute("veiculo", new Veiculo());
    model.addAttribute("clientes", clienteService.findAll());
    model.addAttribute("clienteId", clienteId);
    return "veiculo/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("veiculo") Veiculo veiculo,
                       @RequestParam(value = "clienteId", required = false) Long clienteId) {
    if (veiculo.getIdVeiculo() == null) veiculoService.create(veiculo, clienteId);
    else veiculoService.update(veiculo.getIdVeiculo(), veiculo, clienteId);
    return "redirect:/veiculos";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("veiculo", veiculoService.findById(id));
    model.addAttribute("clientes", clienteService.findAll());
    return "veiculo/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    veiculoService.delete(id);
    return "redirect:/veiculos";
  }
}
