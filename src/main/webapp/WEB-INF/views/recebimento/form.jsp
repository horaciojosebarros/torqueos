<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <title>TorqueOS</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="container py-4">
  <div class="card app-card">
    <div class="card-body">

      <h2 class="mb-3">${recebimento.idRecebimento == null ? 'Novo Recebimento (OS)' : 'Editar Recebimento (OS)'}</h2>

      <form:form method="POST" modelAttribute="recebimento"
                 action="${pageContext.request.contextPath}/recebimentos/salvar"
                 cssClass="needs-validation" novalidate="novalidate">

        <form:hidden path="idRecebimento"/>

        <div class="row g-3">

          <!-- OS -->
          <div class="col-md-6">
            <label class="form-label">Ordem de Serviço</label>
            <select class="form-select" name="osId" required="required">
              <option value="">-- selecione --</option>
              <c:forEach var="os" items="${ordens}">
                <option value="${os.idOs}"
                  <c:if test="${(osId != null && osId == os.idOs) || (recebimento.ordemServico != null && recebimento.ordemServico.idOs == os.idOs)}">selected</c:if>>
                  #${os.idOs} - ${os.veiculo != null ? os.veiculo.placa : ''} - ${os.cliente != null ? os.cliente.nome : ''}
                </option>
              </c:forEach>
            </select>
          </div>

          <!-- Status -->
          <div class="col-md-3">
            <label class="form-label">Status</label>
            <form:select path="status" cssClass="form-select" required="required" id="status">
              <form:option value="PENDENTE">PENDENTE</form:option>
              <form:option value="PAGO">PAGO</form:option>
              <form:option value="CANCELADO">CANCELADO</form:option>
              <form:option value="ESTORNADO">ESTORNADO</form:option>
            </form:select>
          </div>

          <!-- Tipo -->
          <div class="col-md-3">
            <label class="form-label">Tipo de pagamento</label>
            <form:select path="tipoPagamento" cssClass="form-select" required="required">
              <form:option value="PIX">PIX</form:option>
              <form:option value="DINHEIRO">DINHEIRO</form:option>
              <form:option value="CARTAO_DEBITO">CARTAO_DEBITO</form:option>
              <form:option value="CARTAO_CREDITO">CARTAO_CREDITO</form:option>
              <form:option value="BOLETO">BOLETO</form:option>
              <form:option value="TRANSFERENCIA">TRANSFERENCIA</form:option>
            </form:select>
          </div>

          <!-- Valor -->
          <div class="col-md-4">
            <label class="form-label">Valor total</label>
            <form:input path="valorTotal" cssClass="form-control" required="required"/>
          </div>

          <!-- Vencimento (NÃO bindar LocalDate no form tag) -->
          <div class="col-md-4">
            <label class="form-label">Vencimento</label>
            <input type="date" class="form-control" name="dataVencimento" value="${dataVencimentoIso}"/>
          </div>

          <!-- Data/Hora Pagamento separados (evita bind de LocalDateTime) -->
          <div class="col-md-2">
            <label class="form-label">Data pagamento</label>
            <input type="date" class="form-control" name="dataPagamento" value="${dataPagamentoIso}"/>
          </div>

          <div class="col-md-2">
            <label class="form-label">Hora pagamento</label>
            <input type="time" class="form-control" name="horaPagamento" value="${horaPagamento}"/>
          </div>

          <!-- Financeiro -->
          <div class="col-md-3">
            <label class="form-label">Desconto</label>
            <form:input path="desconto" cssClass="form-control"/>
          </div>
          <div class="col-md-3">
            <label class="form-label">Juros</label>
            <form:input path="juros" cssClass="form-control"/>
          </div>
          <div class="col-md-3">
            <label class="form-label">Multa</label>
            <form:input path="multa" cssClass="form-control"/>
          </div>
          <div class="col-md-3">
            <label class="form-label">Parcelas</label>
            <form:input path="parcelas" cssClass="form-control"/>
          </div>

          <div class="col-md-6">
            <label class="form-label">Referência</label>
            <form:input path="referencia" cssClass="form-control"/>
          </div>
          <div class="col-md-6">
            <label class="form-label">Documento ref</label>
            <form:input path="documentoRef" cssClass="form-control"/>
          </div>

          <div class="col-12">
            <label class="form-label">Observação</label>
            <form:textarea path="observacao" cssClass="form-control" rows="3"/>
          </div>
        </div>

        <div class="d-flex gap-2 mt-4">
          <button type="submit" class="btn btn-primary">Salvar</button>
          <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/recebimentos">Voltar</a>
        </div>

      </form:form>

    </div>
  </div>
</div>

<script>
  (function() {
    const statusEl = document.getElementById('status');
    const dataPgEl = document.querySelector('input[name="dataPagamento"]');
    const horaPgEl = document.querySelector('input[name="horaPagamento"]');

    function togglePagamentoFields() {
      const st = (statusEl && statusEl.value) ? statusEl.value : '';
      const isPago = (st === 'PAGO');

      // se for PAGO, sugere data/hora (mas não obriga)
      if (isPago) {
        if (dataPgEl && !dataPgEl.value) dataPgEl.value = new Date().toISOString().slice(0,10);
        if (horaPgEl && !horaPgEl.value) horaPgEl.value = new Date().toTimeString().slice(0,5);
      }
    }

    if (statusEl) {
      statusEl.addEventListener('change', togglePagamentoFields);
      togglePagamentoFields();
    }
  })();
</script>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
