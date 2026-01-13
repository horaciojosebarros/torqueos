package br.com.torqueos.controller;

import br.com.torqueos.model.RecebimentoOS;
import br.com.torqueos.service.OrdemServicoService;
import br.com.torqueos.service.RecebimentoOSService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/recebimentos")
public class RecebimentoOSController {

  private final RecebimentoOSService recebimentoService;
  private final OrdemServicoService ordemServicoService;

  private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
  private static final DateTimeFormatter ISO_TIME = DateTimeFormatter.ofPattern("HH:mm"); // HH:mm

  public RecebimentoOSController(RecebimentoOSService recebimentoService,
                                 OrdemServicoService ordemServicoService) {
    this.recebimentoService = recebimentoService;
    this.ordemServicoService = ordemServicoService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("recebimentos", recebimentoService.listarDaEmpresa());
    return "recebimento/list";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("recebimento", new RecebimentoOS());
    model.addAttribute("ordens", ordemServicoService.findAll());

    // inputs do form
    model.addAttribute("dataVencimentoIso", "");
    model.addAttribute("dataPagamentoIso", "");
    model.addAttribute("horaPagamento", "");

    return "recebimento/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("recebimento") RecebimentoOS recebimento,
                       @RequestParam(value = "osId", required = true) Long osId,
                       @RequestParam(value = "dataVencimento", required = false) String dataVencimentoIso,
                       @RequestParam(value = "dataPagamento", required = false) String dataPagamentoIso,
                       @RequestParam(value = "horaPagamento", required = false) String horaPagamento) {

    // yyyy-MM-dd -> LocalDate
    if (dataVencimentoIso != null && !dataVencimentoIso.trim().isEmpty()) {
      recebimento.setDataVencimento(LocalDate.parse(dataVencimentoIso.trim(), ISO_DATE));
    } else {
      recebimento.setDataVencimento(null);
    }

    // yyyy-MM-dd + HH:mm -> LocalDateTime (opcional)
    LocalDateTime dtPg = null;
    if (dataPagamentoIso != null && !dataPagamentoIso.trim().isEmpty()) {
      LocalDate d = LocalDate.parse(dataPagamentoIso.trim(), ISO_DATE);

      LocalTime t = LocalTime.of(0, 0);
      if (horaPagamento != null && !horaPagamento.trim().isEmpty()) {
        t = LocalTime.parse(horaPagamento.trim(), ISO_TIME);
      }
      dtPg = LocalDateTime.of(d, t);
    }
    recebimento.setDataPagamento(dtPg);

    // Se marcou PAGO e não informou data/hora, seta agora (evita inconsistência)
    if ("PAGO".equalsIgnoreCase(recebimento.getStatus()) && recebimento.getDataPagamento() == null) {
      recebimento.setDataPagamento(LocalDateTime.now());
    }

    if (recebimento.getIdRecebimento() == null) {
      recebimentoService.create(recebimento, osId);
    } else {
      recebimentoService.update(recebimento.getIdRecebimento(), recebimento, osId);
    }
    return "redirect:/recebimentos";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    RecebimentoOS r = recebimentoService.findById(id);
    model.addAttribute("recebimento", r);
    model.addAttribute("ordens", ordemServicoService.findAll());
    model.addAttribute("osId", r.getOrdemServico() != null ? r.getOrdemServico().getIdOs() : null);

    // Vencimento (date)
    model.addAttribute("dataVencimentoIso",
        r.getDataVencimento() != null ? r.getDataVencimento().format(ISO_DATE) : "");

    // Pagamento (date + time)
    if (r.getDataPagamento() != null) {
      model.addAttribute("dataPagamentoIso", r.getDataPagamento().toLocalDate().format(ISO_DATE));
      model.addAttribute("horaPagamento", r.getDataPagamento().toLocalTime().format(ISO_TIME));
    } else {
      model.addAttribute("dataPagamentoIso", "");
      model.addAttribute("horaPagamento", "");
    }

    return "recebimento/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    recebimentoService.delete(id);
    return "redirect:/recebimentos";
  }
}
