package br.com.torqueos.service;

import br.com.torqueos.model.Plano;
import br.com.torqueos.repository.PlanoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoService {
  private final PlanoRepository repo;
  public PlanoService(PlanoRepository repo) { this.repo = repo; }

  public List<Plano> findAll() { return repo.findAll(); }

  public Plano findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new RuntimeException("Plano não encontrado: " + id));
  }

  public Plano save(Plano p) { return repo.save(p); }

  public void delete(Long id) { repo.deleteById(id); }
}
