<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <title>Agendamento</title>
</head>
<body>

<h2>${agendamento.idAgendamento == null ? 'Novo Agendamento' : 'Editar Agendamento'}</h2>

<form:form method="POST" modelAttribute="agendamento"
           action="${pageContext.request.contextPath}/agendamentos/salvar">

  <form:hidden path="idAgendamento"/>

  <div>
    <label>Responsável:</label>
    <select name="usuarioId" required="required">
      <option value="">-- selecione --</option>
      <c:forEach var="u" items="${usuarios}">
        <option value="${u.idUsuario}"
          <c:if test="${agendamento.usuarioResponsavel != null && agendamento.usuarioResponsavel.idUsuario == u.idUsuario}">selected</c:if>>
          ${u.nome}
        </option>
      </c:forEach>
    </select>
  </div>

  <div>
    <label>Início (YYYY-MM-DDTHH:MM):</label>
    <form:input path="inicio" />
    <small>Ex.: 2026-01-06T14:30</small>
  </div>

  <div>
    <label>Duração (min):</label>
    <form:input path="duracaoMinutos" />
  </div>

  <div>
    <label>Status:</label>
    <form:select path="status">
      <form:option value="AGENDADO">AGENDADO</form:option>
      <form:option value="CONFIRMADO">CONFIRMADO</form:option>
      <form:option value="EM_ATENDIMENTO">EM_ATENDIMENTO</form:option>
      <form:option value="CONCLUIDO">CONCLUIDO</form:option>
      <form:option value="CANCELADO">CANCELADO</form:option>
    </form:select>
  </div>

  <div>
    <label>Descrição:</label><br/>
    <form:textarea path="descricao" rows="3" cols="60"/>
  </div>

  <p>
    <button type="submit">Salvar</button>
    <a href="${pageContext.request.contextPath}/agendamentos">Voltar</a>
  </p>

</form:form>

</body>
</html>
