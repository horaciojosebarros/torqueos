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

        <h2 class="mb-3">${pagamento.idPagamento == null ? 'Novo Pagamento' : 'Editar Pagamento'}</h2>

        <form:form method="POST" modelAttribute="pagamento"
                   action="${pageContext.request.contextPath}/pagamentos/salvar"
                   cssClass="needs-validation" novalidate="novalidate">

          <form:hidden path="idPagamento"/>

          <div class="row g-3">

            <!-- OS -->
            <div class="col-md-12">
              <label class="form-label">Ordem de Serviço (opcional)</label>
              <select class="form-select" name="osId">
                <option value="">-- sem OS vinculada --</option>
                <c:forEach var="os" items="${ordens}">
                  <option value="${os.idOs}"
                    <c:if test="${osId != null && osId == os.idOs}">selected</c:if>>
                    OS #${os.idOs}
                    <c:if test="${os.veiculo != null}"> - ${os.veiculo.placa}</c:if>
                    <c:if test="${os.cliente != null}"> - ${os.cliente.nome}</c:if>
                  </option>
                </c:forEach>
              </select>
            </div>

            <div class="col-md-4">
              <label class="form-label">Valor</label>
              <form:input path="valor" cssClass="form-control" required="required"/>
            </div>

            <div class="col-md-4">
              <label class="form-label">Tipo de pagamento</label>
              <form:select path="tipoPagamento" cssClass="form-select" required="required">
                <form:option value="PIX">PIX</form:option>
                <form:option value="DINHEIRO">Dinheiro</form:option>
                <form:option value="CARTAO_CREDITO">Cartão de crédito</form:option>
                <form:option value="CARTAO_DEBITO">Cartão de débito</form:option>
                <form:option value="BOLETO">Boleto</form:option>
                <form:option value="TRANSFERENCIA">Transferência</form:option>
              </form:select>
            </div>

            <div class="col-md-4">
              <label class="form-label">Status</label>
              <form:select path="status" cssClass="form-select" required="required">
                <form:option value="PENDENTE">Pendente</form:option>
                <form:option value="PAGO">Pago</form:option>
                <form:option value="CANCELADO">Cancelado</form:option>
              </form:select>
            </div>

            <div class="col-md-6">
              <label class="form-label">Data de vencimento</label>
              <form:input path="dataVencimento" type="date" cssClass="form-control"/>
            </div>

            <div class="col-md-6">
              <label class="form-label">Data de pagamento</label>
              <form:input path="dataPagamento" type="date" cssClass="form-control"/>
            </div>

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
              <label class="form-label">Documento/Comprovante</label>
              <form:input path="documentoRef" cssClass="form-control"/>
            </div>

            <div class="col-12">
              <label class="form-label">Observação</label>
              <form:textarea path="observacao" cssClass="form-control" rows="3"/>
            </div>

          </div>

          <div class="d-flex gap-2 mt-4">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/pagamentos">Voltar</a>
          </div>

        </form:form>

      </div>
    </div>
  </div>

  <%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
