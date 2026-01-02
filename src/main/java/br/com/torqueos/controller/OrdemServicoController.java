package br.com.torqueos.controller;

import br.com.torqueos.model.OrdemServico;
import br.com.torqueos.service.*;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

  public OrdemServicoController(OrdemServicoService ordemServicoService,
                                ClienteService clienteService,
                                VeiculoService veiculoService,
                                ServicoOSService servicoOSService,
                                PecaOSService pecaOSService,
                                PecaService pecaService,
                                ServicoService servicoService) {
    this.ordemServicoService = ordemServicoService;
    this.clienteService = clienteService;
    this.veiculoService = veiculoService;
    this.servicoOSService = servicoOSService;
    this.pecaOSService = pecaOSService;
    this.pecaService = pecaService;
    this.servicoService = servicoService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("ordens", ordemServicoService.findAll());
    return "ordem/list";
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

    return "ordem/form";
  }

  @PostMapping("/salvar")
  public String salvar(@ModelAttribute("ordem") OrdemServico ordem,
                       @RequestParam(value = "clienteId", required = false) Long clienteId,
                       @RequestParam(value = "veiculoId", required = false) Long veiculoId,
                       @RequestParam(value = "servicoId", required = false) List<Long> servicoId,
                       @RequestParam(value = "pecaId", required = false) List<Long> pecaId,
                       @RequestParam(value = "pecaQuantidade", required = false) List<Integer> pecaQuantidade) {

    if (ordem.getIdOs() == null) {
      ordemServicoService.create(ordem, clienteId, veiculoId, servicoId, pecaId, pecaQuantidade);
    } else {
      ordemServicoService.update(ordem.getIdOs(), ordem, clienteId, veiculoId, servicoId, pecaId, pecaQuantidade);
    }

    return "redirect:/ordens";
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

    return "ordem/form";
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
      // SE você tiver EmpresaService, use aqui (exemplo):
      // Empresa e = empresaService.findPorIdDaEmpresa(os.getIdEmpresa());  // (ou findById)
      // empresaNome = e.getNome();
      // empresaCnpj = e.getCnpj();

      // fallback se você ainda não tem service:
      empresaNome = "Empresa #" + os.getIdEmpresa();
    } catch (Exception ex) {
      empresaNome = "Empresa #" + os.getIdEmpresa();
    }

    String assinaturaLinha1 = "";
    String assinaturaLinha2 = "";
    try {
      // Se você tiver algo assim no seu AssinaturaService:
      // Assinatura a = assinaturaService.getAtivaDaEmpresa(os.getIdEmpresa());
      // assinaturaLinha1 = "Assinatura: " + a.getStatus();
      // assinaturaLinha2 = "Plano: " + a.getPlano().getNome() + " | Próximo ciclo: " + a.getProximoCiclo();

      // fallback:
      assinaturaLinha1 = "Assinatura: ATIVA";
      assinaturaLinha2 = "Plano: (não informado)";
    } catch (Exception ex) {
      assinaturaLinha1 = "Assinatura: (não encontrada)";
      assinaturaLinha2 = "";
    }

    byte[] pdfBytes = br.com.torqueos.util.PdfOrdemServicoGenerator.gerar(
        os, servicos, pecas,
        empresaNome, empresaCnpj,
        assinaturaLinha1, assinaturaLinha2
    );

    String filename = "OS-" + id + ".pdf";
    return ResponseEntity.ok()
        .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
        .body(pdfBytes);
  }

}
