<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <title>TorqueOS</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf" %>

  <style>
    @media print {
      .no-print { display: none !important; }
      .card { border: none !important; box-shadow: none !important; }
      .table { font-size: 12px; }
    }
    .kpi {
      border: 1px solid rgba(0,0,0,.08);
      border-radius: 12px;
      padding: 12px 14px;
      background: rgba(0,0,0,.02);
      min-width: 180px;
    }
    .kpi .label { font-size: 12px; opacity: .75; }
    .kpi .value { font-size: 18px; font-weight: 700; }
    .mono { font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace; }
  </style>
</head>

<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf" %>

<div class="container py-4">
  <div class="card app-card">
    <div class="card-body">

      <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h2 class="mb-0">Recebimentos (Ordens de Serviço)</h2>
        </div>

        <div class="d-flex gap-2 no-print">
          <a class="btn btn-primary" href="${pageContext.request.contextPath}/recebimentos/novo">Novo Recebimento</a>
          <button type="button" class="btn btn-outline-secondary" id="btnPrint">Exportar PDF</button>
          <button type="button" class="btn btn-outline-secondary" id="btnCsv">CSV</button>
          <button type="button" class="btn btn-outline-secondary" id="btnXls">Excel</button>
        </div>
      </div>

      <!-- ===================== FILTROS (SEM BACKEND) ===================== -->
      <div class="row g-3 align-items-end mb-3 no-print">
        <div class="col-md-3">
          <label class="form-label">Status</label>
          <select class="form-select" id="fStatus">
            <option value="">Todos</option>
            <option value="PENDENTE">PENDENTE</option>
            <option value="PAGO">PAGO</option>
            <option value="CANCELADO">CANCELADO</option>
            <option value="ESTORNADO">ESTORNADO</option>
          </select>
        </div>

        <div class="col-md-3">
          <label class="form-label">Vencimento de</label>
          <input type="date" class="form-control" id="fVencIni" />
        </div>

        <div class="col-md-3">
          <label class="form-label">Vencimento até</label>
          <input type="date" class="form-control" id="fVencFim" />
        </div>

        <div class="col-md-3">
          <div class="d-flex gap-2">
            <button type="button" class="btn btn-outline-primary w-50" id="btnAplicar">Aplicar</button>
            <button type="button" class="btn btn-outline-secondary w-50" id="btnLimpar">Limpar</button>
          </div>
        </div>
      </div>

      <!-- ===================== KPIs (FILTRADOS) ===================== -->
      <div class="d-flex flex-wrap gap-2 mb-3">
        <div class="kpi">
          <div class="label">Total filtrado</div>
          <div class="value">R$ <span id="kpiTotal" class="mono">0,00</span></div>
        </div>
        <div class="kpi">
          <div class="label">Pendentes</div>
          <div class="value"><span id="kpiPend" class="mono">0</span></div>
        </div>
        <div class="kpi">
          <div class="label">Pagos</div>
          <div class="value"><span id="kpiPago" class="mono">0</span></div>
        </div>
        <div class="kpi">
          <div class="label">Cancelados</div>
          <div class="value"><span id="kpiCanc" class="mono">0</span></div>
        </div>
        <div class="kpi">
          <div class="label">Estornados</div>
          <div class="value"><span id="kpiEst" class="mono">0</span></div>
        </div>
      </div>

      <!-- ===================== TABELA ===================== -->
      <div class="table-responsive">
        <table class="table table-hover table-striped align-middle app-table" id="tblReceb">
          <thead>
          <tr>
            <th>ID</th>
            <th>OS</th>
            <th>Cliente</th>
            <th>Vencimento</th>
            <th>Pagamento</th>
            <th>Status</th>
            <th>Tipo</th>
            <th class="text-end">Valor Final (R$)</th>
            <th style="width: 170px;" class="no-print">Ações</th>
          </tr>
          </thead>

          <tbody>
          <c:forEach var="r" items="${recebimentos}">
            <tr class="row-receb"
                data-status="${r.status}"
                data-venc="${r.dataVencimento}"
                data-pag="${r.dataPagamento}"
                data-valor="${r.valorTotal}">

              <td>#${r.idRecebimento}</td>

              <td>
                <c:if test="${r.ordemServico != null}">
                  #${r.ordemServico.idOs}
                </c:if>
              </td>

              <td>
                <c:if test="${r.ordemServico != null && r.ordemServico.cliente != null}">
                  ${r.ordemServico.cliente.nome}
                </c:if>
              </td>

              <td>
                <span class="dt-venc" data-iso="${r.dataVencimento}">
                  <c:out value="${r.dataVencimento != null ? r.dataVencimento : '-'}"/>
                </span>
              </td>

              <td>
                <span class="dt-pag" data-iso="${r.dataPagamento}">
                  <c:out value="${r.dataPagamento != null ? r.dataPagamento : '-'}"/>
                </span>
              </td>

              <td>
                <c:choose>
                  <c:when test="${r.status == 'PAGO'}">
                    <span class="badge bg-success">PAGO</span>
                  </c:when>
                  <c:when test="${r.status == 'PENDENTE'}">
                    <span class="badge bg-warning text-dark">PENDENTE</span>
                  </c:when>
                  <c:when test="${r.status == 'CANCELADO'}">
                    <span class="badge bg-secondary">CANCELADO</span>
                  </c:when>
                  <c:when test="${r.status == 'ESTORNADO'}">
                    <span class="badge bg-info text-dark">ESTORNADO</span>
                  </c:when>
                  <c:otherwise>
                    <span class="badge bg-light text-dark">${r.status}</span>
                  </c:otherwise>
                </c:choose>
              </td>

              <td><c:out value="${r.tipoPagamento}"/></td>

              <td class="text-end mono">
                <span class="money"><c:out value="${r.valorTotal}"/></span>
              </td>

              <td class="no-print">
                <a class="btn btn-sm btn-outline-primary"
                   href="${pageContext.request.contextPath}/recebimentos/editar/${r.idRecebimento}">
                  Editar
                </a>

                <c:if test="${sessionScope.ROLE == 'ADMIN' || sessionScope.ROLE == 'SUPERADMIN'}">
                  <a class="btn btn-sm btn-outline-danger"
                     href="${pageContext.request.contextPath}/recebimentos/excluir/${r.idRecebimento}"
                     onclick="return confirm('Deseja realmente excluir este recebimento?');">
                    Excluir
                  </a>
                </c:if>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty recebimentos}">
            <tr>
              <td colspan="9" class="text-center text-muted py-4">
                Nenhum recebimento registrado.
              </td>
            </tr>
          </c:if>

          </tbody>
        </table>
      </div>

      <!-- Rodapé de totais (filtrados) -->
      <div class="mt-3 text-muted small">
        <span class="mono">Dica:</span> use os filtros acima para gerar relatórios e exportar CSV/Excel.
      </div>

    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>

<script>
(function(){
  const rows = Array.from(document.querySelectorAll("tr.row-receb"));

  const fStatus = document.getElementById("fStatus");
  const fVencIni = document.getElementById("fVencIni");
  const fVencFim = document.getElementById("fVencFim");
  const btnAplicar = document.getElementById("btnAplicar");
  const btnLimpar = document.getElementById("btnLimpar");

  const kpiTotal = document.getElementById("kpiTotal");
  const kpiPend  = document.getElementById("kpiPend");
  const kpiPago  = document.getElementById("kpiPago");
  const kpiCanc  = document.getElementById("kpiCanc");
  const kpiEst   = document.getElementById("kpiEst");

  const btnPrint = document.getElementById("btnPrint");
  const btnCsv   = document.getElementById("btnCsv");
  const btnXls   = document.getElementById("btnXls");

  function parseIsoDate(iso){
    // LocalDate vem "YYYY-MM-DD"
    if(!iso || iso === "-" || iso === "null") return null;
    const m = String(iso).match(/^(\d{4})-(\d{2})-(\d{2})/);
    if(!m) return null;
    return new Date(Number(m[1]), Number(m[2])-1, Number(m[3]));
  }

  function parseMoney(v){
    if(v == null) return 0;
    const s = String(v).trim()
      .replace(/\./g, "")      // remove separador milhar (se vier)
      .replace(",", ".")       // troca decimal BR -> ponto
      .replace(/[^0-9.\-]/g, "");
    const n = parseFloat(s);
    return isNaN(n) ? 0 : n;
  }

  function fmtBR(n){
    try {
      return (n || 0).toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    } catch(e){
      const x = Math.round((n || 0) * 100) / 100;
      return x.toFixed(2).replace(".", ",");
    }
  }

  function fmtDateBR(iso){
    const d = parseIsoDate(iso);
    if(!d) return "-";
    const dd = String(d.getDate()).padStart(2,"0");
    const mm = String(d.getMonth()+1).padStart(2,"0");
    const yyyy = d.getFullYear();
    return dd + "/" + mm + "/" + yyyy;
  }

  // Formata datas exibidas (robusto p/ LocalDate/LocalDateTime em string)
  document.querySelectorAll(".dt-venc").forEach(el => {
    const iso = el.getAttribute("data-iso");
    el.textContent = fmtDateBR(iso);
  });
  document.querySelectorAll(".dt-pag").forEach(el => {
    const iso = el.getAttribute("data-iso");
    // se vier LocalDateTime "YYYY-MM-DDTHH:mm..." ou "YYYY-MM-DD ..."
    if(!iso || iso === "-" || iso === "null") { el.textContent = "-"; return; }
    const parts = String(iso).split("T");
    const datePart = parts[0];
    let timePart = (parts.length > 1 ? parts[1] : "");
    if(timePart) timePart = timePart.substring(0,5);
    el.textContent = fmtDateBR(datePart) + (timePart ? (" " + timePart) : "");
  });

  // Formata dinheiro na tabela
  document.querySelectorAll("span.money").forEach(el => {
    const n = parseMoney(el.textContent);
    el.textContent = fmtBR(n);
  });

  function aplicarFiltros(){
    const st = (fStatus.value || "").trim();
    const ini = fVencIni.value ? parseIsoDate(fVencIni.value) : null;
    const fim = fVencFim.value ? parseIsoDate(fVencFim.value) : null;

    let total = 0;
    let cPend = 0, cPago = 0, cCanc = 0, cEst = 0;

    rows.forEach(tr => {
      const rowStatus = (tr.getAttribute("data-status") || "").trim();
      const vencIso   = (tr.getAttribute("data-venc") || "").trim();
      const vencDate  = parseIsoDate(vencIso);
      const valor     = parseMoney(tr.getAttribute("data-valor"));

      let ok = true;

      if(st && rowStatus !== st) ok = false;

      // filtro por vencimento: só filtra se tem vencimento; se não tiver, não passa no range quando range estiver setado
      if(ok && (ini || fim)){
        if(!vencDate) ok = false;
        if(ok && ini && vencDate < ini) ok = false;
        if(ok && fim && vencDate > fim) ok = false;
      }

      tr.style.display = ok ? "" : "none";

      if(ok){
        total += valor;

        if(rowStatus === "PENDENTE") cPend++;
        else if(rowStatus === "PAGO") cPago++;
        else if(rowStatus === "CANCELADO") cCanc++;
        else if(rowStatus === "ESTORNADO") cEst++;
      }
    });

    kpiTotal.textContent = fmtBR(total);
    kpiPend.textContent = cPend;
    kpiPago.textContent = cPago;
    kpiCanc.textContent = cCanc;
    kpiEst.textContent  = cEst;
  }

  function limparFiltros(){
    fStatus.value = "";
    fVencIni.value = "";
    fVencFim.value = "";
    aplicarFiltros();
  }

  // ===================== EXPORTS (SEM BACKEND) =====================
  function getVisibleRows(){
    return rows.filter(tr => tr.style.display !== "none");
  }

  function exportCsv(){
    const vis = getVisibleRows();

    const header = ["ID","OS","Cliente","Vencimento","Pagamento","Status","Tipo","ValorFinal"];
    const lines = [header.join(";")];

    vis.forEach(tr => {
      const tds = tr.querySelectorAll("td");
      const id = (tds[0]?.innerText || "").trim();
      const os = (tds[1]?.innerText || "").trim();
      const cli = (tds[2]?.innerText || "").trim();
      const venc = (tds[3]?.innerText || "").trim();
      const pag = (tds[4]?.innerText || "").trim();
      const status = (tds[5]?.innerText || "").trim();
      const tipo = (tds[6]?.innerText || "").trim();
      const valor = (tds[7]?.innerText || "").trim();

      const row = [id, os, cli, venc, pag, status, tipo, valor].map(v => {
        v = (v || "").replace(/\r?\n/g, " ").trim();
        if(v.includes(";") || v.includes("\"")) v = "\"" + v.replace(/"/g, "\"\"") + "\"";
        return v;
      });

      lines.push(row.join(";"));
    });

    const blob = new Blob(["\ufeff" + lines.join("\n")], {type: "text/csv;charset=utf-8;"});
    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob);
    a.download = "recebimentos.csv";
    document.body.appendChild(a);
    a.click();
    a.remove();
  }

  function exportXls(){
    // Excel simples via HTML table
    const vis = getVisibleRows();
    const table = document.getElementById("tblReceb").cloneNode(true);

    // remove colunas no-print da cópia
    table.querySelectorAll(".no-print").forEach(el => el.remove());

    // remove linhas escondidas
    Array.from(table.querySelectorAll("tbody tr")).forEach((tr, i) => {
      // mesma ordem, então usa visibilidade da tabela original
      const original = rows[i];
      if(original && original.style.display === "none"){
        tr.remove();
      }
    });

    const html =
      "<html><head><meta charset='UTF-8'></head><body>" +
      "<h3>Recebimentos (OS)</h3>" +
      "<div>Total filtrado: R$ " + kpiTotal.textContent + "</div><br/>" +
      table.outerHTML +
      "</body></html>";

    const blob = new Blob([html], {type: "application/vnd.ms-excel;charset=utf-8;"});
    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob);
    a.download = "recebimentos.xls";
    document.body.appendChild(a);
    a.click();
    a.remove();
  }

  // eventos
  btnAplicar && btnAplicar.addEventListener("click", aplicarFiltros);
  btnLimpar && btnLimpar.addEventListener("click", limparFiltros);
  fStatus && fStatus.addEventListener("change", aplicarFiltros);
  fVencIni && fVencIni.addEventListener("change", aplicarFiltros);
  fVencFim && fVencFim.addEventListener("change", aplicarFiltros);

  btnPrint && btnPrint.addEventListener("click", () => window.print());
  btnCsv && btnCsv.addEventListener("click", exportCsv);
  btnXls && btnXls.addEventListener("click", exportXls);

  // inicial
  aplicarFiltros();
})();
</script>

</body>
</html>
