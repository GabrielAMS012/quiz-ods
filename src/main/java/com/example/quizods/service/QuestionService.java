package com.example.quizods.service;

import com.example.quizods.dto.QuizResultDTO;
import com.example.quizods.dto.QuizSubmissionDTO;
import com.example.quizods.model.Category;
import com.example.quizods.model.Question;
import com.example.quizods.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getQuestions(Optional<Category> category) {
        return category.map(questionRepository::findByCategory)
                .orElseGet(questionRepository::findAll);
    }

    public Question createQuestion(Question question) {
        question.setId(null);
        return questionRepository.save(question);
    }

    public Optional<Question> updateQuestion(Long id, Question questionDetails) {
        return questionRepository.findById(id).map(existingQuestion -> {
            existingQuestion.setText(questionDetails.getText());
            existingQuestion.setOptions(questionDetails.getOptions());
            existingQuestion.setCorrectOptionIndex(questionDetails.getCorrectOptionIndex());
            existingQuestion.setCategory(questionDetails.getCategory());
            return questionRepository.save(existingQuestion);
        });
    }

    public QuizResultDTO calculateScore(QuizSubmissionDTO submission) {
        Map<Long, Integer> userAnswers = submission.getAnswers();
        int correctAnswersCount = 0;

        for (Map.Entry<Long, Integer> entry : userAnswers.entrySet()) {
            Long questionId = entry.getKey();
            Integer userAnswerIndex = entry.getValue();

            Optional<Question> optionalQuestion = questionRepository.findById(questionId);

            if (optionalQuestion.isPresent()) {
                Question question = optionalQuestion.get();
                if (question.getCorrectOptionIndex() == userAnswerIndex) {
                    correctAnswersCount++;
                }
            }
        }

        int totalQuestions = userAnswers.size();
        String message = generateEducationalMessage(correctAnswersCount, totalQuestions);

        return new QuizResultDTO(totalQuestions, correctAnswersCount, message);
    }

    private String generateEducationalMessage(int correctAnswers, int totalQuestions) {
        if (totalQuestions == 0) {
            return "Nenhuma resposta foi submetida.";
        }

        double percentage = (double) correctAnswers / totalQuestions;

        if (percentage >= 0.75) {
            return String.format("Excelente! Você acertou %d de %d perguntas. Seu conhecimento sobre sustentabilidade urbana é impressionante!", correctAnswers, totalQuestions);
        } else if (percentage >= 0.5) {
            return String.format("Muito bem! Você acertou %d de %d perguntas. Continue estudando para se tornar um especialista!", correctAnswers, totalQuestions);
        } else {
            return String.format("Você acertou %d de %d perguntas. Não desanime! Cada erro é uma oportunidade para aprender mais sobre como tornar nossas cidades mais sustentáveis.", correctAnswers, totalQuestions);
        }
    }
}