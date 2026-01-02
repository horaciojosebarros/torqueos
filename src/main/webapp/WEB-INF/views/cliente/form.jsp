<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
<h2 class="mb-3">${cliente.idCliente == null ? 'Novo Cliente' : 'Editar Cliente'}</h2>

<form:form method="POST" modelAttribute="cliente" action="${pageContext.request.contextPath}/clientes/salvar" cssClass="needs-validation" novalidate="novalidate">
  <form:hidden path="idCliente"/>
  <div class="row g-3">
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Nome</label><form:input path="nome" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Telefone</label><form:input path="telefone" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Email</label><form:input path="email" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Endereço</label><form:input path="endereco" cssClass="form-control" required="required" /></div>
</div>
</div>
<div style="margin-top:10px;">
    <div class="d-flex gap-2 mt-3"><button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/clientes">Voltar</a>
  </div>
</div>
</form:form>

</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
