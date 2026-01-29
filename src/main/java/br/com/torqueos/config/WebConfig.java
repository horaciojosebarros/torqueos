package br.com.torqueos.config;

import br.com.torqueos.auth.AuthTenantInterceptor;
import br.com.torqueos.web.EmpresaSessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final EmpresaSessionInterceptor empresaSessionInterceptor;

  public WebConfig(EmpresaSessionInterceptor empresaSessionInterceptor) {
    this.empresaSessionInterceptor = empresaSessionInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    // 1) primeiro garante usuário/tenant no TenantContext
    registry.addInterceptor(new AuthTenantInterceptor())
      .addPathPatterns("/**")
      .excludePathPatterns("/css/**", "/js/**", "/images/**", "/webjars/**");

    // 2) depois coloca EMPRESA_NOME na session
    registry.addInterceptor(empresaSessionInterceptor)
      .addPathPatterns("/**")
      .excludePathPatterns("/css/**", "/js/**", "/images/**", "/webjars/**");
  }
}
