package br.com.torqueos.service;

import br.com.torqueos.model.Empresa;
import br.com.torqueos.repository.EmpresaRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public Empresa atualizarEmpresaAtual(Empresa form) {
    Empresa atual = getEmpresaAtual();

    // atualiza APENAS campos editáveis
    atual.setNome(form.getNome());
    atual.setCnpj(form.getCnpj());
    atual.setTelefone(form.getTelefone());
    atual.setEndereco(form.getEndereco());
    atual.setBairro(form.getBairro());
    atual.setCidade(form.getCidade());
    atual.setUf(form.getUf());

    // status/criadoEm/id NÃO mudam por aqui
    return repo.save(atual);
  }
}
