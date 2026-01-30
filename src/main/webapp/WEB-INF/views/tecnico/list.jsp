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

      <div class="page-title mb-3 d-flex justify-content-between align-items-center">
        <h2 class="mb-0">Técnicos</h2>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/tecnicos/novo">Novo Técnico</a>
      </div>

      <div class="table-responsive">
        <table class="table table-striped table-hover app-table align-middle">
          <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Tipo</th>
            <th>Comissão (%)</th>
            <th>Telefone</th>
            <th>Ativo</th>
            <th style="width: 220px;">Ações</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="t" items="${tecnicos}">
            <tr>
              <td>${t.idTecnico}</td>
              <td>${t.nome}</td>
              <td>${t.tipo}</td>
              <td>${t.percentualComissao}</td>
              <td>${t.telefone}</td>
              <td>
                <c:choose>
                  <c:when test="${t.ativo}"><span class="badge text-bg-success">SIM</span></c:when>
                  <c:otherwise><span class="badge text-bg-secondary">NÃO</span></c:otherwise>
                </c:choose>
              </td>
              <td class="d-flex gap-2">
                <a class="btn btn-sm btn-outline-secondary"
                   href="${pageContext.request.contextPath}/tecnicos/ver/${t.idTecnico}">Ver</a>
                <a class="btn btn-sm btn-outline-primary"
                   href="${pageContext.request.contextPath}/tecnicos/editar/${t.idTecnico}">Editar</a>
                <a class="btn btn-sm btn-outline-danger"
                   href="${pageContext.request.contextPath}/tecnicos/excluir/${t.idTecnico}"
                   onclick="return confirm('Excluir técnico #${t.idTecnico}?');">Excluir</a>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty tecnicos}">
            <tr><td colspan="7" class="text-center text-muted py-4">Nenhum técnico cadastrado.</td></tr>
          </c:if>
          </tbody>
        </table>
      </div>

    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
