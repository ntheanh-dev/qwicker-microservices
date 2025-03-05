package com.nta.paymentservice.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthenticationRequestInterceptor implements RequestInterceptor {
  @Override
  public void apply(final RequestTemplate requestTemplate) {
    final ServletRequestAttributes servletRequestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    assert servletRequestAttributes != null;
    final var authHeader = servletRequestAttributes.getRequest().getHeader("Authorization");
    if (StringUtils.hasText(authHeader)) {
      requestTemplate.header("Authorization", authHeader);
    }
  }
}
