package br.com.torqueos.config;

import br.com.torqueos.auth.AuthTenantInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AuthTenantInterceptor())
      .addPathPatterns("/**");
  }
}
