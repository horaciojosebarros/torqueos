package br.com.torqueos.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.torqueos.model.Peca;

public interface PecaRepository extends JpaRepository<Peca, Long> {
  List<Peca> findByIdEmpresaAndAtivoTrueOrderByNomeAsc(Long idEmpresa);
  Optional<Peca> findByIdPecaAndIdEmpresa(Long idPeca, Long idEmpresa);
}
