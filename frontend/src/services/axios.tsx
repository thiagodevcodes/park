import axios from "axios";

interface User {
    id: number,
    username: string;
    password: string;
    person: Person;
    role: number;
}

interface Person {
    name: string;
    email: string;
    phone: string;
    cpf: string;
}

interface Page<User> {
    content: User[];
    pageable: Pageable;
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
    sort: Sort;
    numberOfElements: number;
}

interface Pageable {
    pageNumber: number;
    pageSize: number;
    offset: number;
    paged: boolean;
    unpaged: boolean;
}

interface Sort {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
}

const baseUrl = "http://localhost:8080"

const handleCreate = async (data: any, token: string | undefined, url: string) => {
    let message = ""

    try {
        const response = await axios({
            method: "POST",
            url: baseUrl + `/api/${url}`,
            data: data,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.status === 200) {
            return { success: true, message: "Criado com sucesso", data: response.data };
        } 
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                message = "Usuário não autorizado";
            } else if (error.response.status === 422) {
                message = "Dado já existente";
            } else {
                message = error.response.data.messages;
            }
        } else {
            message = "Erro ao cadastrar.";
        }

        return { success: false, message: message };
    }
};

const handleUpdate = async (data: any, token: string | undefined, url: string, id: number | undefined) => {
    let message = ""
    try {
        if (!id) return
        

        const response = await axios({
            method: "PUT",
            url: baseUrl + `/api/${url}?id=${id}`,
            data: data,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.status === 200) {
            return { success: true, message: "Atualizado com sucesso", data: response.data };
        } 

    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                message = "Usuário não autorizado";
            } else if (error.response.status === 422) {
                message = "Dado já existente";
            } else {
                message = error.response.data.messages;
            }
        } else {
            message = "Erro ao cadastrar.";
        }

        return { success: false, message: message };
    }
};

const fetchDataAll = async (token: string | undefined, currentPage: number, url: string, params?: string) => {
    let message = ""
    try {
        const response = await axios.get<Page<User>>(`${baseUrl}/api/${url}?${params}&size=${5}&page=${currentPage}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            },
        });

        if (response.status === 200) {
            return response.data
        }

    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                message = "Usuário não autorizado";
            } else if (error.response.status === 422) {
                message = "Dado já existente";
            } else {
                message = error.response.data.messages;
            }
        } else {
            message = "Erro ao cadastrar.";
        }

        return { success: false, message: message };
    }
};

const fetchData = async (token: string | undefined, id: number | undefined, url: string) => {
    let message = ""
    try {
        if (!id) return

        const response = await axios.get(baseUrl + `/api/${url}/find?id=${id}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            },
        });

        if (response.status === 200) {
            return response.data
        }
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                message = "Usuário não autorizado";
            } else if (error.response.status === 422) {
                message = "Dado já existente";
            } else {
                message = error.response.data.messages;
            }
        } else {
            message = "Erro ao cadastrar.";
        }

        return { success: false, message: message };
    }
};

const handleDelete = async (token: string | undefined, id: number | undefined, url: string) => {
    let message = ""
    try {
        if (!id) return

        const response = await axios.delete(baseUrl + `/api/${url}?id=${id}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            },
        });

        if (response.status === 204) {
            return { success: true, message: "Deletado com sucesso", data: response.data };
        } 
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                message = "Usuário não autorizado";
            } else if (error.response.status === 422) {
                message = "Dado já existente";
            } else {
                message = error.response.data.messages;
            }
        } else {
            message = "Erro ao cadastrar.";
        }

        return { success: false, message: message };
    }
}

export { handleCreate, handleUpdate, fetchData, fetchDataAll, handleDelete }