<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html><head><title>Serviço</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<div class="container py-4">
  <div class="card app-card"><div class="card-body">
    <h2 class="mb-3">${servico.idServico == null ? 'Novo Serviço' : 'Editar Serviço'}</h2>

    <form:form method="POST" modelAttribute="servico"
               action="${pageContext.request.contextPath}/servicos/salvar">
      <form:hidden path="idServico"/>

      <div class="mb-3">
        <label class="form-label">Nome</label>
        <form:input path="nome" cssClass="form-control" required="required"/>
      </div>

      <div class="mb-3">
        <label class="form-label">Valor</label>
        <form:input path="valor" cssClass="form-control" required="required"/>
      </div>

      <div class="form-check mb-3">
        <form:checkbox path="ativo" cssClass="form-check-input" id="ativo"/>
        <label class="form-check-label" for="ativo">Ativo</label>
      </div>

      <div class="d-flex gap-2">
        <button class="btn btn-primary" type="submit">Salvar</button>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/servicos">Voltar</a>
      </div>
    </form:form>
  </div></div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body></html>
