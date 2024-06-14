import React, { createContext, useState, useEffect } from 'react';
import { handleLogin } from '@/services/axios';
import axios from 'axios';
import { useRouter } from 'next/router';
import jwt from "jsonwebtoken"

const AuthContext = createContext();

function AuthProvider({ children }) {
    const [auth, setAuth] = useState(false);
    const router = useRouter();

    useEffect(() => {
        const token = localStorage.getItem('token');
    
        console.log(isTokenValid(token));
        console.log(token)
        if (!token) {
            router.push('/');
        } else {
            setAuth(true);
            setAuthToken(token); // Configurar o token no Axios
        }
    }, [auth]);

    const isTokenValid = (token) => {
        try {
            const decoded = jwt.decode(token);
            console.log('Decoded token:', decoded);

            const now = Date.now().valueOf() / 1000;
            console.log('Current time:', now);

            if (typeof decoded.exp !== 'undefined' && decoded.exp < now) {
                console.log('Token expired');
                return false; // Token expirado
            }
            return true;
        } catch (error) {
            console.error('Error decoding token:', error);
            return false; // Token inválido
        }
    };

    const setAuthToken = (token) => {
        if (token) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        } else {
            delete axios.defaults.headers.common['Authorization'];
        }
    };

    const login = async (data) => {
        try {
            console.log(data)
            const res = await handleLogin(data);
            console.log('Login response:', res);

            // Log da resposta do login
            const token = res.data.token;
            localStorage.setItem('token', token);
            setAuthToken(token);
            setAuth(true);
            console.log('Redirecionando para /home');
            router.push('/home');
        } catch (e) {
            console.log('Erro durante o login:', e);
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        setAuthToken(null);
        setAuth(false);
        router.push('/');
    };

    return (
        <AuthContext.Provider value={{ auth, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export { AuthContext, AuthProvider }