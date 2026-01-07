<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
          <h2 class="mb-0">Agendamento #${agendamento.idAgendamento}</h2>
          <div class="d-flex gap-2">
            <a class="btn btn-outline-primary"
               href="${pageContext.request.contextPath}/agendamentos/editar/${agendamento.idAgendamento}">Editar</a>
            <a class="btn btn-outline-secondary"
               href="${pageContext.request.contextPath}/agendamentos">Voltar</a>
          </div>
        </div>

        <div class="row g-3">
          <div class="col-md-6">
            <div class="mb-2"><b>Status:</b> ${agendamento.status}</div>
          </div>
          <div class="col-md-6">
            <div class="mb-2"><b>Início:</b> ${agendamento.inicio}</div>
            <div class="mb-2"><b>Duração:</b> ${agendamento.duracaoMinutos} min</div>
          </div>
        </div>

        <hr class="my-3"/>

        <div>
          <b>Descrição:</b>
          <div class="mt-2">${agendamento.descricao}</div>
        </div>

      </div>
    </div>
  </div>

  <%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
