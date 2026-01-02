package br.com.torqueos.repository;

import br.com.torqueos.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
  List<Veiculo> findByIdEmpresa(Long idEmpresa);
  List<Veiculo> findByIdEmpresaAndCliente_IdCliente(Long idEmpresa, Long idCliente);
  Optional<Veiculo> findByIdVeiculoAndIdEmpresa(Long idVeiculo, Long idEmpresa);
  void deleteByIdVeiculoAndIdEmpresa(Long idVeiculo, Long idEmpresa);
}
