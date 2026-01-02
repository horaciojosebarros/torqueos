package br.com.torqueos.service;

import br.com.torqueos.model.Cliente;
import br.com.torqueos.repository.ClienteRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

  private final ClienteRepository repo;
  private final AssinaturaService assinaturaService;

  public ClienteService(ClienteRepository repo, AssinaturaService assinaturaService) {
    this.repo = repo;
    this.assinaturaService = assinaturaService;
  }

  public Cliente create(Cliente c) {
    assinaturaService.validarAssinaturaAtiva();
    c.setIdEmpresa(TenantContext.getEmpresaId());
    return repo.save(c);
  }

  public Cliente findById(Long id) {
    Long emp = TenantContext.getEmpresaId();
    return repo.findByIdClienteAndIdEmpresa(id, emp)
      .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + id));
  }

  public List<Cliente> findAll() {
    return repo.findByIdEmpresa(TenantContext.getEmpresaId());
  }

  public Cliente update(Long id, Cliente novo) {
    Cliente c = findById(id);
    c.setNome(novo.getNome());
    c.setTelefone(novo.getTelefone());
    c.setEmail(novo.getEmail());
    c.setEndereco(novo.getEndereco());
    return repo.save(c);
  }

  public void delete(Long id) {
    repo.deleteByIdClienteAndIdEmpresa(id, TenantContext.getEmpresaId());
  }
  
  public Cliente findPorIdDaEmpresa(Long id) {
	  return findById(id); 
	}
  
  public br.com.torqueos.model.Cliente find(Long id) {
	  return findPorIdDaEmpresa(id); 
	}
}
