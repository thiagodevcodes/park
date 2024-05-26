import axios from "axios";
import { toast } from "react-toastify";

export const fetchData = async (path) => {
    try {
        const response = await axios.get(`http://localhost:8080/api/${path}`);
        return response.data
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
};

export const fetchDataPage = async (limit, currentPage, path) => {
    try {
        const response = await axios.get(`http://localhost:8080/api/${path}?size=${limit}&page=${currentPage}`);
        return response.data
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
};

export const fetchDataAll = async (path) => {
    try {
        if (!path) return
        const response = await axios.get(`http://localhost:8080/api/orcamento/${path}/all`);
        return response.data
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
};

export const fetchDataById = async (id, path) => {
    try {
        const response = await axios.get(`http://localhost:8080/api/orcamento/${path}/${id}`);
        return response.data
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
        toast.error("Erro ao buscar dado!")
    }
};

export const handleCreate = async (data, path) => {
    try {
        const response = await axios.post(`http://localhost:8080/api/${path}`, data);
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
        const response = await axios.patch(`http://localhost:8080/api/${path}/${id}`, data);
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
        const response = await axios.delete(`http://localhost:8080/api/${path}/${id}`)
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