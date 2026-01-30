package br.com.torqueos.controller;

import br.com.torqueos.model.OrdemServico;
import br.com.torqueos.model.RecebimentoOS;
import br.com.torqueos.service.*;
import br.com.torqueos.util.PdfOrdemServicoGenerator;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/ordens")
public class OrdemServicoController {

  private final OrdemServicoService ordemServicoService;
  private final ClienteService clienteService;
  private final VeiculoService veiculoService;
  private final ServicoOSService servicoOSService;
  private final PecaOSService pecaOSService;
  private final PecaService pecaService;
  private final ServicoService servicoService;
  private final RecebimentoOSService recebimentoOSService;

  // ✅ técnicos
  private final TecnicoService tecnicoService;
  private final OrdemServicoTecnicoService osTecnicoService;

  public OrdemServicoController(OrdemServicoService ordemServicoService,
                                ClienteService clienteService,
                                VeiculoService veiculoService,
                                ServicoOSService servicoOSService,
                                PecaOSService pecaOSService,
                                PecaService pecaService,
                                ServicoService servicoService,
                                RecebimentoOSService recebimentoOSService,
                                TecnicoService tecnicoService,
                                OrdemServicoTecnicoService osTecnicoService) {
    this.ordemServicoService = ordemServicoService;
    this.clienteService = clienteService;
    this.veiculoService = veiculoService;
    this.servicoOSService = servicoOSService;
    this.pecaOSService = pecaOSService;
    this.pecaService = pecaService;
    this.servicoService = servicoService;
    this.recebimentoOSService = recebimentoOSService;

    this.tecnicoService = tecnicoService;
    this.osTecnicoService = osTecnicoService;
  }

  @GetMapping
  public String list(Model model) {
    List<OrdemServico> ordens = ordemServicoService.findAll();

    // Descobre quais OS estão pagas
    Set<Long> osPagas = new HashSet<>();
    List<RecebimentoOS> recebimentosPagos = recebimentoOSService.listarPagosDaEmpresa();
    if (recebimentosPagos != null) {
      for (RecebimentoOS r : recebimentosPagos) {
        if (r.getOrdemServico() != null && r.getOrdemServico().getIdOs() != null) {
          osPagas.add(r.getOrdemServico().getIdOs());
        }
      }
    }

    model.addAttribute("ordens", ordens);
    model.addAttribute("osPagas", osPagas);
    return "ordem/list";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("ordem") OrdemServico ordem,
                       @RequestParam(value = "clienteId", required = false) Long clienteId,
                       @RequestParam(value = "veiculoId", required = false) Long veiculoId,
                       @RequestParam(value = "servicoId", required = false) List<Long> servicoId,
                       @RequestParam(value = "pecaId", required = false) List<Long> pecaId,
                       @RequestParam(value = "pecaQuantidade", required = false) List<Integer> pecaQuantidade,

                       // ===== NOVO: técnicos =====
                       @RequestParam(value = "tecnicoId", required = false) List<Long> tecnicoId,
                       @RequestParam(value = "tecnicoPapel", required = false) List<String> tecnicoPapel,
                       @RequestParam(value = "tecnicoPercentual", required = false) List<Integer> tecnicoPercentual) {

    if (ordem.getIdOs() == null) {
      ordemServicoService.create(ordem, clienteId, veiculoId, servicoId, pecaId, pecaQuantidade,
          tecnicoId, tecnicoPapel, tecnicoPercentual);
    } else {
      ordemServicoService.update(ordem.getIdOs(), ordem, clienteId, veiculoId, servicoId, pecaId, pecaQuantidade,
          tecnicoId, tecnicoPapel, tecnicoPercentual);
    }

    return "redirect:/ordens";
  }

  @GetMapping("/ver/{id}")
  public String ver(@PathVariable Long id, Model model) {
    OrdemServico os = ordemServicoService.findById(id);
    model.addAttribute("ordem", os);

    // view.jsp usa "servicos" e "pecas"
    model.addAttribute("servicos", servicoOSService.findByOs(id));
    model.addAttribute("pecas", pecaOSService.findByOs(id));

    return "ordem/view";
  }

  @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Long id) {
    ordemServicoService.delete(id);
    return "redirect:/ordens";
  }

  @GetMapping("/pdf/{id}")
  public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
    OrdemServico os = ordemServicoService.findById(id);

    List<br.com.torqueos.model.ServicoOS> servicos = servicoOSService.findByOs(id);
    List<br.com.torqueos.model.PecaOS> pecas = pecaOSService.findByOs(id);

    // --------- Cabeçalho (empresa + assinatura) ----------
    String empresaNome = "";
    String empresaCnpj = "";

    try {
      // fallback se você ainda não tem service:
      empresaNome = "Empresa #" + os.getIdEmpresa();
    } catch (Exception ex) {
      empresaNome = "Empresa #" + os.getIdEmpresa();
    }

    String assinaturaLinha1 = "";
    String assinaturaLinha2 = "";
    try {
      // fallback:
      assinaturaLinha1 = "Assinatura: ATIVA";
      assinaturaLinha2 = "Plano: (não informado)";
    } catch (Exception ex) {
      assinaturaLinha1 = "Assinatura: (não encontrada)";
      assinaturaLinha2 = "";
    }

    byte[] pdfBytes = PdfOrdemServicoGenerator.gerar(os, servicos, pecas, empresaNome, empresaCnpj);

    String filename = "OS-" + id + ".pdf";
    return ResponseEntity.ok()
        .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
        .body(pdfBytes);
  }

  @GetMapping("/nova")
  public String nova(Model model) {
    model.addAttribute("ordem", new OrdemServico());

    model.addAttribute("clientes", clienteService.findAll());
    model.addAttribute("veiculos", veiculoService.findAll());

    model.addAttribute("servicosOs", Collections.emptyList());
    model.addAttribute("pecasOs", Collections.emptyList());

    model.addAttribute("servicosCatalogo", servicoService.listAtivosDaEmpresa());
    model.addAttribute("pecasCatalogo", pecaService.listAtivosDaEmpresa());

    // ✅ técnicos
    model.addAttribute("tecnicosCatalogo", tecnicoService.listarDaEmpresa());
    model.addAttribute("tecnicosOs", Collections.emptyList());

    return "ordem/form";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    OrdemServico os = ordemServicoService.findById(id);

    model.addAttribute("ordem", os);

    model.addAttribute("clientes", clienteService.findAll());
    model.addAttribute("veiculos", veiculoService.findAll());

    model.addAttribute("servicosOs", servicoOSService.findByOs(id));
    model.addAttribute("pecasOs", pecaOSService.findByOs(id));

    model.addAttribute("servicosCatalogo", servicoService.listAtivosDaEmpresa());
    model.addAttribute("pecasCatalogo", pecaService.listAtivosDaEmpresa());

    // ✅ técnicos
    model.addAttribute("tecnicosCatalogo", tecnicoService.listarDaEmpresa());
    model.addAttribute("tecnicosOs", osTecnicoService.findByOs(id));

    return "ordem/form";
  }
}
