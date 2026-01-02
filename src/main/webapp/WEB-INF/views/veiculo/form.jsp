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
<h2 class="mb-3">${veiculo.idVeiculo == null ? 'Novo Veículo' : 'Editar Veículo'}</h2>

<form:form method="POST" modelAttribute="veiculo" action="${pageContext.request.contextPath}/veiculos/salvar" cssClass="needs-validation" novalidate="novalidate">
  <form:hidden path="idVeiculo"/>

  <div>
    Cliente:
    <select name="clienteId">
      <option value="">-- selecione --</option>
      <c:forEach var="c" items="${clientes}">
        <option value="${c.idCliente}"
          <c:if test="${veiculo.cliente != null && veiculo.cliente.idCliente == c.idCliente}">selected</c:if>>
          ${c.nome}
        </option>
      </c:forEach>
    </select>
  </div>

  <div class="row g-3">
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Placa</label><form:input path="placa" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Marca</label><form:input path="marca" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Modelo</label><form:input path="modelo" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Ano</label><form:input path="ano" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Chassi</label><form:input path="chassi" cssClass="form-control" required="required" /></div>
</div>
</div>
<div style="margin-top:10px;">
    <div class="d-flex gap-2 mt-3"><button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/veiculos">Voltar</a>
  </div>
</div>
</form:form>

</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
