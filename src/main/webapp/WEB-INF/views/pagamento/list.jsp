<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html><head>
<title>TorqueOS</title>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<div class="container py-4">
<div class="card app-card">
<div class="card-body">
<div class="page-title mb-3"><h2 class="mb-3">Pagamentos</h2><a class="btn btn-primary" href="${pageContext.request.contextPath}/pagamentos/novo">Novo Pagamento</a></div></div>

<table class="table table-striped table-hover app-table">
  <tr><th>ID</th><th>Valor</th><th>Data</th><th>Status</th><th>ReferÃªncia</th><th>AÃ§Ãµes</th></tr>
  <c:forEach var="p" items="${pagamentos}">
    <tr>
      <td>${p.idPagamento}</td>
      <td>${p.valor}</td>
      <td>${p.dataPagamento}</td>
      <td>${p.status}</td>
      <td>${p.referencia}</td>
      <td>
        <a href="${pageContext.request.contextPath}/pagamentos/editar/${p.idPagamento}"" class="btn btn-sm btn-outline-primary">Editar</a> |
        <a href="${pageContext.request.contextPath}/pagamentos/excluir/${p.idPagamento}"" class="btn btn-sm btn-outline-danger">Excluir</a>
      </td>
    </tr>
  </c:forEach>
</table>
</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body></html>
