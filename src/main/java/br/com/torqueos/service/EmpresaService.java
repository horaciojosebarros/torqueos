package br.com.torqueos.service;

import br.com.torqueos.model.Empresa;
import br.com.torqueos.repository.EmpresaRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

  private final EmpresaRepository repo;

  public EmpresaService(EmpresaRepository repo) {
    this.repo = repo;
  }

  public Empresa getEmpresaAtual() {
    Long empId = TenantContext.getEmpresaId();
    if (empId == null) throw new RuntimeException("Tenant não definido (empresaId).");
    return repo.findById(empId).orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + empId));
  }

  public String getNomeEmpresaAtual() {
    return getEmpresaAtual().getNome();
  }
}
