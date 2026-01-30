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
      <h2 class="mb-3">Orçamento/Ordem de Serviço</h2>

      <p>
        <a class="btn btn-sm btn-primary" href="${pageContext.request.contextPath}/ordens/nova">Novo</a>
      </p>

      <table class="table table-striped table-hover app-table">
        <thead>
          <tr>
            <th>Número</th>
            <th>Cliente</th>
            <th>Veículo</th>
            <th>Abertura</th>
            <th>Finalização</th>
            <th>Status</th>
            <th>Ações</th>
          </tr>
        </thead>

        <tbody>
        <c:forEach var="os" items="${ordens}">
          <c:set var="paga" value="${osPagas != null && osPagas.contains(os.idOs)}" />

          <tr>
            <td>${os.idOs}</td>
            <td><c:out value="${os.cliente != null ? os.cliente.nome : '-'}"/></td>
            <td><c:out value="${os.veiculo != null ? os.veiculo.placa : '-'}"/></td>
            <td>${os.dataAbertura}</td>
            <td>${os.dataFinalizacao}</td>
            <td>${os.status}</td>

            <td class="d-flex gap-2 flex-wrap align-items-center">
              <a class="btn btn-sm btn-outline-secondary"
                 href="${pageContext.request.contextPath}/ordens/ver/${os.idOs}">Ver</a>

              <c:choose>
                <c:when test="${paga}">
                  <a class="btn btn-sm btn-outline-primary disabled" href="#" tabindex="-1" aria-disabled="true">
                    Editar
                  </a>
                  <span class="badge bg-success">PAGA</span>
                </c:when>
                <c:otherwise>
                  <a class="btn btn-sm btn-outline-primary"
                     href="${pageContext.request.contextPath}/ordens/editar/${os.idOs}">
                    Editar
                  </a>
                </c:otherwise>
              </c:choose>

              <a class="btn btn-sm btn-outline-danger"
                 href="${pageContext.request.contextPath}/ordens/excluir/${os.idOs}">
                Excluir
              </a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>

    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
