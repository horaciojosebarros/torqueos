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

        <div class="page-title mb-3 d-flex align-items-center justify-content-between">
          <h2 class="mb-0">Pagamentos</h2>
          <a class="btn btn-primary" href="${pageContext.request.contextPath}/pagamentos/novo">Novo Pagamento</a>
        </div>

        <div class="table-responsive">
          <table class="table table-striped table-hover app-table align-middle">
            <thead>
              <tr>
                <th>ID</th>
                <th>OS</th>
                <th>Valor</th>
                <th>Vencimento</th>
                <th>Pagamento</th>
                <th>Tipo</th>
                <th>Status</th>
                <th>Referência</th>
                <th style="width:180px;">Ações</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="p" items="${pagamentos}">
                <tr>
                  <td>${p.idPagamento}</td>
                  <td>
                    <c:choose>
                      <c:when test="${p.ordemServico != null}">#${p.ordemServico.idOs}</c:when>
                      <c:otherwise>-</c:otherwise>
                    </c:choose>
                  </td>
                  <td>${p.valor}</td>
                  <td>${p.dataVencimento}</td>
                  <td>${p.dataPagamento}</td>
                  <td>${p.tipoPagamento}</td>
                  <td>${p.status}</td>
                  <td>${p.referencia}</td>
                  <td>
                    <a href="${pageContext.request.contextPath}/pagamentos/editar/${p.idPagamento}" class="btn btn-sm btn-outline-primary">Editar</a>
                    <a href="${pageContext.request.contextPath}/pagamentos/excluir/${p.idPagamento}" class="btn btn-sm btn-outline-danger"
                       onclick="return confirm('Confirma excluir?');">Excluir</a>
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
