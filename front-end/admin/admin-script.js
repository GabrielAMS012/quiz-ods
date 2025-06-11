document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("create-question-form");
  const responseMessageDiv = document.getElementById("response-message");

  // URL base da sua API. Certifique-se de que a API está rodando!
  const API_BASE_URL = "http://localhost:8080";

  form.addEventListener("submit", async (event) => {
    event.preventDefault(); // Impede o recarregamento da página

    // Coleta os dados do formulário
    const text = document.getElementById("question-text").value;
    const option1 = document.getElementById("option1").value;
    const option2 = document.getElementById("option2").value;
    const option3 = document.getElementById("option3").value;
    const option4 = document.getElementById("option4").value;
    const correctOptionIndex = parseInt(
      document.getElementById("correct-option-index").value
    );
    const category = document.getElementById("category").value;

    // Cria o objeto de pergunta conforme o modelo da API
    const newQuestion = {
      text: text, //
      options: [option1, option2, option3, option4], //
      correctOptionIndex: correctOptionIndex, //
      category: category, //
    };

    try {
      const response = await fetch(`${API_BASE_URL}/questions`, {
        //
        method: "POST", //
        headers: {
          "Content-Type": "application/json", //
        },
        body: JSON.stringify(newQuestion), //
      });

      if (response.status === 201) {
        // 201 Created
        const createdQuestion = await response.json();
        displayMessage(
          `Pergunta "${createdQuestion.text}" criada com sucesso! ID: ${createdQuestion.id}`,
          "success"
        ); //
        form.reset(); // Limpa o formulário após o sucesso
      } else {
        const errorData = await response.json();
        displayMessage(
          `Erro ao criar pergunta: ${errorData.message || response.statusText}`,
          "error"
        );
      }
    } catch (error) {
      console.error("Erro ao enviar pergunta:", error);
      displayMessage(
        "Erro de conexão ao criar pergunta. Verifique se a API está rodando.",
        "error"
      );
    }
  });

  // Função para exibir mensagens na tela
  function displayMessage(message, type) {
    responseMessageDiv.textContent = message;
    responseMessageDiv.className = `message-box ${type}`;
    responseMessageDiv.style.display = "block";
    setTimeout(() => {
      responseMessageDiv.style.display = "none";
    }, 5000); // Esconde a mensagem após 5 segundos
  }
});
