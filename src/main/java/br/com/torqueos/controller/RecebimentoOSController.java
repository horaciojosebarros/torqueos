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
  private static final DateTimeFormatter ISO_TIME = DateTimeFormatter.ofPattern("HH:mm");

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
    RecebimentoOS r = new RecebimentoOS();
    model.addAttribute("recebimento", r);
    model.addAttribute("ordens", ordemServicoService.findAll());

    model.addAttribute("osId", null);
    model.addAttribute("dataVencimentoIso", "");
    model.addAttribute("dataPagamentoIso", "");
    model.addAttribute("horaPagamento", "");
    model.addAttribute("osPago", false);

    return "recebimento/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("recebimento") RecebimentoOS recebimento,
                       @RequestParam(value = "osId", required = true) Long osId,
                       @RequestParam(value = "dataVencimentoIso", required = false) String dataVencimentoIso,
                       @RequestParam(value = "dataPagamentoIso", required = false) String dataPagamentoIso,
                       @RequestParam(value = "horaPagamento", required = false) String horaPagamento) {

    // ✅ NUNCA deixe o Spring tentar bindar LocalDate/LocalDateTime do form
    recebimento.setDataVencimento(parseDateOrNull(dataVencimentoIso));
    recebimento.setDataPagamento(parseDateTimeOrNull(dataPagamentoIso, horaPagamento));

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

    boolean osPago = (r.getDataPagamento() != null) || "PAGO".equalsIgnoreCase(r.getStatus());

    model.addAttribute("recebimento", r);
    model.addAttribute("ordens", ordemServicoService.findAll());
    model.addAttribute("osId", r.getOrdemServico() != null ? r.getOrdemServico().getIdOs() : null);

    model.addAttribute("dataVencimentoIso", r.getDataVencimento() != null ? r.getDataVencimento().format(ISO_DATE) : "");
    model.addAttribute("dataPagamentoIso", r.getDataPagamento() != null ? r.getDataPagamento().toLocalDate().format(ISO_DATE) : "");
    model.addAttribute("horaPagamento", r.getDataPagamento() != null ? r.getDataPagamento().toLocalTime().format(ISO_TIME) : "");

    model.addAttribute("osPago", osPago);
    return "recebimento/form";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    recebimentoService.delete(id);
    return "redirect:/recebimentos";
  }

  // ===================== helpers parse =====================

  private static LocalDate parseDateOrNull(String iso) {
    if (iso == null) return null;
    String s = iso.trim();
    if (s.isEmpty()) return null;
    return LocalDate.parse(s, ISO_DATE);
  }

  private static LocalDateTime parseDateTimeOrNull(String dateIso, String timeHm) {
    if (dateIso == null) return null;
    String d = dateIso.trim();
    if (d.isEmpty()) return null;

    LocalDate date = LocalDate.parse(d, ISO_DATE);

    LocalTime time = LocalTime.MIDNIGHT;
    if (timeHm != null && !timeHm.trim().isEmpty()) {
      time = LocalTime.parse(timeHm.trim(), ISO_TIME);
    }
    return LocalDateTime.of(date, time);
  }
}
