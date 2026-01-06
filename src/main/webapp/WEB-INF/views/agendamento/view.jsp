<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
  <title>Agendamento</title>
</head>
<body>

<h2>Agendamento #${agendamento.idAgendamento}</h2>

<p><b>Responsável:</b> ${agendamento.usuarioResponsavel != null ? agendamento.usuarioResponsavel.nome : '-'}</p>
<p><b>Início:</b> ${agendamento.inicio}</p>
<p><b>Duração:</b> ${agendamento.duracaoMinutos} min</p>
<p><b>Status:</b> ${agendamento.status}</p>
<p><b>Descrição:</b><br/> ${agendamento.descricao}</p>

<p>
  <a href="${pageContext.request.contextPath}/agendamentos/editar/${agendamento.idAgendamento}">Editar</a> |
  <a href="${pageContext.request.contextPath}/agendamentos">Voltar</a>
</p>

</body>
</html>
