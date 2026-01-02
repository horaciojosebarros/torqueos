<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html><head><title>Serviços</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<div class="container py-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h2>Serviços</h2>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/servicos/novo">Novo</a>
  </div>

  <div class="card app-card"><div class="card-body">
    <div class="table-responsive">
      <table class="table table-striped align-middle">
        <thead><tr><th>Nome</th><th>Valor</th><th>Ativo</th><th style="width:160px;"></th></tr></thead>
        <tbody>
        <c:forEach var="s" items="${lista}">
          <tr>
            <td>${s.nome}</td>
            <td>R$ ${s.valor}</td>
            <td>${s.ativo}</td>
            <td class="d-flex gap-2">
              <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/servicos/editar/${s.idServico}">Editar</a>
              <a class="btn btn-sm btn-outline-danger" href="${pageContext.request.contextPath}/servicos/excluir/${s.idServico}">Excluir</a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div></div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body></html>
