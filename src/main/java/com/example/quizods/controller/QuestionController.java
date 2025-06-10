package com.example.quizods.controller;

import com.example.quizods.model.Category;
import com.example.quizods.model.Question;
import com.example.quizods.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
@Tag(name = "Gerenciamento de Perguntas", description = "Endpoints para criar, atualizar e listar perguntas do quiz.")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(summary = "Lista perguntas", description = "Retorna uma lista de todas as perguntas. Pode ser filtrada por categoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de perguntas retornada com sucesso.")
    })
    @GetMapping
    public ResponseEntity<List<Question>> getQuestions(
            @Parameter(description = "Filtra as perguntas pela categoria especificada. Se não for informado, retorna todas.")
            @RequestParam(required = false) Category category) {
        List<Question> questions = questionService.getQuestions(Optional.ofNullable(category));
        return ResponseEntity.ok(questions);
    }

    @Operation(summary = "Cria uma nova pergunta", description = "Adiciona uma nova pergunta ao banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pergunta criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (corpo da requisição malformatado).")
    })
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        Question createdQuestion = questionService.createQuestion(question);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualiza uma pergunta existente", description = "Atualiza os dados de uma pergunta com base no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pergunta atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pergunta não encontrada para o ID informado."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
            @Parameter(description = "ID da pergunta a ser atualizada.") @PathVariable Long id,
            @RequestBody Question questionDetails) {
        return questionService.updateQuestion(id, questionDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}