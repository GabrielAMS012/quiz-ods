package com.example.quizods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Map;

@Data
@Schema(description = "Objeto para submissão das respostas de um quiz.")
public class QuizSubmissionDTO {

    @Schema(description = "Mapa de respostas do usuário, onde a chave é o ID da pergunta e o valor é o índice da resposta escolhida.",
            example = "{\"1\": 1, \"2\": 2}")
    private Map<Long, Integer> answers;
}