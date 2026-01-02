package br.com.torqueos.repository;

import br.com.torqueos.model.ServicoOS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicoOSRepository extends JpaRepository<ServicoOS, Long> {
  List<ServicoOS> findByIdEmpresa(Long idEmpresa);
  List<ServicoOS> findByIdEmpresaAndOrdemServico_IdOs(Long idEmpresa, Long idOs);
  Optional<ServicoOS> findByIdServicoOsAndIdEmpresa(Long idServicoOs, Long idEmpresa);
  void deleteByIdServicoOsAndIdEmpresa(Long idServicoOs, Long idEmpresa);
  void deleteByIdEmpresaAndOrdemServico_IdOs(Long idEmpresa, Long idOs);
  
}
