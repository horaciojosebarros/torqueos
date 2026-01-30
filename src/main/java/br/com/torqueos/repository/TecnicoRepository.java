package br.com.torqueos.repository;

import br.com.torqueos.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

  List<Tecnico> findByIdEmpresaOrderByNomeAsc(Long idEmpresa);

  Optional<Tecnico> findByIdTecnicoAndIdEmpresa(Long idTecnico, Long idEmpresa);

  void deleteByIdTecnicoAndIdEmpresa(Long idTecnico, Long idEmpresa);

  boolean existsByIdEmpresaAndCpf(Long idEmpresa, String cpf);

  boolean existsByIdEmpresaAndCpfAndIdTecnicoNot(Long idEmpresa, String cpf, Long idTecnico);
}
