import axios from "axios";

export function formatDate(dateTime) {
    // Cria um objeto Date com a data de entryTime
    const date = new Date(dateTime);
    
    // Obtém o dia, mês e ano
    const day = String(date.getDate()).padStart(2, '0'); // Adiciona um zero à esquerda se for necessário
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Adiciona um zero à esquerda se for necessário (Os meses começam em 0)
    const year = date.getFullYear();
    
    // Obtém o horário
    const hours = String(date.getHours()).padStart(2, '0'); // Adiciona um zero à esquerda se for necessário
    const minutes = String(date.getMinutes()).padStart(2, '0'); // Adiciona um zero à esquerda se for necessário
    const seconds = String(date.getSeconds()).padStart(2, '0'); // Adiciona um zero à esquerda se for necessário
    
    // Retorna a data e hora formatadas
    return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
}

export async function isValidToken (token) {
    try {
        const response = await axios.post(`http://localhost:8080/api/auth/validate-token`, { token });
        console.log(response.status)

        if(response.status == 200) {
            return response.data.valid
        } else {
            return false
        }
    } catch (error) {
        console.error('Erro ao validar o token:', error);
        return false;
    }
}

