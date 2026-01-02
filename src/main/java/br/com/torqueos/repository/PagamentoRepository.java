package br.com.torqueos.repository;

import br.com.torqueos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
  List<Pagamento> findByAssinatura_IdAssinatura(Long idAssinatura);
}
