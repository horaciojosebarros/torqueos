package br.com.torqueos.repository;

import br.com.torqueos.model.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {
  Optional<Assinatura> findByIdEmpresa(Long idEmpresa);
}
