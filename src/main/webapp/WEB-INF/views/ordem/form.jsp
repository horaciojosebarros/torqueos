<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <title>TorqueOS</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf"%>
  <style>
    /* Só pra ficar mais compacto sem “mexer em layout” de verdade */
    .app-card .card-body { padding: 1rem; }
    .table td, .table th { padding: .35rem .5rem; }
    .form-label { margin-bottom: .25rem; }
  </style>
</head>

<body>
<%@ include file="/WEB-INF/views/fragments/header.jspf"%>

<div class="container py-4">
  <div class="card app-card">
    <div class="card-body">

      <div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
        <h2 class="mb-0">${ordem.idOs == null ? 'Novo Orçamento/OS' : 'Editar Orçamento/OS'}</h2>

        <div class="d-flex gap-2">
          <button type="submit" form="osForm" class="btn btn-primary">Salvar</button>
          <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/ordens">Voltar</a>

          <c:if test="${ordem.idOs != null}">
            <a class="btn btn-outline-primary"
               href="${pageContext.request.contextPath}/ordens/pdf/${ordem.idOs}"
               target="_blank">Gerar Orçamento/OS em PDF</a>
          </c:if>
        </div>
      </div>

      <form:form id="osForm" method="POST" modelAttribute="ordem"
                 action="${pageContext.request.contextPath}/ordens/salvar"
                 cssClass="needs-validation" novalidate="novalidate">

        <form:hidden path="idOs" />

        <!-- ===== LINHA PRINCIPAL (MENOS ROLAGEM) ===== -->
        <div class="row g-3">

          <!-- Cliente -->
          <div class="col-md-5">
            <label class="form-label">Cliente</label>
            <select class="form-select" name="clienteId">
              <option value="">-- selecione --</option>
              <c:forEach var="c" items="${clientes}">
                <option value="${c.idCliente}"
                  <c:if test="${ordem.cliente != null && ordem.cliente.idCliente == c.idCliente}">selected</c:if>>
                  ${c.nome}
                </option>
              </c:forEach>
            </select>
          </div>

          <!-- Veículo -->
          <div class="col-md-4">
            <label class="form-label">Veículo</label>
            <select class="form-select" name="veiculoId">
              <option value="">-- selecione --</option>
              <c:forEach var="v" items="${veiculos}">
                <option value="${v.idVeiculo}"
                  <c:if test="${ordem.veiculo != null && ordem.veiculo.idVeiculo == v.idVeiculo}">selected</c:if>>
                  ${v.placa} - ${v.modelo}
                </option>
              </c:forEach>
            </select>
          </div>

          <!-- Status -->
          <div class="col-md-3">
            <label class="form-label">Status</label>
            <form:select path="status" cssClass="form-select" required="required">
              <form:option value="">-- selecione --</form:option>
              <form:option value="EM_ANDAMENTO">Em andamento</form:option>
              <form:option value="SUSPENSA">Suspensa</form:option>
              <form:option value="NAO_AUTORIZADA">Não autorizada</form:option>
              <form:option value="CONCLUIDA">Concluída</form:option>
            </form:select>
          </div>

          <!-- Observações (compacto) -->
          <div class="col-12">
            <label class="form-label">Observações</label>
            <form:textarea path="observacoes" cssClass="form-control" rows="2" />
          </div>

        </div>

        <hr class="my-3"/>

        <!-- ===== SERVIÇOS + PEÇAS (LADO A LADO) ===== -->
        <div class="row g-3">

          <!-- ===================== SERVIÇOS (CATÁLOGO) ===================== -->
          <div class="col-lg-6">
            <div class="border rounded p-2 h-100">
              <div class="d-flex align-items-center justify-content-between mb-2">
                <h5 class="mb-0">Serviços</h5>
                <button type="button" class="btn btn-outline-primary btn-sm" onclick="addServicoRow()">+ Adicionar serviço</button>
              </div>

              <div class="table-responsive">
                <table class="table table-sm table-striped align-middle mb-0">
                  <thead>
                  <tr>
                    <th>Serviço</th>
                    <th style="width: 60px;"></th>
                  </tr>
                  </thead>

                  <tbody id="tbodyServicos">
                  <c:choose>
                    <c:when test="${not empty servicosOs}">
                      <c:forEach var="s" items="${servicosOs}">
                        <tr>
                          <td>
                            <select class="form-select" name="servicoId">
                              <option value="">-- selecione --</option>
                              <c:forEach var="cat" items="${servicosCatalogo}">
                                <option value="${cat.idServico}"
                                  <c:if test="${
                                    (s.idServicoCatalogo != null && s.idServicoCatalogo == cat.idServico)
                                    || (s.idServicoCatalogo == null && s.descricao != null && cat.nome != null && s.descricao eq cat.nome)
                                  }">selected</c:if>>
                                  ${cat.nome} - R$ ${cat.valor}
                                </option>
                              </c:forEach>
                            </select>

                            <!-- fallback informativo -->
                            <c:if test="${s.descricao != null}">
                              <div class="small text-muted mt-1">
                                Item gravado na OS: ${s.descricao} (R$ ${s.valor})
                              </div>
                            </c:if>
                          </td>

                          <td class="text-end">
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                    onclick="this.closest('tr').remove()">X</button>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:when>

                    <c:otherwise>
                      <tr>
                        <td>
                          <select class="form-select" name="servicoId">
                            <option value="">-- selecione --</option>
                            <c:forEach var="cat" items="${servicosCatalogo}">
                              <option value="${cat.idServico}">${cat.nome} - R$ ${cat.valor}</option>
                            </c:forEach>
                          </select>
                        </td>
                        <td class="text-end">
                          <button type="button" class="btn btn-outline-secondary btn-sm"
                                  onclick="this.closest('tr').remove()">X</button>
                        </td>
                      </tr>
                    </c:otherwise>
                  </c:choose>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <!-- ===================== PEÇAS (CATÁLOGO) ===================== -->
          <div class="col-lg-6">
            <div class="border rounded p-2 h-100">
              <div class="d-flex align-items-center justify-content-between mb-2">
                <h5 class="mb-0">Peças</h5>
                <button type="button" class="btn btn-outline-primary btn-sm" onclick="addPecaRow()">+ Adicionar peça</button>
              </div>

              <div class="table-responsive">
                <table class="table table-sm table-striped align-middle mb-0">
                  <thead>
                  <tr>
                    <th>Peça/Produto</th>
                    <th style="width: 120px;">Qtd</th>
                    <th style="width: 60px;"></th>
                  </tr>
                  </thead>

                  <tbody id="tbodyPecas">
                  <c:choose>
                    <c:when test="${not empty pecasOs}">
                      <c:forEach var="p" items="${pecasOs}">
                        <tr>
                          <td>
                            <select class="form-select" name="pecaId">
                              <option value="">-- selecione --</option>
                              <c:forEach var="cat" items="${pecasCatalogo}">
                                <option value="${cat.idPeca}"
                                  <c:if test="${
                                    (p.idPecaCatalogo != null && p.idPecaCatalogo == cat.idPeca)
                                    || (p.idPecaCatalogo == null && p.descricao != null && cat.nome != null && p.descricao eq cat.nome)
                                  }">selected</c:if>>
                                  ${cat.nome} - R$ ${cat.valorUnitario}
                                </option>
                              </c:forEach>
                            </select>

                            <!-- fallback informativo -->
                            <c:if test="${p.descricao != null}">
                              <div class="small text-muted mt-1">
                                Item gravado na OS: ${p.descricao} (Qtd ${p.quantidade}, R$ ${p.valorUnitario})
                              </div>
                            </c:if>
                          </td>

                          <td>
                            <input class="form-control" type="number" min="1"
                                   name="pecaQuantidade"
                                   value="${p.quantidade != null ? p.quantidade : 1}" />
                          </td>

                          <td class="text-end">
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                    onclick="this.closest('tr').remove()">X</button>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:when>

                    <c:otherwise>
                      <tr>
                        <td>
                          <select class="form-select" name="pecaId">
                            <option value="">-- selecione --</option>
                            <c:forEach var="cat" items="${pecasCatalogo}">
                              <option value="${cat.idPeca}">${cat.nome} - R$ ${cat.valorUnitario}</option>
                            </c:forEach>
                          </select>
                        </td>
                        <td>
                          <input class="form-control" type="number" min="1" name="pecaQuantidade" value="1" />
                        </td>
                        <td class="text-end">
                          <button type="button" class="btn btn-outline-secondary btn-sm"
                                  onclick="this.closest('tr').remove()">X</button>
                        </td>
                      </tr>
                    </c:otherwise>
                  </c:choose>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

        </div>

        <script>
          function addServicoRow() {
            const tb = document.getElementById('tbodyServicos');
            const tr = document.createElement('tr');
            tr.innerHTML =
              `<td>
                <select class="form-select" name="servicoId">
                  <option value="">-- selecione --</option>
                  <c:forEach var="cat" items="${servicosCatalogo}">
                    <option value="${cat.idServico}">${cat.nome} - R$ ${cat.valor}</option>
                  </c:forEach>
                </select>
              </td>
              <td class="text-end">
                <button type="button" class="btn btn-outline-secondary btn-sm" onclick="this.closest('tr').remove()">X</button>
              </td>`;
            tb.appendChild(tr);
          }

          function addPecaRow() {
            const tb = document.getElementById('tbodyPecas');
            const tr = document.createElement('tr');
            tr.innerHTML =
              `<td>
                <select class="form-select" name="pecaId">
                  <option value="">-- selecione --</option>
                  <c:forEach var="cat" items="${pecasCatalogo}">
                    <option value="${cat.idPeca}">${cat.nome} - R$ ${cat.valorUnitario}</option>
                  </c:forEach>
                </select>
              </td>
              <td>
                <input class="form-control" type="number" min="1" name="pecaQuantidade" value="1"/>
              </td>
              <td class="text-end">
                <button type="button" class="btn btn-outline-secondary btn-sm" onclick="this.closest('tr').remove()">X</button>
              </td>`;
            tb.appendChild(tr);
          }
        </script>

      </form:form>

    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/footer.jspf"%>
</body>
</html>
