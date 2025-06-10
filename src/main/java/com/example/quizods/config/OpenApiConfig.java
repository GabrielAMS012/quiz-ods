package com.example.quizods.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Quiz ODS API",
                version = "1.0.0",
                description = "API REST para um quiz sobre Objetivos de Desenvolvimento Sustentável (ODS) focados em sustentabilidade urbana. Permite gerenciar perguntas e submeter respostas para obter uma pontuação."
        )
)
public class OpenApiConfig {
}