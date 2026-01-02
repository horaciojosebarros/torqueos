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

<h2 class="mb-3">${pagamento.idPagamento == null ? 'Novo Pagamento' : 'Editar Pagamento'}</h2>

<form:form method="POST" modelAttribute="pagamento" action="${pageContext.request.contextPath}/pagamentos/salvar" cssClass="needs-validation" novalidate="novalidate">
  <form:hidden path="idPagamento"/>
  <div class="row g-3">
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Valor</label><form:input path="valor" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Status</label><form:input path="status" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Referência</label><form:input path="referencia" cssClass="form-control" required="required" /></div>
</div>
<div class="col-md-6">
<div class="mb-3"><label class="form-label">Data (opcional)</label><form:input path="dataPagamento" cssClass="form-control" required="required" /></div>
</div>
</div>
<div style="margin-top:10px;">
    <div class="d-flex gap-2 mt-3"><button type="submit" class="btn btn-primary">Salvar</button>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/pagamentos">Voltar</a>
  </div>
</div>
</form:form>

<p style="margin-top:10px;">
  Dica: para demo, deixe "status" como PAGO e a data em branco.
</p>

</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body></html>
