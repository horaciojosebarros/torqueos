package br.com.torqueos.service;

import br.com.torqueos.model.Empresa;
import br.com.torqueos.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {
  private final EmpresaRepository repo;
  public EmpresaService(EmpresaRepository repo) { this.repo = repo; }

  public Empresa findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + id));
  }
}
