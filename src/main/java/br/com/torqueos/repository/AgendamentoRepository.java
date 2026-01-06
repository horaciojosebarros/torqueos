package br.com.torqueos.repository;

import br.com.torqueos.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

  List<Agendamento> findByIdEmpresaOrderByInicioDesc(Long idEmpresa);

  Optional<Agendamento> findByIdAgendamentoAndIdEmpresa(Long idAgendamento, Long idEmpresa);

  // Opcional: agenda do dia (ou intervalo)
  List<Agendamento> findByIdEmpresaAndInicioBetweenOrderByInicioAsc(Long idEmpresa, LocalDateTime ini, LocalDateTime fim);

  // Opcional: bloquear conflito de horário do mesmo responsável.
  // Regra: existe conflito se (inicio_existente < fim_novo) e (fim_existente > inicio_novo)
  @Query(value = "select exists(" + 
     " select 1 " +
     " from agendamentos a " +
     " where a.id_empresa = :idEmpresa " +
     "   and a.id_usuario_responsavel = :idUsuario " +
     "   and (:idIgnorar is null or a.id_agendamento <> :idIgnorar) " + 
     "   and a.inicio < :fimNovo " +
     "   and (a.inicio + (a.duracao_minutos || ' minutes')::interval) > :inicioNovo " +
    " ) " 
  , nativeQuery = true)  
  boolean existeConflito(@Param("idEmpresa") Long idEmpresa,
                         @Param("idUsuario") Long idUsuario,
                         @Param("inicioNovo") LocalDateTime inicioNovo,
                         @Param("fimNovo") LocalDateTime fimNovo,
                         @Param("idIgnorar") Long idIgnorar);
}
