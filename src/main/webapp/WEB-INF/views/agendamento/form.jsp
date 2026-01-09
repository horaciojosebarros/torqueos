<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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

        <h2 class="mb-3">${agendamento.idAgendamento == null ? 'Novo Agendamento' : 'Editar Agendamento'}</h2>

        <form:form method="POST" modelAttribute="agendamento"
                   action="${pageContext.request.contextPath}/agendamentos/salvar"
                   cssClass="needs-validation" novalidate="novalidate">

          <form:hidden path="idAgendamento"/>

          <!-- Cliente / Veículo (texto) -->
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Nome do cliente</label>
              <input class="form-control" name="clienteNome" value="${clienteNome}" />
            </div>

            <div class="col-md-3">
              <label class="form-label">Placa</label>
              <input class="form-control" name="placa" value="${placa}" />
            </div>

            <div class="col-md-3">
              <label class="form-label">Marca</label>
              <input class="form-control" name="marca" value="${marca}" />
            </div>

            <div class="col-md-6">
              <label class="form-label">Modelo</label>
              <input class="form-control" name="modelo" value="${modelo}" />
            </div>
          </div>

          <hr class="my-4"/>

          <!-- Data e hora separados (nativo, sem internet) -->
          <div class="row g-3">
            <div class="col-md-4">
              <label class="form-label">Data</label>
              <!-- ISO (yyyy-MM-dd) -->
              <input type="date" class="form-control" id="data" name="data" value="${dataIso}" required="required"/>
              <div class="form-text">Selecione a data no calendário.</div>
            </div>

            <div class="col-md-4">
              <label class="form-label">Hora</label>
              <input type="time" class="form-control" id="hora" name="hora" value="${hora}" required="required"/>
            </div>

            <div class="col-md-4">
              <label class="form-label">Duração (min)</label>
              <form:input path="duracaoMinutos" cssClass="form-control" />
            </div>

            <div class="col-md-4">
              <label class="form-label">Status</label>
              <form:select path="status" cssClass="form-select" required="required">
                <form:option value="AGENDADO">AGENDADO</form:option>
                <form:option value="CONFIRMADO">CONFIRMADO</form:option>
                <form:option value="EM_ATENDIMENTO">EM_ATENDIMENTO</form:option>
                <form:option value="CONCLUIDO">CONCLUIDO</form:option>
                <form:option value="CANCELADO">CANCELADO</form:option>
              </form:select>
            </div>

            <div class="col-12">
              <label class="form-label">Descrição</label>
              <form:textarea path="descricao" cssClass="form-control" rows="3"
                             placeholder="Ex.: troca de óleo + filtros"/>
            </div>
          </div>

          <div class="d-flex gap-2 mt-4">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/agendamentos">Voltar</a>
          </div>

        </form:form>

      </div>
    </div>
  </div>

  <!-- Impedir data/hora no passado (frontend) -->
  <script>
    (function() {
      const dataEl = document.getElementById('data');
      const horaEl = document.getElementById('hora');
      const form = document.querySelector('form');
      if (!dataEl || !horaEl || !form) return;

      function pad(n){ return String(n).padStart(2,'0'); }

      function setMinNow() {
        const now = new Date();
        const yyyy = now.getFullYear();
        const mm = pad(now.getMonth()+1);
        const dd = pad(now.getDate());
        const hh = pad(now.getHours());
        const mi = pad(now.getMinutes());

        const todayIso = `${yyyy}-${mm}-${dd}`;
        dataEl.min = todayIso;

        if (dataEl.value === todayIso) {
          horaEl.min = `${hh}:${mi}`;
        } else {
          horaEl.min = "";
        }
      }

      function isPastSelection() {
        if (!dataEl.value || !horaEl.value) return false;
        const dt = new Date(dataEl.value + "T" + horaEl.value + ":00");
        return dt.getTime() < Date.now();
      }

      dataEl.addEventListener('change', setMinNow);
      horaEl.addEventListener('focus', setMinNow);

      form.addEventListener('submit', function(e) {
        setMinNow();
        if (isPastSelection()) {
          e.preventDefault();
          alert('Não é permitido agendar no passado.');
          dataEl.focus();
        }
      });

      setMinNow();
    })();
  </script>

  <%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
