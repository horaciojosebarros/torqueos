package br.com.torqueos.repository;

import br.com.torqueos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  List<Cliente> findByIdEmpresa(Long idEmpresa);
  Optional<Cliente> findByIdClienteAndIdEmpresa(Long idCliente, Long idEmpresa);
  void deleteByIdClienteAndIdEmpresa(Long idCliente, Long idEmpresa);
}
