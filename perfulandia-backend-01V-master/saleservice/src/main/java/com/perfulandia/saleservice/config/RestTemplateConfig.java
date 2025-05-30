
package com.perfulandia.saleservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        // Usar HttpComponentsClientHttpRequestFactory que soporta PATCH
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
