package br.com.torqueos.service;

import br.com.torqueos.model.OrdemServico;
import br.com.torqueos.model.OrdemServicoTecnico;
import br.com.torqueos.model.Tecnico;
import br.com.torqueos.repository.OrdemServicoTecnicoRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdemServicoTecnicoService {

  private final OrdemServicoTecnicoRepository repo;
  private final OrdemServicoService ordemServicoService;
  private final TecnicoService tecnicoService; // cadastro de técnicos
  private final AssinaturaService assinaturaService;

  public OrdemServicoTecnicoService(OrdemServicoTecnicoRepository repo,
                                    OrdemServicoService ordemServicoService,
                                    TecnicoService tecnicoService,
                                    AssinaturaService assinaturaService) {
    this.repo = repo;
    this.ordemServicoService = ordemServicoService;
    this.tecnicoService = tecnicoService;
    this.assinaturaService = assinaturaService;
  }

  public List<OrdemServicoTecnico> listarDaOs(Long idOs) {
    Long emp = TenantContext.getEmpresaId();
    return repo.findByIdEmpresaAndOrdemServico_IdOsOrderByIdOsTecnicoAsc(emp, idOs);
  }

  /**
   * Salva a lista "do zero" (remove tudo e inclui de novo).
   * Perfeito pro JSP com combos dinâmicos.
   */
  @Transactional
  public void salvarTecnicosDaOs(Long idOs, List<Long> tecnicosIds, List<Integer> percentuais, List<String> papeis) {
    assinaturaService.validarAssinaturaAtiva();

    Long emp = TenantContext.getEmpresaId();
    if (emp == null) throw new RuntimeException("Tenant não definido (empresaId).");

    OrdemServico os = ordemServicoService.findById(idOs); // já filtra por tenant

    // remove todos vínculos atuais
    repo.deleteVinculosDaOs(emp, idOs);
    

    if (tecnicosIds == null) return;

    LocalDateTime now = LocalDateTime.now();

    for (int i = 0; i < tecnicosIds.size(); i++) {
      Long tid = tecnicosIds.get(i);
      if (tid == null) continue;

      Tecnico t = tecnicoService.findById(tid);

      OrdemServicoTecnico link = new OrdemServicoTecnico();
      link.setIdEmpresa(emp);
      link.setOrdemServico(os);
      link.setTecnico(t);

      // percentual NÃO pode ser null (coluna nullable=false)
      Integer pct = 0;
      if (percentuais != null && percentuais.size() > i && percentuais.get(i) != null) {
        pct = percentuais.get(i);
      }
      link.setPercentual(pct);

      String papel = (papeis != null && papeis.size() > i) ? papeis.get(i) : null;
      link.setPapel((papel == null || papel.trim().isEmpty()) ? "RESPONSAVEL" : papel);

      link.setCriadoEm(now);
      link.setAtualizadoEm(now);

      repo.save(link);
    }
  }

  /**
   * Corrigido: não existe tecnicoService.findByOs(...)
   * O vínculo OS<->Técnico é consultado aqui, via repo.
   */
  public List<OrdemServicoTecnico> findByOs(Long idOs) {
    return listarDaOs(idOs);
  }
}
