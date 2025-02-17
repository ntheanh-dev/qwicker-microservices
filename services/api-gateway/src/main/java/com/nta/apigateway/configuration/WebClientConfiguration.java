package com.nta.apigateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.nta.apigateway.repository.IdentityClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:8082/identity").build();
    }

    @Bean
    public IdentityClient identityClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(
                        WebClientAdapter.create(webClient))
                .build();
        return httpServiceProxyFactory.createClient(IdentityClient.class);
    }
}
