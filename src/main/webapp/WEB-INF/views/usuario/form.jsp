<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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

<h2 class="mb-3">${usuario.idUsuario == null ? 'Novo Usuário' : 'Editar Usuário'}</h2>

<form:form method="POST" modelAttribute="usuario" action="${pageContext.request.contextPath}/usuarios/salvar" cssClass="needs-validation" novalidate="novalidate">
  <form:hidden path="idUsuario"/>

  <div class="mb-3"><label class="form-label">Nome</label><form:input path="nome" cssClass="form-control" required="required" /></div>
  <div class="mb-3"><label class="form-label">Email</label><form:input path="email" cssClass="form-control" required="required" /></div>

  <div>
    Role:
    <form:select path="role">
      <form:option value="ADMIN">ADMIN</form:option>
      <form:option value="OPERADOR">OPERADOR</form:option>
    </form:select>
  </div>

  <div>
    Ativo:
    <form:select path="ativo">
      <form:option value="true">true</form:option>
      <form:option value="false">false</form:option>
    </form:select>
  </div>

  <div>Senha (opcional): <input type="password" name="senha"/></div>

  <div style="margin-top:10px;">
    <div class="d-flex gap-2 mt-3"><button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/usuarios">Voltar</a>
  </div>
</div>
</form:form>

</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body></html>
