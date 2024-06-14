import axios from "axios";
import { toast } from "react-toastify";

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Substitua pela URL base da sua API
});

export const handleLogin = async (data) => {
    console.log("handle", data)
    await api.post('/auth/login', data);
};

export default api;