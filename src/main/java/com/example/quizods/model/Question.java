package com.example.quizods.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa uma pergunta do quiz com suas opções e resposta correta.")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da pergunta, gerado pelo banco de dados.", example = "1")
    private Long id;

    @Column(nullable = false, length = 500)
    @Schema(description = "O enunciado (texto) da pergunta.", example = "Qual é a maneira correta de descartar o lixo eletrônico?")
    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text", nullable = false)
    @Schema(description = "Uma lista com 4 strings que representam as opções de resposta.")
    private List<String> options;

    @Column(nullable = false)
    @Schema(description = "O índice (de 0 a 3) da resposta correta na lista de opções.", example = "2")
    private int correctOptionIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "A categoria temática da pergunta.", example = "LIXO")
    private Category category;
}