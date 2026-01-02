package br.com.torqueos.service;

import br.com.torqueos.model.PecaOS;
import br.com.torqueos.repository.PecaOSRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PecaOSService {

  private final PecaOSRepository repo;
  private final OrdemServicoService ordemServicoService;
  private final AssinaturaService assinaturaService;

  public PecaOSService(PecaOSRepository repo, OrdemServicoService ordemServicoService, AssinaturaService assinaturaService) {
    this.repo = repo;
    this.ordemServicoService = ordemServicoService;
    this.assinaturaService = assinaturaService;
  }

  public PecaOS create(PecaOS p, Long idOs) {
    assinaturaService.validarAssinaturaAtiva();
    Long emp = TenantContext.getEmpresaId();
    p.setIdEmpresa(emp);
    if (idOs != null) p.setOrdemServico(ordemServicoService.findById(idOs));
    else p.setOrdemServico(null);
    return repo.save(p);
  }

  public PecaOS findById(Long id) {
    return repo.findByIdPecaOsAndIdEmpresa(id, TenantContext.getEmpresaId())
      .orElseThrow(() -> new RuntimeException("Peça não encontrada: " + id));
  }

  public List<PecaOS> findAll() {
    return repo.findByIdEmpresa(TenantContext.getEmpresaId());
  }

  public List<PecaOS> findByOs(Long idOs) {
    return repo.findByIdEmpresaAndOrdemServico_IdOs(TenantContext.getEmpresaId(), idOs);
  }

  public PecaOS update(Long id, PecaOS novo, Long idOs) {
    PecaOS p = findById(id);
    p.setDescricao(novo.getDescricao());
    p.setQuantidade(novo.getQuantidade());
    p.setValorUnitario(novo.getValorUnitario());
    if (idOs != null) p.setOrdemServico(ordemServicoService.findById(idOs));
    else p.setOrdemServico(null);
    return repo.save(p);
  }

  public void delete(Long id) {
    repo.deleteByIdPecaOsAndIdEmpresa(id, TenantContext.getEmpresaId());
  }
}
