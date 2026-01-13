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

      <div class="page-title mb-3 d-flex justify-content-between align-items-center">
        <h2 class="mb-0">Recebimentos (OS)</h2>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/recebimentos/novo">Novo</a>
      </div>

      <div class="table-responsive">
        <table class="table table-striped table-hover app-table">
          <thead>
          <tr>
            <th>ID</th>
            <th>OS</th>
            <th>Venc.</th>
            <th>Pago em</th>
            <th>Status</th>
            <th>Tipo</th>
            <th>Valor</th>
            <th>Ações</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="r" items="${recebimentos}">
            <tr>
              <td>${r.idRecebimento}</td>
              <td>${r.ordemServico != null ? r.ordemServico.idOs : ''}</td>
              <td>${r.dataVencimento}</td>
              <td>${r.dataPagamento}</td>
              <td>${r.status}</td>
              <td>${r.tipoPagamento}</td>
              <td>${r.valorTotal}</td>
              <td>
                <a class="btn btn-sm btn-outline-primary"
                   href="${pageContext.request.contextPath}/recebimentos/editar/${r.idRecebimento}">Editar</a>
                <a class="btn btn-sm btn-outline-danger"
                   href="${pageContext.request.contextPath}/recebimentos/excluir/${r.idRecebimento}">Excluir</a>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>

    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
