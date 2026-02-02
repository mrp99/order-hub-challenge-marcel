package com.orderhub.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String MESSAGE = "Descrição: API para controle de estoque e processamento de pedidos.";
    private static final String TITLE = "Order Hub - API de Gerenciamento.";
    private static final String NUMERIC_VERSION = "1.0";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info().title(TITLE).version(NUMERIC_VERSION).description(MESSAGE)
                );
    }
}
