document.addEventListener("DOMContentLoaded", () => {
  const quizContainer = document.getElementById("quiz-container"); //
  const submitButton = document.getElementById("submit-quiz"); //
  const quizResultDiv = document.getElementById("quiz-result"); //

  let questions = []; // Array para armazenar as perguntas carregadas da API
  const userAnswers = new Map(); // Mapa para armazenar as respostas do usuário (questionId -> chosenOptionIndex)

  // URL base da sua API. Certifique-se de que a API está rodando!
  // Se você estiver executando o Spring Boot na porta padrão 8080, esta URL deve funcionar.
  const API_BASE_URL = "http://localhost:8080";

  // Função para carregar as perguntas da API
  async function fetchQuestions() {
    try {
      quizContainer.innerHTML = "<p>Carregando perguntas...</p>"; //
      const response = await fetch(`${API_BASE_URL}/questions`); //
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      questions = await response.json(); //
      renderQuestions(); // Renderiza as perguntas após carregá-las
      submitButton.style.display = "block"; // Mostra o botão de submissão
    } catch (error) {
      console.error("Erro ao carregar perguntas:", error);
      quizContainer.innerHTML =
        "<p>Erro ao carregar perguntas. Por favor, tente novamente mais tarde.</p>"; //
    }
  }

  // Função para renderizar as perguntas no HTML
  function renderQuestions() {
    quizContainer.innerHTML = ""; // Limpa o conteúdo existente
    if (questions.length === 0) {
      quizContainer.innerHTML = "<p>Nenhuma pergunta disponível.</p>";
      submitButton.style.display = "none";
      return;
    }

    questions.forEach((question) => {
      //
      const questionCard = document.createElement("div"); //
      questionCard.classList.add("question-card"); //

      const questionText = document.createElement("p"); //
      questionText.classList.add("question-text"); //
      questionText.textContent = question.text; //
      questionCard.appendChild(questionText); //

      const optionsList = document.createElement("ul"); //
      optionsList.classList.add("options-list"); //

      question.options.forEach((option, index) => {
        //
        const listItem = document.createElement("li"); //
        const radioInput = document.createElement("input"); //
        radioInput.type = "radio"; //
        radioInput.name = `question-${question.id}`; //
        radioInput.value = index; //
        radioInput.id = `question-${question.id}-option-${index}`; //

        radioInput.addEventListener("change", (event) => {
          // Armazena a resposta do usuário quando uma opção é selecionada
          userAnswers.set(question.id, parseInt(event.target.value)); //
        });

        const label = document.createElement("label"); //
        label.htmlFor = `question-${question.id}-option-${index}`; //
        label.textContent = option; //

        listItem.appendChild(radioInput); //
        listItem.appendChild(label); //
        optionsList.appendChild(listItem); //
      });

      questionCard.appendChild(optionsList); //
      quizContainer.appendChild(questionCard); //
    });
  }

  // Função para submeter as respostas e calcular a pontuação
  submitButton.addEventListener("click", async () => {
    if (userAnswers.size !== questions.length) {
      alert("Por favor, responda a todas as perguntas antes de enviar!");
      return;
    }

    const submissionPayload = {
      answers: Object.fromEntries(userAnswers), // Converte o Map para um objeto JSON
    };

    try {
      const response = await fetch(`${API_BASE_URL}/quiz/submit`, {
        //
        method: "POST", //
        headers: {
          "Content-Type": "application/json", //
        },
        body: JSON.stringify(submissionPayload), //
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json(); //
      displayQuizResult(result); // Exibe o resultado
      submitButton.style.display = "none"; // Oculta o botão de submissão
      quizContainer.style.display = "none"; // Oculta as perguntas
    } catch (error) {
      console.error("Erro ao submeter quiz:", error);
      quizResultDiv.innerHTML =
        "<p>Erro ao submeter o quiz. Por favor, tente novamente.</p>"; //
      quizResultDiv.style.display = "block"; //
    }
  });

  // Função para exibir o resultado do quiz
  function displayQuizResult(result) {
    quizResultDiv.innerHTML = `
            <p>Total de Perguntas: ${result.totalQuestions}</p>
            <p>Respostas Corretas: ${result.correctAnswers}</p>
            <p>${result.message}</p>
        `; //
    quizResultDiv.style.display = "block"; // Exibe a div de resultado
  }

  // Inicia o carregamento das perguntas quando a página é carregada
  fetchQuestions();
});
