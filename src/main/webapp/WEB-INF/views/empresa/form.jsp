<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <title>TorqueOS</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf"%>
</head>

<body>
  <%@ include file="/WEB-INF/views/fragments/header.jspf"%>

  <div class="container py-4">
    <div class="card app-card">
      <div class="card-body">

        <h2 class="mb-3">Minha Empresa</h2>

        <c:if test="${not empty msg}">
          <div class="alert alert-success">${msg}</div>
        </c:if>

        <form:form method="POST" modelAttribute="empresa"
                   action="${pageContext.request.contextPath}/empresa/salvar"
                   cssClass="needs-validation" novalidate="novalidate">

          <form:hidden path="idEmpresa"/>

          <div class="row g-3">

            <div class="col-12 col-md-8">
              <label class="form-label">Nome</label>
              <form:input path="nome" cssClass="form-control" maxlength="60" required="required"/>
              <div class="invalid-feedback">Informe o nome da empresa.</div>
            </div>

            <div class="col-12 col-md-4">
              <label class="form-label">Status</label>
              <!-- somente leitura -->
              <input class="form-control" value="${empresa.status}" readonly />
            </div>

            <div class="col-12 col-md-4">
              <label class="form-label">CNPJ</label>
              <form:input path="cnpj" cssClass="form-control" maxlength="20" placeholder="00.000.000/0000-00"/>
            </div>

            <div class="col-12 col-md-4">
              <label class="form-label">Telefone</label>
              <form:input path="telefone" cssClass="form-control" maxlength="20" placeholder="(xx) xxxxx-xxxx"/>
            </div>

            <div class="col-12 col-md-4">
              <label class="form-label">UF</label>
              <form:select path="uf" cssClass="form-select">
                <form:option value="">-- selecione --</form:option>

                <form:option value="AC">AC</form:option>
                <form:option value="AL">AL</form:option>
                <form:option value="AP">AP</form:option>
                <form:option value="AM">AM</form:option>
                <form:option value="BA">BA</form:option>
                <form:option value="CE">CE</form:option>
                <form:option value="DF">DF</form:option>
                <form:option value="ES">ES</form:option>
                <form:option value="GO">GO</form:option>
                <form:option value="MA">MA</form:option>
                <form:option value="MT">MT</form:option>
                <form:option value="MS">MS</form:option>
                <form:option value="MG">MG</form:option>
                <form:option value="PA">PA</form:option>
                <form:option value="PB">PB</form:option>
                <form:option value="PR">PR</form:option>
                <form:option value="PE">PE</form:option>
                <form:option value="PI">PI</form:option>
                <form:option value="RJ">RJ</form:option>
                <form:option value="RN">RN</form:option>
                <form:option value="RS">RS</form:option>
                <form:option value="RO">RO</form:option>
                <form:option value="RR">RR</form:option>
                <form:option value="SC">SC</form:option>
                <form:option value="SP">SP</form:option>
                <form:option value="SE">SE</form:option>
                <form:option value="TO">TO</form:option>

              </form:select>
            </div>

            <div class="col-12 col-md-6">
              <label class="form-label">Cidade</label>
              <form:input path="cidade" cssClass="form-control" maxlength="120"/>
            </div>

            <div class="col-12 col-md-6">
              <label class="form-label">Bairro</label>
              <form:input path="bairro" cssClass="form-control" maxlength="120"/>
            </div>

            <div class="col-12">
              <label class="form-label">Endereço</label>
              <form:input path="endereco" cssClass="form-control" maxlength="200"
                          placeholder="Rua, número, complemento"/>
            </div>

          </div>

          <hr class="my-4"/>

          <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/">Voltar</a>
          </div>

        </form:form>

      </div>
    </div>
  </div>

  <%@ include file="/WEB-INF/views/fragments/footer.jspf"%>

  <script>
    // bootstrap validation (client-side)
    (() => {
      'use strict';
      const forms = document.querySelectorAll('.needs-validation');
      Array.prototype.slice.call(forms).forEach((form) => {
        form.addEventListener('submit', (event) => {
          if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
          }
          form.classList.add('was-validated');
        }, false);
      });
    })();
  </script>

</body>
</html>
