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
        await axios
            .post(`http://localhost:8080/api/${path}`, data)
            .then((res) => {
                if(res.status === 200) 
                    toast.success('Cadastrado com sucesso!')
            })
            .catch((error) => {
                toast.error(error.response.data.messages[0])
            });
    } catch (error) {
        console.error(error);
        toast.error('Ocorreu um erro ao cadastrar!')
    }
}


export const handleUpdate = async (id, path, data) => {
    try {
        await axios.put(`http://localhost:8080/api/${path}/${id}`, data)
            .then((res) => {

                toast.success('Operação realizada com sucesso!')
            }).catch((error) => {
                toast.error(error.response.data.messages[0])
            })
    } catch (error) {
        console.error(error);
    }
}

export const handleDelete = async (id, path) => {
    try {
        await axios.delete(`http://localhost:8080/api/${path}/${id}`).then(() => {
            toast.success('Operação realizada com sucesso!');
        })
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
        toast.error("Erro ao deletar: " + error.response.data.messages);
    }
}