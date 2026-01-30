package br.com.torqueos.repository;

import br.com.torqueos.model.OrdemServicoTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdemServicoTecnicoRepository extends JpaRepository<OrdemServicoTecnico, Long> {

  List<OrdemServicoTecnico> findByIdEmpresaAndOrdemServico_IdOsOrderByIdOsTecnicoAsc(Long idEmpresa, Long idOs);

  /**
   * DELETE "bulk" com limpeza automática do Persistence Context.
   * Isso evita o cenário clássico:
   * - faz delete
   * - no mesmo transaction, faz insert
   * - Hibernate ainda "enxerga" registros antigos e estoura UNIQUE
   */
  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query("delete from OrdemServicoTecnico ost " +
         "where ost.idEmpresa = :idEmpresa and ost.ordemServico.idOs = :idOs")
  int deleteVinculosDaOs(@Param("idEmpresa") Long idEmpresa, @Param("idOs") Long idOs);

  // (Opcional) ajuda a debugar rapidamente
  boolean existsByIdEmpresaAndOrdemServico_IdOs(Long idEmpresa, Long idOs);
}
