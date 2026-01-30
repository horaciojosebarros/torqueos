<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <title>TorqueOS</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf"%>

  <style>
    /* Compactação: menos altura e menos espaços */
    .compact-label { margin-bottom: 4px; }
    .mb-compact { margin-bottom: .75rem !important; }
    .table-sm td, .table-sm th { padding-top: .35rem; padding-bottom: .35rem; }
  </style>
</head>

<body>
  <%@ include file="/WEB-INF/views/fragments/header.jspf"%>

  <div class="container py-3">
    <div class="card app-card">
      <div class="card-body">

        <h2 class="mb-3">${ordem.idOs == null ? 'Novo Orçamento/OS' : 'Editar Orçamento/OS'}</h2>

        <form:form method="POST" modelAttribute="ordem"
                   action="${pageContext.request.contextPath}/ordens/salvar"
                   cssClass="needs-validation" novalidate="novalidate">

          <form:hidden path="idOs" />

          <!-- ===== Linha compacta: Cliente + Veículo + Status (mesma linha no desktop) ===== -->
          <div class="row g-2 align-items-end mb-compact">

            <!-- Cliente (mais compacto) -->
            <div class="col-12 col-lg-5">
              <label class="form-label compact-label">Cliente</label>
              <select class="form-select form-select-sm" name="clienteId">
                <option value="">-- selecione --</option>
                <c:forEach var="c" items="${clientes}">
                  <option value="${c.idCliente}"
                          <c:if test="${ordem.cliente != null && ordem.cliente.idCliente == c.idCliente}">selected</c:if>>
                    ${c.nome}
                  </option>
                </c:forEach>
              </select>
            </div>

            <!-- Veículo (mais compacto) -->
            <div class="col-12 col-lg-5">
              <label class="form-label compact-label">Veículo</label>
              <select class="form-select form-select-sm" name="veiculoId">
                <option value="">-- selecione --</option>
                <c:forEach var="v" items="${veiculos}">
                  <option value="${v.idVeiculo}"
                          <c:if test="${ordem.veiculo != null && ordem.veiculo.idVeiculo == v.idVeiculo}">selected</c:if>>
                    ${v.placa} - ${v.modelo}
                  </option>
                </c:forEach>
              </select>
            </div>

            <!-- Status (mais estreito) -->
            <div class="col-12 col-lg-2">
              <label class="form-label compact-label">Status</label>
              <form:select path="status" cssClass="form-select form-select-sm" required="required">
                <form:option value="">--</form:option>
                <form:option value="EM_ANDAMENTO">Em andamento</form:option>
                <form:option value="SUSPENSA">Suspensa</form:option>
                <form:option value="NAO_AUTORIZADA">Não autorizada</form:option>
                <form:option value="CONCLUIDA">Concluída</form:option>
              </form:select>
            </div>

          </div>

          <!-- Observações (menos linhas + maxlength) -->
          <div class="mb-compact">
            <label class="form-label compact-label">Observações</label>
            <!-- TROQUE 500 pelo tamanho real do @Column(length=...) no seu banco -->
            <form:textarea path="observacoes" cssClass="form-control form-control-sm" rows="2" maxlength="500" />
          </div>

          <hr class="my-3" />

          <!-- ===================== TÉCNICOS ===================== -->

          <div class="table-responsive">
            <table class="table table-sm table-striped align-middle">
              <thead>
              <tr>
                <th>Técnico</th>
                <th style="width: 170px;">Papel</th>
                <th style="width: 160px;">% Comissão (OS)</th>
                <th style="width: 60px;"></th>
              </tr>
              </thead>

              <tbody id="tbodyTecnicos">
              <c:choose>
                <c:when test="${not empty tecnicosOs}">
                  <c:forEach var="tlink" items="${tecnicosOs}">
                    <tr class="tecnico-row">
                      <td>
                        <select class="form-select form-select-sm" name="tecnicoId" onchange="toggleTecnicoRow(this)">
                          <option value="">-- selecione --</option>
                          <c:forEach var="tec" items="${tecnicosCatalogo}">
                            <option value="${tec.idTecnico}"
                              <c:if test="${tlink.tecnico != null && tlink.tecnico.idTecnico == tec.idTecnico}">selected</c:if>>
                              ${tec.nome} (${tec.tipo})
                            </option>
                          </c:forEach>
                        </select>

                        <c:if test="${tlink.tecnico != null}">
                          <div class="small text-muted mt-1">
                            Vinculado: ${tlink.tecnico.nome} (${tlink.tecnico.tipo})
                          </div>
                        </c:if>
                      </td>

                      <td>
                        <select class="form-select form-select-sm" name="tecnicoPapel">
                          <option value="MECANICO" <c:if test="${tlink.papel == 'MECANICO'}">selected</c:if>>MECÂNICO</option>
                          <option value="AUXILIAR" <c:if test="${tlink.papel == 'AUXILIAR'}">selected</c:if>>AUXILIAR</option>
                        </select>
                      </td>

                      <td>
                        <input class="form-control form-control-sm" type="number" min="0" max="100"
                               name="tecnicoPercentual"
                               value="${tlink.percentual != null ? tlink.percentual : ''}"
                               placeholder="0..100" />
                      </td>

                      <td>
                        <button type="button"
                                class="btn btn-outline-secondary btn-sm"
                                onclick="removeTecnicoRow(this)">X</button>
                      </td>
                    </tr>
                  </c:forEach>
                </c:when>

                <c:otherwise>
                  <tr class="tecnico-row">
                    <td>
                      <select class="form-select form-select-sm" name="tecnicoId" onchange="toggleTecnicoRow(this)">
                        <option value="">-- selecione --</option>
                        <c:forEach var="tec" items="${tecnicosCatalogo}">
                          <option value="${tec.idTecnico}">${tec.nome} (${tec.tipo})</option>
                        </c:forEach>
                      </select>
                    </td>

                    <td>
                      <select class="form-select form-select-sm" name="tecnicoPapel" disabled>
                        <option value="MECANICO">MECÂNICO</option>
                        <option value="AUXILIAR">AUXILIAR</option>
                      </select>
                    </td>

                    <td>
                      <input class="form-control form-control-sm" type="number" min="0" max="100"
                             name="tecnicoPercentual" value=""
                             placeholder="0..100"
                             disabled />
                    </td>

                    <td>
                      <button type="button"
                              class="btn btn-outline-secondary btn-sm"
                              onclick="removeTecnicoRow(this)">X</button>
                    </td>
                  </tr>
                </c:otherwise>
              </c:choose>
              </tbody>
            </table>
          </div>

          <button type="button" class="btn btn-outline-primary btn-sm" onclick="addTecnicoRow()">
            + Adicionar técnico
          </button>

          <hr class="my-3" />

          <!-- ===================== SERVIÇOS (CATÁLOGO) ===================== -->

          <div class="table-responsive">
            <table class="table table-sm table-striped align-middle">
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
                        <select class="form-select form-select-sm" name="servicoId">
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
                      </td>

                      <td>
                        <button type="button"
                                class="btn btn-outline-secondary btn-sm"
                                onclick="this.closest('tr').remove()">X</button>
                      </td>
                    </tr>
                  </c:forEach>
                </c:when>

                <c:otherwise>
                  <tr>
                    <td>
                      <select class="form-select form-select-sm" name="servicoId">
                        <option value="">-- selecione --</option>
                        <c:forEach var="cat" items="${servicosCatalogo}">
                          <option value="${cat.idServico}">
                            ${cat.nome} - R$ ${cat.valor}
                          </option>
                        </c:forEach>
                      </select>
                    </td>
                    <td>
                      <button type="button"
                              class="btn btn-outline-secondary btn-sm"
                              onclick="this.closest('tr').remove()">X</button>
                    </td>
                  </tr>
                </c:otherwise>
              </c:choose>
              </tbody>
            </table>
          </div>

          <button type="button" class="btn btn-outline-primary btn-sm" onclick="addServicoRow()">
            + Adicionar serviço
          </button>

          <hr class="my-3" />

          <!-- ===================== PEÇAS (CATÁLOGO) ===================== -->

          <div class="table-responsive">
            <table class="table table-sm table-striped align-middle">
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
                        <select class="form-select form-select-sm" name="pecaId">
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
                      </td>

                      <td>
                        <input class="form-control form-control-sm" type="number" min="1"
                               name="pecaQuantidade"
                               value="${p.quantidade != null ? p.quantidade : 1}" />
                      </td>

                      <td>
                        <button type="button"
                                class="btn btn-outline-secondary btn-sm"
                                onclick="this.closest('tr').remove()">X</button>
                      </td>
                    </tr>
                  </c:forEach>
                </c:when>

                <c:otherwise>
                  <tr>
                    <td>
                      <select class="form-select form-select-sm" name="pecaId">
                        <option value="">-- selecione --</option>
                        <c:forEach var="cat" items="${pecasCatalogo}">
                          <option value="${cat.idPeca}">
                            ${cat.nome} - R$ ${cat.valorUnitario}
                          </option>
                        </c:forEach>
                      </select>
                    </td>
                    <td>
                      <input class="form-control form-control-sm" type="number" min="1" name="pecaQuantidade" value="1" />
                    </td>
                    <td>
                      <button type="button"
                              class="btn btn-outline-secondary btn-sm"
                              onclick="this.closest('tr').remove()">X</button>
                    </td>
                  </tr>
                </c:otherwise>
              </c:choose>
              </tbody>
            </table>
          </div>

          <button type="button" class="btn btn-outline-primary btn-sm" onclick="addPecaRow()">
            + Adicionar peça
          </button>

          <hr class="my-3" />

          <!-- Botões -->
          <div class="d-flex gap-2 mt-3">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a class="btn btn-outline-secondary"
               href="${pageContext.request.contextPath}/ordens">Voltar</a>
         

          <c:if test="${ordem.idOs != null}">
            <a class="btn btn-outline-primary mt-2"
               href="${pageContext.request.contextPath}/ordens/pdf/${ordem.idOs}"
               target="_blank">
              Gerar Orçamento/OS em PDF
            </a>
          </c:if>
		  </div>

          <script>
            function toggleTecnicoRow(selectEl) {
              const tr = selectEl.closest('tr');
              if (!tr) return;

              const hasTecnico = selectEl.value && selectEl.value.trim() !== '';

              const papel = tr.querySelector('select[name="tecnicoPapel"]');
              const pct = tr.querySelector('input[name="tecnicoPercentual"]');

              if (papel) papel.disabled = !hasTecnico;
              if (pct) pct.disabled = !hasTecnico;

              if (!hasTecnico) {
                if (papel) papel.value = 'MECANICO';
                if (pct) pct.value = '';
              }
            }

            function removeTecnicoRow(btn) {
              const tr = btn.closest('tr');
              if (tr) tr.remove();
            }

            function addTecnicoRow() {
              const tb = document.getElementById('tbodyTecnicos');
              const tr = document.createElement('tr');
              tr.className = 'tecnico-row';
              tr.innerHTML =
                `<td>
                   <select class="form-select form-select-sm" name="tecnicoId" onchange="toggleTecnicoRow(this)">
                     <option value="">-- selecione --</option>
                     <c:forEach var="tec" items="${tecnicosCatalogo}">
                       <option value="${tec.idTecnico}">${tec.nome} (${tec.tipo})</option>
                     </c:forEach>
                   </select>
                 </td>
                 <td>
                   <select class="form-select form-select-sm" name="tecnicoPapel" disabled>
                     <option value="MECANICO">MECÂNICO</option>
                     <option value="AUXILIAR">AUXILIAR</option>
                   </select>
                 </td>
                 <td>
                   <input class="form-control form-control-sm" type="number" min="0" max="100"
                          name="tecnicoPercentual" value=""
                          placeholder="0..100"
                          disabled />
                 </td>
                 <td>
                   <button type="button" class="btn btn-outline-secondary btn-sm"
                           onclick="removeTecnicoRow(this)">X</button>
                 </td>`;
              tb.appendChild(tr);

              const sel = tr.querySelector('select[name="tecnicoId"]');
              if (sel) toggleTecnicoRow(sel);
            }

            function addServicoRow() {
              const tb = document.getElementById('tbodyServicos');
              const tr = document.createElement('tr');
              tr.innerHTML =
                `<td>
                   <select class="form-select form-select-sm" name="servicoId">
                     <option value="">-- selecione --</option>
                     <c:forEach var="cat" items="${servicosCatalogo}">
                       <option value="${cat.idServico}">${cat.nome} - R$ ${cat.valor}</option>
                     </c:forEach>
                   </select>
                 </td>
                 <td>
                   <button type="button" class="btn btn-outline-secondary btn-sm"
                           onclick="this.closest('tr').remove()">X</button>
                 </td>`;
              tb.appendChild(tr);
            }

            function addPecaRow() {
              const tb = document.getElementById('tbodyPecas');
              const tr = document.createElement('tr');
              tr.innerHTML =
                `<td>
                   <select class="form-select form-select-sm" name="pecaId">
                     <option value="">-- selecione --</option>
                     <c:forEach var="cat" items="${pecasCatalogo}">
                       <option value="${cat.idPeca}">${cat.nome} - R$ ${cat.valorUnitario}</option>
                     </c:forEach>
                   </select>
                 </td>
                 <td>
                   <input class="form-control form-control-sm" type="number" min="1"
                          name="pecaQuantidade" value="1"/>
                 </td>
                 <td>
                   <button type="button" class="btn btn-outline-secondary btn-sm"
                           onclick="this.closest('tr').remove()">X</button>
                 </td>`;
              tb.appendChild(tr);
            }

            document.addEventListener('DOMContentLoaded', function() {
              document.querySelectorAll('#tbodyTecnicos select[name="tecnicoId"]').forEach(toggleTecnicoRow);
            });
          </script>

        </form:form>

      </div>
    </div>
  </div>

  <%@ include file="/WEB-INF/views/fragments/footer.jspf"%>
</body>
</html>
