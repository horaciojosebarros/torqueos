package br.com.torqueos.auth;

import br.com.torqueos.tenant.TenantContext;
import javax.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthTenantInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String uri = request.getRequestURI();
    String ctx = request.getContextPath();

    // rotas públicas / recursos
    if (uri.startsWith(ctx + "/auth/") || uri.startsWith(ctx + "/h2") ||
        uri.startsWith(ctx + "/css/") || uri.startsWith(ctx + "/js/") || uri.startsWith(ctx + "/images/")) {
      return true;
    }

    HttpSession session = request.getSession(false);
    if (session == null) {
      response.sendRedirect(ctx + "/auth/login");
      return false;
    }

    Long userId = (Long) session.getAttribute("USER_ID");
    Long empresaId = (Long) session.getAttribute("EMPRESA_ID");
    String role = (String) session.getAttribute("ROLE");

    if (userId == null || role == null) {
  response.sendRedirect(ctx + "/auth/login");
  return false;
}

// SUPERADMIN pode logar sem empresa selecionada (EMPRESA_ID = null)
if (!"SUPERADMIN".equalsIgnoreCase(role) && empresaId == null) {
  response.sendRedirect(ctx + "/auth/login");
  return false;
}

    if (empresaId != null) TenantContext.setEmpresaId(empresaId);
    CurrentUserContext.set(userId, role);

// Se for SUPERADMIN e ainda não escolheu empresa, exigir seleção antes de acessar telas tenant-scoped
if ("SUPERADMIN".equalsIgnoreCase(role) && empresaId == null) {
  boolean allowNoTenant = uri.startsWith(ctx + "/admin/") || uri.startsWith(ctx + "/planos") || uri.startsWith(ctx + "/auth/");
  if (!allowNoTenant) {
    response.sendRedirect(ctx + "/admin/empresas");
    return false;
  }
}

    // Autorização simples (admin only)
    boolean adminOnly =
      uri.startsWith(ctx + "/usuarios") ||
      uri.startsWith(ctx + "/planos") ||
      uri.startsWith(ctx + "/admin/"); // planos = cadastro global simples (demo)

    if (adminOnly && !"ADMIN".equalsIgnoreCase(role) && !"SUPERADMIN".equalsIgnoreCase(role)) {
      response.setStatus(403);
      response.getWriter().write("Acesso negado (ADMIN apenas).");
      return false;
    }

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    TenantContext.clear();
    CurrentUserContext.clear();
  }
}
