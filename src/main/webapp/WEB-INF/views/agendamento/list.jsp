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

        <div class="d-flex justify-content-between align-items-center mb-3">
          <h2 class="mb-0">Agendamentos</h2>
          <a class="btn btn-primary" href="${pageContext.request.contextPath}/agendamentos/novo">Novo agendamento</a>
        </div>

        <div class="table-responsive">
          <table class="table table-sm table-striped align-middle">
            <thead>
              <tr>
                <th style="width:80px;">ID</th>
                <th style="width:220px;">Início</th>
                <th style="width:120px;">Duração</th>
                <th style="width:150px;">Status</th>
                <th>Descrição</th>
                <th style="width:210px;">Ações</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="a" items="${agendamentos}">
                <tr>
                  <td>${a.idAgendamento}</td>
                  <td>${a.inicio}</td>
                  <td>${a.duracaoMinutos} min</td>
                  <td>${a.status}</td>
                  <td>${a.descricao}</td>
                  <td class="text-nowrap">
                    <a class="btn btn-outline-secondary btn-sm"
                       href="${pageContext.request.contextPath}/agendamentos/ver/${a.idAgendamento}">Ver</a>
                    <a class="btn btn-outline-primary btn-sm"
                       href="${pageContext.request.contextPath}/agendamentos/editar/${a.idAgendamento}">Editar</a>
                    <a class="btn btn-outline-danger btn-sm"
                       href="${pageContext.request.contextPath}/agendamentos/excluir/${a.idAgendamento}"
                       onclick="return confirm('Excluir agendamento?');">Excluir</a>
                  </td>
                </tr>
              </c:forEach>

              <c:if test="${empty agendamentos}">
                <tr>
                  <td colspan="7" class="text-center text-muted py-4">Nenhum agendamento</td>
                </tr>
              </c:if>
            </tbody>
          </table>
        </div>

      </div>
    </div>
  </div>

  <%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
