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
<div class="page-title mb-3"><h2 class="mb-3">Clientes</h2><a class="btn btn-primary" href="${pageContext.request.contextPath}/clientes/novo">Novo Cliente</a></div></div>
<table class="table table-striped table-hover app-table">
  <tr>
    <th>ID</th><th>Nome</th><th>Telefone</th><th>Email</th><th>Endereço</th><th>Ações</th>
  </tr>
  <c:forEach var="c" items="${clientes}">
    <tr>
      <td>${c.idCliente}</td>
      <td>${c.nome}</td>
      <td>${c.telefone}</td>
      <td>${c.email}</td>
      <td>${c.endereco}</td>
      <td>
        <a href="${pageContext.request.contextPath}/clientes/editar/${c.idCliente}"" class="btn btn-sm btn-outline-primary">Editar</a> |
        <a href="${pageContext.request.contextPath}/clientes/excluir/${c.idCliente}"" class="btn btn-sm btn-outline-danger">Excluir</a> |
        <a href="${pageContext.request.contextPath}/veiculos?clienteId=${c.idCliente}">Veículos</a>
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
