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
<div class="page-title mb-3"><h2 class="mb-3">Planos (cadastro global - demo)</h2><a class="btn btn-primary" href="${pageContext.request.contextPath}/planos/novo">Novo Plano</a></div></div>

<table class="table table-striped table-hover app-table">
  <tr><th>ID</th><th>Nome</th><th>PreÃ§o Mensal</th><th>Limite UsuÃ¡rios</th><th>Limite OS/mÃªs</th><th>Ativo</th><th>AÃ§Ãµes</th></tr>
  <c:forEach var="p" items="${planos}">
    <tr>
      <td>${p.idPlano}</td>
      <td>${p.nome}</td>
      <td>${p.precoMensal}</td>
      <td>${p.limiteUsuarios}</td>
      <td>${p.limiteOsMes}</td>
      <td>${p.ativo}</td>
      <td>
        <a href="${pageContext.request.contextPath}/planos/editar/${p.idPlano}"" class="btn btn-sm btn-outline-primary">Editar</a> |
        <a href="${pageContext.request.contextPath}/planos/excluir/${p.idPlano}"" class="btn btn-sm btn-outline-danger">Excluir</a>
      </td>
    </tr>
  </c:forEach>
</table>
</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body></html>
