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

<h2 class="mb-3">Empresas (SUPERADMIN)</h2><p class="text-muted">Escolha uma empresa para operar no contexto dela.</p>
<p>Selecione uma empresa para operar o sistema no contexto dela.</p>

<form class="d-flex gap-2 align-items-end" method="post" action="${pageContext.request.contextPath}/admin/empresas/usar">
  <select class="form-select" name="empresaId">
    <c:forEach var="e" items="${empresas}">
      <option value="${e.idEmpresa}">#${e.idEmpresa} - ${e.nome} (${e.status})</option>
    </c:forEach>
  </select>
  <button type="submit" class="btn btn-primary ms-2">Usar empresa</button>
</form>

<p style="margin-top:10px;">
  <a href="${pageContext.request.contextPath}/admin/empresas/limpar">Limpar empresa selecionada</a>
</p>

</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
