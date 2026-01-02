package br.com.torqueos.repository;

import br.com.torqueos.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
  List<OrdemServico> findByIdEmpresa(Long idEmpresa);
  void deleteByIdOsAndIdEmpresa(Long idOs, Long idEmpresa);
  long countByIdEmpresa(Long idEmpresa);
  java.util.Optional<OrdemServico> findByIdOsAndIdEmpresa(Long idOs, Long idEmpresa);
  java.util.List<OrdemServico> findByIdEmpresaOrderByIdOsDesc(Long idEmpresa);

}
