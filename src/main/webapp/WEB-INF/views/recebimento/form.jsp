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

      <h2 class="mb-3">${recebimento.idRecebimento == null ? 'Novo Recebimento (OS)' : 'Editar Recebimento (OS)'}</h2>

      <form:form method="POST" modelAttribute="recebimento"
                 action="${pageContext.request.contextPath}/recebimentos/salvar"
                 cssClass="needs-validation" novalidate="novalidate">

        <form:hidden path="idRecebimento"/>

        <c:set var="bloquearOs" value="${osPago}" />

        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Ordem de Serviço</label>

            <c:if test="${bloquearOs}">
              <input type="hidden" name="osId"
                     value="${osId != null ? osId : (recebimento.ordemServico != null ? recebimento.ordemServico.idOs : '')}"/>
            </c:if>

            <select class="form-select" name="osId" id="osId" required="required"
                    <c:if test="${bloquearOs}">disabled</c:if>>
              <option value="">-- selecione --</option>
              <c:forEach var="os" items="${ordens}">
                <option value="${os.idOs}"
                  <c:if test="${(osId != null && osId == os.idOs) || (recebimento.ordemServico != null && recebimento.ordemServico.idOs == os.idOs)}">selected</c:if>>
                  #${os.idOs} - ${os.veiculo != null ? os.veiculo.placa : ''} - ${os.cliente != null ? os.cliente.nome : ''}
                </option>
              </c:forEach>
            </select>

            <c:if test="${bloquearOs}">
              <div class="small text-muted mt-1">OS bloqueada porque este recebimento está PAGO. Para trocar, exclua o recebimento.</div>
            </c:if>
          </div>

          <div class="col-md-3">
            <label class="form-label">Status</label>
            <form:select path="status" cssClass="form-select" required="required" id="status">
              <form:option value="PENDENTE">PENDENTE</form:option>
              <form:option value="PAGO">PAGO</form:option>
              <form:option value="CANCELADO">CANCELADO</form:option>
              <form:option value="ESTORNADO">ESTORNADO</form:option>
            </form:select>
          </div>

          <div class="col-md-3">
            <label class="form-label">Tipo de pagamento</label>
            <form:select path="tipoPagamento" cssClass="form-select" required="required">
              <form:option value="PIX">PIX</form:option>
              <form:option value="DINHEIRO">DINHEIRO</form:option>
              <form:option value="CARTAO_DEBITO">CARTAO_DEBITO</form:option>
              <form:option value="CARTAO_CREDITO">CARTAO_CREDITO</form:option>
              <form:option value="BOLETO">BOLETO</form:option>
              <form:option value="TRANSFERENCIA">TRANSFERENCIA</form:option>
            </form:select>
          </div>

          <div class="col-md-4">
            <label class="form-label">Total base da OS</label>
            <input class="form-control" id="osBase" value="" readonly="readonly"/>
            <div class="form-text">Soma serviços + peças (via AJAX).</div>
          </div>

          <div class="col-md-4">
            <label class="form-label">Valor final</label>
            <input class="form-control" id="valorFinalVis" value="" readonly="readonly"/>
            <!-- valorTotal persistido (backend recalcula sempre, mas mantemos pra edição / histórico) -->
            <form:input path="valorTotal" cssClass="form-control" id="valorTotal" style="display:none;"/>
          </div>

          <div class="col-md-4">
            <label class="form-label">Vencimento</label>
            <input type="date" class="form-control" name="dataVencimentoIso" value="${dataVencimentoIso}"/>
          </div>

          <div class="col-md-4">
            <label class="form-label">Data pagamento</label>
            <input type="date" class="form-control" name="dataPagamentoIso" value="${dataPagamentoIso}" />
          </div>

          <div class="col-md-2">
            <label class="form-label">Hora</label>
            <input type="time" class="form-control" name="horaPagamento" value="${horaPagamento}" />
          </div>

          <div class="col-md-3">
            <label class="form-label">Desconto</label>
            <form:input path="desconto" cssClass="form-control" id="desconto"/>
          </div>

          <div class="col-md-3">
            <label class="form-label">Juros</label>
            <form:input path="juros" cssClass="form-control" id="juros"/>
          </div>

          <div class="col-md-3">
            <label class="form-label">Multa</label>
            <form:input path="multa" cssClass="form-control" id="multa"/>
          </div>

          <div class="col-md-3">
            <label class="form-label">Parcelas</label>
            <form:input path="parcelas" cssClass="form-control"/>
          </div>

          <div class="col-md-6">
            <label class="form-label">Referência</label>
            <form:input path="referencia" cssClass="form-control"/>
          </div>

          <div class="col-md-6">
            <label class="form-label">Documento ref</label>
            <form:input path="documentoRef" cssClass="form-control"/>
          </div>

          <div class="col-12">
            <label class="form-label">Observação</label>
            <form:textarea path="observacao" cssClass="form-control" rows="3"/>
          </div>
        </div>

        <div class="d-flex gap-2 mt-4">
          <button type="submit" class="btn btn-primary">Salvar</button>
          <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/recebimentos">Voltar</a>
        </div>

      </form:form>

    </div>
  </div>
</div>

<script>
(function(){
  const ctx = "${pageContext.request.contextPath}";
  const osSelect = document.getElementById("osId");
  const osBase = document.getElementById("osBase");
  const valorTotal = document.getElementById("valorTotal");       // hidden persist
  const valorFinalVis = document.getElementById("valorFinalVis"); // readonly
  const desconto = document.getElementById("desconto");
  const juros = document.getElementById("juros");
  const multa = document.getElementById("multa");

  const bloquearOs = ${osPago ? "true" : "false"};

  function toNumber(v){
    if(!v) return 0;
    v = (""+v).replace(",", ".").replace(/[^0-9.\-]/g,'');
    const n = parseFloat(v);
    return isNaN(n) ? 0 : n;
  }
  function fmt2(n){ return (Math.round(n * 100) / 100).toFixed(2); }

  function recalcFinal(){
    const base = toNumber(valorTotal?.value);
    const finalVal = base - toNumber(desconto?.value) + toNumber(juros?.value) + toNumber(multa?.value);
    if(valorFinalVis) valorFinalVis.value = fmt2(finalVal);
  }

  async function carregarTotalDaOs(osId){
    if(!osId){
      if(osBase) osBase.value = "";
      recalcFinal();
      return;
    }

    const url = ctx + "/api/ordens/total/" + osId;
    console.log("GET:", url);

    if(osBase) osBase.value = "Carregando...";

    try{
      const resp = await fetch(url, {
        method: "GET",
        headers: { "Accept": "application/json" }
      });

      if(!resp.ok){
        if(osBase) osBase.value = "Erro HTTP " + resp.status;
        return;
      }

      const json = await resp.json();
      const totalBase = toNumber(json.totalBase);

      if(osBase) osBase.value = fmt2(totalBase);

      // Apenas para exibir e recalcular no front (backend recalcula ao salvar)
      if(valorTotal) valorTotal.value = fmt2(totalBase);

      recalcFinal();
    } catch(e){
      if(osBase) osBase.value = "Erro ao calcular";
      console.error(e);
    }
  }

  if(osSelect && !bloquearOs){
    osSelect.addEventListener("change", function(){
      carregarTotalDaOs(this.value);
    });
  }

  ["input","change"].forEach(evt => {
    desconto?.addEventListener(evt, recalcFinal);
    juros?.addEventListener(evt, recalcFinal);
    multa?.addEventListener(evt, recalcFinal);
  });

  // inicial
  if(osSelect && osSelect.value){
    if(!bloquearOs) carregarTotalDaOs(osSelect.value);
    else {
      // pago: mostra o que já está persistido
      if(osBase) osBase.value = valorTotal?.value ? fmt2(toNumber(valorTotal.value)) : "";
      recalcFinal();
    }
  } else {
    recalcFinal();
  }
})();
</script>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
