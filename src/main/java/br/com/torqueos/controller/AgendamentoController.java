package br.com.torqueos.controller;

import br.com.torqueos.model.Agendamento;
import br.com.torqueos.service.AgendamentoService;
import br.com.torqueos.service.UsuarioService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

  private final AgendamentoService agendamentoService;
  private final UsuarioService usuarioService;

  // FORM: dd/MM/yyyy e HH:mm
  private static final DateTimeFormatter DF_FORM = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  private static final DateTimeFormatter TF_FORM = DateTimeFormatter.ofPattern("HH:mm");

  // LISTA: dd/MM/yyyy HH:mm
  private static final DateTimeFormatter DTF_LIST = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
  
  private static final DateTimeFormatter DF = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd


  public AgendamentoController(AgendamentoService agendamentoService, UsuarioService usuarioService) {
    this.agendamentoService = agendamentoService;
    this.usuarioService = usuarioService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("agendamentos",
        agendamentoService.findFrom(java.time.LocalDate.now().atStartOfDay()));
    return "agendamento/list";
  }


  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("agendamento", new Agendamento());
    model.addAttribute("usuarios", usuarioService.findall());

    // deixa em branco (o form usa dd/MM/yyyy)
    model.addAttribute("dataIso", "");
    model.addAttribute("hora", "");

    model.addAttribute("clienteNome", "");
    model.addAttribute("marca", "");
    model.addAttribute("modelo", "");
    model.addAttribute("placa", "");
    return "agendamento/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("agendamento") Agendamento agendamento,
                       @RequestParam(value = "usuarioId", required = false) Long usuarioId,
                       @RequestParam("data") String data,
                       @RequestParam("hora") String hora,
                       @RequestParam(value = "clienteNome", required = false) String clienteNome,
                       @RequestParam(value = "marca", required = false) String marca,
                       @RequestParam(value = "modelo", required = false) String modelo,
                       @RequestParam(value = "placa", required = false) String placa) {

    // data do form é dd/MM/yyyy
	  LocalDate d = LocalDate.parse(data, DF);
    LocalTime h = LocalTime.parse(hora, TF_FORM);
    agendamento.setInicio(LocalDateTime.of(d, h));

    String info = montarInfo(clienteNome, marca, modelo, placa);
    String desc = agendamento.getDescricao() == null ? "" : agendamento.getDescricao().trim();
    agendamento.setDescricao((info + (desc.isEmpty() ? "" : "\n" + desc)).trim());

    if (agendamento.getIdAgendamento() == null) {
      agendamentoService.create(agendamento, usuarioId);
    } else {
      agendamentoService.update(agendamento.getIdAgendamento(), agendamento, usuarioId);
    }
    return "redirect:/agendamentos";
  }

  private String montarInfo(String clienteNome, String marca, String modelo, String placa) {
    StringBuilder sb = new StringBuilder();
    if (clienteNome != null && !clienteNome.trim().isEmpty()) sb.append("Cliente: ").append(clienteNome.trim()).append("\n");
    if (placa != null && !placa.trim().isEmpty()) sb.append("Placa: ").append(placa.trim()).append("\n");
    if (marca != null && !marca.trim().isEmpty()) sb.append("Marca: ").append(marca.trim()).append("\n");
    if (modelo != null && !modelo.trim().isEmpty()) sb.append("Modelo: ").append(modelo.trim()).append("\n");
    return sb.toString().trim();
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    Agendamento a = agendamentoService.findById(id);
    model.addAttribute("agendamento", a);
    model.addAttribute("usuarios", usuarioService.findall());

    if (a.getInicio() != null) {
    	model.addAttribute("dataIso", a.getInicio().toLocalDate().toString()); // yyyy-MM-dd
    	model.addAttribute("hora", a.getInicio().toLocalTime().toString().substring(0,5)); // HH:mm

    } else {
      model.addAttribute("data", "");
      model.addAttribute("hora", "");
    }

    // por enquanto ficam em branco (pois você embute na descrição)
    model.addAttribute("clienteNome", "");
    model.addAttribute("marca", "");
    model.addAttribute("modelo", "");
    model.addAttribute("placa", "");

    return "agendamento/form";
  }

  @GetMapping("/ver/{id}")
  public String ver(@PathVariable Long id, Model model) {
    model.addAttribute("agendamento", agendamentoService.findById(id));
    return "agendamento/view";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    agendamentoService.delete(id);
    return "redirect:/agendamentos";
  }
}
