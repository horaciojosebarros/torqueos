<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <title>Agendamentos</title>
</head>
<body>

<h2>Agendamentos</h2>

<p>
  <a href="${pageContext.request.contextPath}/agendamentos/novo">Novo agendamento</a>
</p>

<table border="1" cellpadding="6" cellspacing="0">
  <thead>
    <tr>
      <th>ID</th>
      <th>Responsável</th>
      <th>Início</th>
      <th>Duração (min)</th>
      <th>Status</th>
      <th>Descrição</th>
      <th>Ações</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="a" items="${agendamentos}">
      <tr>
        <td>${a.idAgendamento}</td>
        <td>${a.usuarioResponsavel != null ? a.usuarioResponsavel.nome : '-'}</td>
        <td>${a.inicio}</td>
        <td>${a.duracaoMinutos}</td>
        <td>${a.status}</td>
        <td>${a.descricao}</td>
        <td>
          <a href="${pageContext.request.contextPath}/agendamentos/ver/${a.idAgendamento}">Ver</a> |
          <a href="${pageContext.request.contextPath}/agendamentos/editar/${a.idAgendamento}">Editar</a> |
          <a href="${pageContext.request.contextPath}/agendamentos/excluir/${a.idAgendamento}"
             onclick="return confirm('Excluir agendamento?');">Excluir</a>
        </td>
      </tr>
    </c:forEach>
    <c:if test="${empty agendamentos}">
      <tr><td colspan="7">Nenhum agendamento</td></tr>
    </c:if>
  </tbody>
</table>

</body>
</html>
