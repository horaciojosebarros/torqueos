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

      <h2 class="mb-3">OS #${ordem.idOs}</h2>

      <div class="row g-3 mb-3">
        <div class="col-md-4">
          <div class="small text-muted">Cliente</div>
          <div>${ordem.cliente != null ? ordem.cliente.nome : '-'}</div>
        </div>
        <div class="col-md-4">
          <div class="small text-muted">Veículo</div>
          <div>${ordem.veiculo != null ? ordem.veiculo.placa : '-'} - ${ordem.veiculo != null ? ordem.veiculo.modelo : ''}</div>
        </div>
        <div class="col-md-4">
          <div class="small text-muted">Status</div>
          <div>${ordem.status}</div>
        </div>
      </div>

      <div class="mb-3">
        <div class="small text-muted">Observações</div>
        <div style="white-space: pre-wrap;">${ordem.observacoes}</div>
      </div>

      <hr class="my-4"/>

      <h5 class="mb-3">Serviços</h5>
      <div class="table-responsive">
        <table class="table table-sm table-striped align-middle">
          <thead>
            <tr>
              <th>Descrição</th>
              <th style="width:160px;">Valor</th>
            </tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${not empty servicos}">
                <c:forEach var="s" items="${servicos}">
                  <tr>
                    <td>${s.descricao}</td>
                    <td>R$ ${s.valor}</td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr><td colspan="2" class="text-muted">Nenhum serviço</td></tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>

      <hr class="my-4"/>

      <h5 class="mb-3">Peças</h5>
      <div class="table-responsive">
        <table class="table table-sm table-striped align-middle">
          <thead>
            <tr>
              <th>Descrição</th>
              <th style="width:110px;">Qtd</th>
              <th style="width:160px;">Vlr Unit</th>
              <th style="width:160px;">Subtotal</th>
            </tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${not empty pecas}">
                <c:forEach var="p" items="${pecas}">
                  <tr>
                    <td>${p.descricao}</td>
                    <td>${p.quantidade}</td>
                    <td>R$ ${p.valorUnitario}</td>
                    <td>R$ ${p.valorUnitario * p.quantidade}</td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr><td colspan="4" class="text-muted">Nenhuma peça</td></tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>

      <div class="d-flex gap-2 mt-3">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/ordens/editar/${ordem.idOs}">Editar</a>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/ordens">Voltar</a>
      </div>

    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
