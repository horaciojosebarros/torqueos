<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<div class="page-title mb-3"><h2 class="mb-3">Veículo</h2></div>

<p>
  <a href="${pageContext.request.contextPath}/veiculos/novo">Novo Veículo</a>
  <c:if test="${clienteId != null}">
    | <a href="${pageContext.request.contextPath}/clientes">Voltar ao Cliente</a>
  </c:if>
</p>

<table class="table table-striped table-hover app-table">
  <tr>
    <th>ID</th><th>Cliente</th><th>Placa</th><th>Marca</th><th>Modelo</th><th>Ano</th><th>Chassi</th><th>Ações</th>
  </tr>
  <c:forEach var="v" items="${veiculos}">
    <tr>
      <td>${v.idVeiculo}</td>
      <td><c:out value="${v.cliente != null ? v.cliente.nome : '-'}"/></td>
      <td>${v.placa}</td>
      <td>${v.marca}</td>
      <td>${v.modelo}</td>
      <td>${v.ano}</td>
      <td>${v.chassi}</td>
      <td>
        <a href="${pageContext.request.contextPath}/veiculos/editar/${v.idVeiculo}"" class="btn btn-sm btn-outline-primary">Editar</a> |
        <a href="${pageContext.request.contextPath}/veiculos/excluir/${v.idVeiculo}"" class="btn btn-sm btn-outline-danger">Excluir</a>
      </td>
    </tr>
  </c:forEach>
</table>
</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
