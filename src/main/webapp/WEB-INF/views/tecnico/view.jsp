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

      <h2 class="mb-3">Técnico #${tecnico.idTecnico}</h2>

      <div class="row g-3">
        <div class="col-md-6">
          <div class="small text-muted">Nome</div>
          <div class="fw-semibold">${tecnico.nome}</div>
        </div>

        <div class="col-md-3">
          <div class="small text-muted">Tipo</div>
          <div class="fw-semibold">${tecnico.tipo}</div>
        </div>

        <div class="col-md-3">
          <div class="small text-muted">Comissão (%)</div>
          <div class="fw-semibold">${tecnico.percentualComissao}</div>
        </div>

        <div class="col-md-4">
          <div class="small text-muted">Telefone</div>
          <div class="fw-semibold">${tecnico.telefone}</div>
        </div>

        <div class="col-md-4">
          <div class="small text-muted">E-mail</div>
          <div class="fw-semibold">${tecnico.email}</div>
        </div>

        <div class="col-md-4">
          <div class="small text-muted">CPF</div>
          <div class="fw-semibold">${tecnico.cpf}</div>
        </div>

        <div class="col-md-3">
          <div class="small text-muted">Ativo</div>
          <div class="fw-semibold">
            <c:choose>
              <c:when test="${tecnico.ativo}">SIM</c:when>
              <c:otherwise>NÃO</c:otherwise>
            </c:choose>
          </div>
        </div>

        <div class="col-12">
          <div class="small text-muted">Observação</div>
          <div class="fw-semibold">${tecnico.observacao}</div>
        </div>
      </div>

      <div class="d-flex gap-2 mt-4">
        <a class="btn btn-outline-primary"
           href="${pageContext.request.contextPath}/tecnicos/editar/${tecnico.idTecnico}">Editar</a>
        <a class="btn btn-outline-secondary"
           href="${pageContext.request.contextPath}/tecnicos">Voltar</a>
      </div>

    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
