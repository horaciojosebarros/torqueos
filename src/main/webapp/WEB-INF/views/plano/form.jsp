<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html><head>
<title>TorqueOS</title>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<div class="container py-4">
<div class="card app-card">
<div class="card-body">

<h2 class="mb-3">${plano.idPlano == null ? 'Novo Plano' : 'Editar Plano'}</h2>

<form:form method="POST" modelAttribute="plano" action="${pageContext.request.contextPath}/planos/salvar" cssClass="needs-validation" novalidate="novalidate">
  <form:hidden path="idPlano"/>
  <div class="row g-3">
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Nome</label><form:input path="nome" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Preço Mensal</label><form:input path="precoMensal" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Limite Usuários</label><form:input path="limiteUsuarios" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Limite OS/Mês</label><form:input path="limiteOsMes" cssClass="form-control" required="required" /></div>
</div>
</div>
<div>Ativo: <form:checkbox path="ativo"/></div>

  <div style="margin-top:10px;">
    <div class="d-flex gap-2 mt-3"><button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/planos">Voltar</a>
  </div>
</div>
</form:form>

</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body></html>
