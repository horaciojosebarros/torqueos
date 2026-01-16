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
        <div class="page-title mb-2">
          <h2>Bem-vindo ao TorqueOS</h2>
        </div>
        <p class="muted mb-4">Comece criando um orçamento / Ordem de Serviço ou cadastre um cliente.</p>
        <div class="d-flex gap-2 flex-wrap">
          <a class="btn btn-primary" href="${pageContext.request.contextPath}/ordens/nova">Novo Orçamento/OS</a>
          <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/clientes/novo">Novo Cliente</a>
          <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/veiculos/novo">Novo Veículo</a>
        </div>
      </div>
    </div>
  </div>
  <%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
