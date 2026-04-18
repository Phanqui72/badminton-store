package com.badminton.store.feign; // Đảm bảo đúng package của bạn

import com.badminton.store.feign.AuthFeignClient;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NativeFeignConfig {
    @Value("${auth.server.url:http://localhost:4120}")
    private String authServerUrl;

    @Bean
    public AuthFeignClient authFeignClient() {
        return Feign.builder()
                .encoder(new feign.form.FormEncoder())
                .decoder(new feign.jackson.JacksonDecoder())
                // Tự động thêm Header Authorization: Basic ...
                .requestInterceptor(new feign.auth.BasicAuthRequestInterceptor("abc_client", "abc123"))
                .target(AuthFeignClient.class, authServerUrl);
    }
}