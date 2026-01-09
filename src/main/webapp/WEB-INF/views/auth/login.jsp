<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>TorqueOS — Login</title>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body>
<div class="container py-4">
<div class="card app-card"><div class="card-body">
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="container py-5" style="max-width:520px;">
<div class="card app-card"><div class="card-body p-4"><h2 class="mb-2">TorqueOS</h2><p class="text-muted mb-4">Controle total da sua oficina</p>

<c:if test="${erro != null}">
  <div class="alert alert-danger"><b>${erro}</b></div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/auth/login">
  <div class="mb-3"><label class="form-label">Empresa ID</label><input class="form-control" name="empresaId" value="2"/></div>
  <div class="mb-3"><label class="form-label">Email</label><input class="form-control" name="email" value="horacio.jose.barros@gmail.com"/></div>
  <div class="mb-3"><label class="form-label">Senha</label><input class="form-control" name="senha" type="password" value="123"/></div>
  <div style="margin-top:10px;">
    <button type="submit" class="btn btn-primary w-100">Entrar</button>
  </div>
</form>

<hr class="my-4"/>
</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</div></div></div>
</div></div></div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
