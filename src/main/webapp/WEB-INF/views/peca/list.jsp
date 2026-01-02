<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html><head><title>Peças</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<div class="container py-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h2>Peças / Produtos</h2>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/pecas/novo">Novo</a>
  </div>

  <div class="card app-card"><div class="card-body">
    <div class="table-responsive">
      <table class="table table-striped align-middle">
        <thead><tr><th>Nome</th><th>Valor Unit.</th><th>Ativo</th><th style="width:160px;"></th></tr></thead>
        <tbody>
        <c:forEach var="p" items="${lista}">
          <tr>
            <td>${p.nome}</td>
            <td>R$ ${p.valorUnitario}</td>
            <td>${p.ativo}</td>
            <td class="d-flex gap-2">
              <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/pecas/editar/${p.idPeca}">Editar</a>
              <a class="btn btn-sm btn-outline-danger" href="${pageContext.request.contextPath}/pecas/excluir/${p.idPeca}">Excluir</a>
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
