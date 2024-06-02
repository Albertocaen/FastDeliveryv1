document.addEventListener('DOMContentLoaded', function() {
    // Manejo del chatbot desplegable
    window.toggleChatbot = function() {
        const chatbotContainer = document.querySelector('.chatbot-container');
        chatbotContainer.classList.toggle('minimized');
        if (chatbotContainer.classList.contains('minimized')) {
            chatbotContainer.style.height = '60px';
        } else {
            chatbotContainer.style.height = '400px';
        }
    }

    // Envío de mensajes
    window.sendMessage = async function() {
        const inputField = document.getElementById('chatbot-input');
        const userMessage = inputField.value;
        const messagesContainer = document.getElementById('chatbot-messages');

        if (userMessage.trim() === '') return;

        // Añadir mensaje del usuario
        const userMessageElement = document.createElement('div');
        userMessageElement.className = 'user-message';
        userMessageElement.textContent = userMessage;
        messagesContainer.appendChild(userMessageElement);

        // Limpiar el campo de entrada
        inputField.value = '';

        // Desplazar el contenedor de mensajes hacia abajo
        messagesContainer.scrollTop = messagesContainer.scrollHeight;

        try {
            const response = await fetch('/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message: userMessage })
            });

            const data = await response.json();
            const botMessage = data.message;

            // Añadir respuesta del chatbot
            const chatbotMessageElement = document.createElement('div');
            chatbotMessageElement.className = 'chatbot-message';
            chatbotMessageElement.textContent = botMessage;
            messagesContainer.appendChild(chatbotMessageElement);

            // Desplazar el contenedor de mensajes hacia abajo
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        } catch (error) {
            console.error('Error:', error);
        }
    }
});
