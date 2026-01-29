package br.com.torqueos.controller;

import br.com.torqueos.model.PecaOS;
import br.com.torqueos.model.ServicoOS;
import br.com.torqueos.service.OrdemServicoService;
import br.com.torqueos.service.PecaOSService;
import br.com.torqueos.service.ServicoOSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordens")
public class OrdemServicoApiController {

  private final OrdemServicoService ordemServicoService;
  private final ServicoOSService servicoOSService;
  private final PecaOSService pecaOSService;

  public OrdemServicoApiController(OrdemServicoService ordemServicoService,
                                   ServicoOSService servicoOSService,
                                   PecaOSService pecaOSService) {
    this.ordemServicoService = ordemServicoService;
    this.servicoOSService = servicoOSService;
    this.pecaOSService = pecaOSService;
  }

  @GetMapping("/total/{id}")
  public ResponseEntity<Map<String, Object>> totalDaOs(@PathVariable("id") Long idOs) {
    // garante tenant e existência
    ordemServicoService.findById(idOs);

    BigDecimal totalServ = BigDecimal.ZERO;
    List<ServicoOS> servicos = servicoOSService.findByOs(idOs);
    if (servicos != null) {
      for (ServicoOS s : servicos) totalServ = totalServ.add(nz(s.getValor()));
    }

    BigDecimal totalPecas = BigDecimal.ZERO;
    List<PecaOS> pecas = pecaOSService.findByOs(idOs);
    if (pecas != null) {
      for (PecaOS p : pecas) {
        BigDecimal unit = nz(p.getValorUnitario());
        int qtd = (p.getQuantidade() == null ? 0 : p.getQuantidade());
        totalPecas = totalPecas.add(unit.multiply(new BigDecimal(qtd)));
      }
    }

    BigDecimal totalBase = totalServ.add(totalPecas).setScale(2, BigDecimal.ROUND_HALF_UP);

    Map<String, Object> body = new HashMap<>();
    body.put("idOs", idOs);
    body.put("totalServicos", totalServ.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    body.put("totalPecas", totalPecas.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    body.put("totalBase", totalBase.toString());
    return ResponseEntity.ok(body);
  }

  private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}
