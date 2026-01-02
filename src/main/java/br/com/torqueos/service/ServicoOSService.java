package br.com.torqueos.service;

import br.com.torqueos.model.ServicoOS;
import br.com.torqueos.repository.ServicoOSRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoOSService {

  private final ServicoOSRepository repo;
  private final OrdemServicoService ordemServicoService;
  private final AssinaturaService assinaturaService;

  public ServicoOSService(ServicoOSRepository repo, OrdemServicoService ordemServicoService, AssinaturaService assinaturaService) {
    this.repo = repo;
    this.ordemServicoService = ordemServicoService;
    this.assinaturaService = assinaturaService;
  }

  public ServicoOS create(ServicoOS s, Long idOs) {
    assinaturaService.validarAssinaturaAtiva();
    Long emp = TenantContext.getEmpresaId();
    s.setIdEmpresa(emp);
    if (idOs != null) s.setOrdemServico(ordemServicoService.findById(idOs));
    else s.setOrdemServico(null);
    return repo.save(s);
  }

  public ServicoOS findById(Long id) {
    return repo.findByIdServicoOsAndIdEmpresa(id, TenantContext.getEmpresaId())
      .orElseThrow(() -> new RuntimeException("Serviço não encontrado: " + id));
  }

  public List<ServicoOS> findAll() {
    return repo.findByIdEmpresa(TenantContext.getEmpresaId());
  }

  public List<ServicoOS> findByOs(Long idOs) {
    return repo.findByIdEmpresaAndOrdemServico_IdOs(TenantContext.getEmpresaId(), idOs);
  }

  public ServicoOS update(Long id, ServicoOS novo, Long idOs) {
    ServicoOS s = findById(id);
    s.setDescricao(novo.getDescricao());
    s.setValor(novo.getValor());
    if (idOs != null) s.setOrdemServico(ordemServicoService.findById(idOs));
    else s.setOrdemServico(null);
    return repo.save(s);
  }

  public void delete(Long id) {
    repo.deleteByIdServicoOsAndIdEmpresa(id, TenantContext.getEmpresaId());
  }
}
