package br.com.torqueos.web;

import br.com.torqueos.service.EmpresaService;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class EmpresaSessionInterceptor implements HandlerInterceptor {

  private final EmpresaService empresaService;

  public EmpresaSessionInterceptor(EmpresaService empresaService) {
    this.empresaService = empresaService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    try {
      Long empId = TenantContext.getEmpresaId();
      if (empId != null) {
        // evita bater no banco toda hora
        Object atual = request.getSession().getAttribute("EMPRESA_NOME");
        Object atualId = request.getSession().getAttribute("EMPRESA_ID");
        if (atual == null || atualId == null || !empId.equals(atualId)) {
          request.getSession().setAttribute("EMPRESA_ID", empId);
          request.getSession().setAttribute("EMPRESA_NOME", empresaService.getNomeEmpresaAtual());
        }
      }
    } catch (Exception ignore) {
      // se der problema, não derruba a request (só não mostra o nome)
    }
    return true;
  }
}
