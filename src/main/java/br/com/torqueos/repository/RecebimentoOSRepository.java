package br.com.torqueos.repository;

import br.com.torqueos.model.RecebimentoOS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecebimentoOSRepository extends JpaRepository<RecebimentoOS, Long> {

  List<RecebimentoOS> findByIdEmpresaOrderByDataVencimentoAsc(Long idEmpresa);

  Optional<RecebimentoOS> findByIdRecebimentoAndIdEmpresa(Long idRecebimento, Long idEmpresa);

  List<RecebimentoOS> findByIdEmpresaAndStatusOrderByDataVencimentoAsc(Long idEmpresa, String status);

  List<RecebimentoOS> findByIdEmpresaAndDataVencimentoBetweenOrderByDataVencimentoAsc(Long idEmpresa, LocalDate ini, LocalDate fim);

  List<RecebimentoOS> findByIdEmpresaAndOrdemServico_IdOsOrderByCriadoEmDesc(Long idEmpresa, Long idOs);

  void deleteByIdRecebimentoAndIdEmpresa(Long idRecebimento, Long idEmpresa);
  
  

  List<RecebimentoOS> findByIdEmpresaAndStatusIgnoreCase(Long idEmpresa, String status);

}
