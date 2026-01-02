package br.com.torqueos.service;

import br.com.torqueos.model.Cliente;
import br.com.torqueos.model.Veiculo;
import br.com.torqueos.repository.VeiculoRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {

  private final VeiculoRepository repo;
  private final ClienteService clienteService;
  private final AssinaturaService assinaturaService;

  public VeiculoService(VeiculoRepository repo, ClienteService clienteService, AssinaturaService assinaturaService) {
    this.repo = repo;
    this.clienteService = clienteService;
    this.assinaturaService = assinaturaService;
  }

  public Veiculo create(Veiculo v, Long idCliente) {
    assinaturaService.validarAssinaturaAtiva();
    Long emp = TenantContext.getEmpresaId();
    v.setIdEmpresa(emp);
    if (idCliente != null) {
      Cliente c = clienteService.findById(idCliente);
      v.setCliente(c);
    } else {
      v.setCliente(null);
    }
    return repo.save(v);
  }

  public Veiculo findById(Long id) {
    Long emp = TenantContext.getEmpresaId();
    return repo.findByIdVeiculoAndIdEmpresa(id, emp)
      .orElseThrow(() -> new RuntimeException("Veículo não encontrado: " + id));
  }

  public List<Veiculo> findAll() {
    return repo.findByIdEmpresa(TenantContext.getEmpresaId());
  }

  public List<Veiculo> findByCliente(Long idCliente) {
    return repo.findByIdEmpresaAndCliente_IdCliente(TenantContext.getEmpresaId(), idCliente);
  }

  public Veiculo update(Long id, Veiculo novo, Long idCliente) {
    Veiculo v = findById(id);
    v.setPlaca(novo.getPlaca());
    v.setMarca(novo.getMarca());
    v.setModelo(novo.getModelo());
    v.setAno(novo.getAno());
    v.setChassi(novo.getChassi());
    if (idCliente != null) v.setCliente(clienteService.findById(idCliente));
    else v.setCliente(null);
    return repo.save(v);
  }

  public void delete(Long id) {
    repo.deleteByIdVeiculoAndIdEmpresa(id, TenantContext.getEmpresaId());
  }
  
  public br.com.torqueos.model.Veiculo findPorIdDaEmpresa(Long id) {
	  return findById(id); 
	}
  
  public br.com.torqueos.model.Veiculo find(Long id) {
	  return findPorIdDaEmpresa(id); // <-- troque pelo método REAL que existe no seu VeiculoService
	}
}
