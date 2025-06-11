package com.example.quizods.controller;

import com.example.quizods.dto.QuizResultDTO;
import com.example.quizods.dto.QuizSubmissionDTO;
import com.example.quizods.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
@CrossOrigin(origins = "*")
@Tag(name = "Operações do Quiz", description = "Endpoints para interagir com o quiz, como submeter respostas.")
public class QuizController {

    private final QuestionService questionService;

    public QuizController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(summary = "Submete respostas do quiz", description = "Recebe as respostas do usuário, calcula a pontuação e retorna o resultado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado do quiz calculado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida.")
    })
    @PostMapping("/submit")
    public ResponseEntity<QuizResultDTO> submitQuiz(@RequestBody QuizSubmissionDTO submission) {
        QuizResultDTO result = questionService.calculateScore(submission);
        return ResponseEntity.ok(result);
    }
}