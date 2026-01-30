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

      <h2 class="mb-3">${tecnico.idTecnico == null ? 'Novo Técnico' : 'Editar Técnico'}</h2>

      <form:form method="POST" modelAttribute="tecnico"
                 action="${pageContext.request.contextPath}/tecnicos/salvar"
                 cssClass="needs-validation" novalidate="novalidate">

        <form:hidden path="idTecnico"/>

        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Nome</label>
            <form:input path="nome" cssClass="form-control" maxlength="120" required="required"/>
          </div>

          <div class="col-md-3">
            <label class="form-label">Tipo</label>
            <form:select path="tipo" cssClass="form-select" required="required">
              <form:option value="MECANICO">MECÂNICO</form:option>
              <form:option value="AUXILIAR">AUXILIAR</form:option>
            </form:select>
          </div>

          <div class="col-md-3">
            <label class="form-label">Comissão por OS (%)</label>
            <form:input path="percentualComissao" cssClass="form-control" type="number"
                        min="0" max="100" step="1" required="required"/>
          </div>

          <div class="col-md-3">
            <label class="form-label">Telefone</label>
            <form:input path="telefone" cssClass="form-control" maxlength="20"/>
          </div>

          <div class="col-md-5">
            <label class="form-label">E-mail</label>
            <form:input path="email" cssClass="form-control" maxlength="120"/>
          </div>

          <div class="col-md-4">
            <label class="form-label">CPF</label>
            <form:input path="cpf" cssClass="form-control" maxlength="14"/>
          </div>

          <div class="col-md-3">
            <label class="form-label">Ativo</label>
            <form:select path="ativo" cssClass="form-select">
              <form:option value="true">SIM</form:option>
              <form:option value="false">NÃO</form:option>
            </form:select>
          </div>

          <div class="col-12">
            <label class="form-label">Observação</label>
            <form:textarea path="observacao" cssClass="form-control" rows="3" maxlength="500"/>
          </div>
        </div>

        <div class="d-flex gap-2 mt-4">
          <button type="submit" class="btn btn-primary">Salvar</button>
          <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/tecnicos">Voltar</a>
          <c:if test="${tecnico.idTecnico != null}">
            <a class="btn btn-outline-secondary"
               href="${pageContext.request.contextPath}/tecnicos/ver/${tecnico.idTecnico}">Ver</a>
          </c:if>
        </div>

      </form:form>

    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
