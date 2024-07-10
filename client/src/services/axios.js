import axios from "axios";
import { toast } from "react-toastify";
import Cookies from "js-cookie";
import jwt from "jsonwebtoken";

export const fetchData = async (path) => {
    try {
        const token = Cookies.get("auth_token");

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` 
        };

        const response = await axios.get(`http://localhost:8080/api/${path}`, { headers });
        return response.data
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
};

export const fetchDataPage = async (limit, currentPage, path) => {
    try {
        const token = Cookies.get("auth_token");

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` 
        };

        const response = await axios.get(`http://localhost:8080/api/${path}?size=${limit}&page=${currentPage}`, { headers });
        return response.data
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
};

export const fetchDataAll = async (path) => {
    try {
        const token = Cookies.get("auth_token");

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }

        if (!path) return
        const response = await axios.get(`http://localhost:8080/api/orcamento/${path}/all`, { headers });
        return response.data
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
};

export const fetchDataById = async (id, path) => {
    try {
        const token = Cookies.get("auth_token");

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }

        const response = await axios.get(`http://localhost:8080/api/orcamento/${path}/${id}`, { headers });
        return response.data
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
        toast.error("Erro ao buscar dado!")
    }
};

export const handleCreate = async (data, path) => {
    try {
        const token = Cookies.get("auth_token");

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` 
        };

        const response = await axios.post(`http://localhost:8080/api/${path}`, data, { headers });
        if (response.status === 200) toast.success('Cadastrado com sucesso!');
        return response;
    } catch (error) {
        if (error.response && error.response.data && error.response.data.messages && error.response.data.messages.length > 0) {
            toast.error(error.response.data.messages[0]);
        } else {
            toast.error('Ocorreu um erro ao cadastrar!');
        }
    }
}

export const handleUpdate = async (id, path, data) => {
    try {
        const token = Cookies.get("auth_token");

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` 
        };

        const response = await axios.put(`http://localhost:8080/api/${path}?id=${id}`, data, { headers });
        if (response.status === 200) toast.success('Operação realizada com sucesso!');
        return response;
    } catch (error) {
        if (error.response && error.response.data && error.response.data.messages && error.response.data.messages.length > 0) {
            toast.error(error.response.data.messages[0]);
        } else {
            toast.error('Ocorreu um erro ao cadastrar!');
        }
    }
}

export const handleDelete = async (id, path) => {
    try {
        const token = Cookies.get("auth_token");

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` 
        };

        const response = await axios.delete(`http://localhost:8080/api/${path}?id=${id}`, { headers })
        console.log(response)
        if(response.status === 204) toast.success('Operação realizada com sucesso!');
        return response
    } catch (error) {
        if (error.response && error.response.data && error.response.data.messages && error.response.data.messages.length > 0) {
            toast.error(error.response.data.messages[0]);
        } else {
            toast.error('Ocorreu um erro ao deletar!');
        }
    }
}