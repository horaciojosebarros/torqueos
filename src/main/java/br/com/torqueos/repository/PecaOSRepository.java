package br.com.torqueos.repository;

import br.com.torqueos.model.PecaOS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PecaOSRepository extends JpaRepository<PecaOS, Long> {
  List<PecaOS> findByIdEmpresa(Long idEmpresa);
  List<PecaOS> findByIdEmpresaAndOrdemServico_IdOs(Long idEmpresa, Long idOs);
  Optional<PecaOS> findByIdPecaOsAndIdEmpresa(Long idPecaOs, Long idEmpresa);
  void deleteByIdPecaOsAndIdEmpresa(Long idPecaOs, Long idEmpresa);
  void deleteByIdEmpresaAndOrdemServico_IdOs(Long idEmpresa, Long idOs);
}
