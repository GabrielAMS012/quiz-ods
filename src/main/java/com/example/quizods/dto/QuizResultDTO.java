package com.example.quizods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Objeto com o resultado consolidado do quiz.")
public class QuizResultDTO {

    @Schema(description = "Número total de perguntas que foram respondidas.", example = "4")
    private int totalQuestions;

    @Schema(description = "Número de respostas corretas.", example = "3")
    private int correctAnswers;

    @Schema(description = "Mensagem educativa personalizada com base na pontuação.", example = "Excelente! Você acertou 3 de 4 perguntas...")
    private String message;
}