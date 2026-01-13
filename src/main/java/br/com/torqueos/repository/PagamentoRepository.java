package br.com.torqueos.repository;

import br.com.torqueos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

  // manter compatibilidade (se você usa em outro lugar)
  List<Pagamento> findByAssinatura_IdAssinatura(Long idAssinatura);

  // multiempresa
  Optional<Pagamento> findByIdPagamentoAndIdEmpresa(Long idPagamento, Long idEmpresa);

  List<Pagamento> findByIdEmpresaOrderByDataVencimentoAsc(Long idEmpresa);

  List<Pagamento> findByIdEmpresaAndStatusOrderByDataVencimentoAsc(Long idEmpresa, String status);

  List<Pagamento> findByIdEmpresaAndDataPagamentoBetweenOrderByDataPagamentoAsc(Long idEmpresa, LocalDate ini, LocalDate fim);

  List<Pagamento> findByIdEmpresaAndOrdemServico_IdOsOrderByCriadoEmDesc(Long idEmpresa, Long idOs);
}
