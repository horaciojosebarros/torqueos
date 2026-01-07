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
              <input class="form-control" name="clienteNome" value="${clienteNome}"  />
            </div>

            <div class="col-md-3">
              <label class="form-label">Placa</label>
              <input class="form-control" name="placa" value="${placa}"  />
            </div>

            <div class="col-md-3">
              <label class="form-label">Marca</label>
              <input class="form-control" name="marca" value="${marca}" />
            </div>

            <div class="col-md-6">
              <label class="form-label">Modelo</label>
              <input class="form-control" name="modelo" value="${modelo}"  />
            </div>

          </div>

          <hr class="my-4"/>

          <!-- Data e hora separados com componentes nativos -->
          <div class="row g-3">
			<div class="row g-3">
			  <div class="col-md-4">
			    <label class="form-label">Data</label>
			    <input type="date" class="form-control" name="data" value="${dataIso}" required="required"/>
			  </div>

			  <div class="col-md-4">
			    <label class="form-label">Hora</label>
			    <input type="time" class="form-control" name="hora" value="${hora}" required="required"/>
			  </div>

			  <div class="col-md-4">
			    <label class="form-label">Duração (min)</label>
			    <form:input path="duracaoMinutos" cssClass="form-control" />
			  </div>
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
  <script>
    (function() {
      const el = document.getElementById('data');
      if (!el) return;

      function maskDate(v) {
        v = (v || '').replace(/\D/g, '').slice(0, 8); // só números, até 8
        if (v.length >= 5) return v.slice(0,2) + '/' + v.slice(2,4) + '/' + v.slice(4);
        if (v.length >= 3) return v.slice(0,2) + '/' + v.slice(2);
        return v;
      }

      el.addEventListener('input', function() {
        const pos = el.selectionStart;
        const old = el.value;
        el.value = maskDate(el.value);

        // tenta manter cursor num lugar "ok"
        if (el.value.length > old.length && (pos === 2 || pos === 5)) {
          el.setSelectionRange(pos + 1, pos + 1);
        }
      });

      el.addEventListener('blur', function() {
        // validação simples no blur: dd/MM/aaaa completo
        const v = el.value || '';
        const ok = /^\d{2}\/\d{2}\/\d{4}$/.test(v);
        if (!ok && v.trim().length > 0) {
          el.classList.add('is-invalid');
        } else {
          el.classList.remove('is-invalid');
        }
      });
    })();
  </script>
  
  <!-- Flatpickr (calendário) - sem jQuery -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css"/>
  <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
  <script src="https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/pt.js"></script>

  <script>
    (function () {
      const el = document.getElementById('data');
      if (!el) return;

      flatpickr(el, {
        locale: "pt",
        dateFormat: "d/m/Y",   // dd/MM/yyyy
        allowInput: true,      // permite digitar também
        disableMobile: true    // força o calendário mesmo no celular
      });
    })();
  </script>


  <%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
