package br.com.torqueos.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.torqueos.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
  List<Servico> findByIdEmpresaAndAtivoTrueOrderByNomeAsc(Long idEmpresa);
  Optional<Servico> findByIdServicoAndIdEmpresa(Long idServico, Long idEmpresa);
}
