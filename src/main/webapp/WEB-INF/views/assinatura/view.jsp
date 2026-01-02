<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html><head>
<title>TorqueOS</title>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>
<div class="container py-4">
<div class="card app-card">
<div class="card-body">

<h2 class="mb-3">Assinatura da Empresa #${sessionScope.EMPRESA_ID}</h2>

<p>
  <b>Status:</b> ${assinatura.status}<br/>
  <b>Plano atual:</b> ${assinatura.plano.nome} (R$ ${assinatura.plano.precoMensal})<br/>
  <b>Limite usuários:</b> ${assinatura.plano.limiteUsuarios}<br/>
  <b>Limite OS/mês:</b> ${assinatura.plano.limiteOsMes}<br/>
  <b>Início:</b> ${assinatura.dataInicio}<br/>
  <b>Próximo ciclo:</b> ${assinatura.proximoCiclo}<br/>
  <b>Gateway ref:</b> ${assinatura.gatewayRef}<br/>
</p>

<hr/>
<h3>Trocar plano (demo)</h3>
<form method="post" action="${pageContext.request.contextPath}/assinatura/alterar-plano">
  <select name="planoId">
    <c:forEach var="p" items="${planos}">
      <option value="${p.idPlano}" <c:if test="${assinatura.plano.idPlano == p.idPlano}">selected</c:if>>
        ${p.nome} - R$ ${p.precoMensal} (users=${p.limiteUsuarios}, os=${p.limiteOsMes})
      </option>
    </c:forEach>
  </select>
  <button type="submit">Alterar</button>
</form>

<p style="margin-top:10px;">
  Observação: em produção, você travaria a troca de plano num fluxo de cobrança (Stripe/Mercado Pago) e só efetivaria após pagamento.
</p>

</div>
</div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body></html>
