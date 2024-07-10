import { useEffect ,createContext, useState } from "react";
import { isValidToken } from "@/services/utils";
import Cookies from "js-cookie";

const AuthContext = createContext({})

export const AuthProvider = ({ children }) => {
    const [authenticated, setAuthenticated] = useState(false);
    const [checkingAuth, setCheckingAuth] = useState(true); // Estado para controlar a verificação

    useEffect(() => {
        const checkAuth = async () => {
            const authToken = Cookies.get('auth_token');
            if (authToken) {
                const isValid = await isValidToken(authToken);
                setAuthenticated(isValid); // Define o estado de autenticação com base no resultado da validação
            } else {
                setAuthenticated(false);
            }
            setCheckingAuth(false); // Marca que a verificação foi concluída
        };

        checkAuth();
    }, []);

    // Aguarda até que a verificação seja concluída antes de renderizar o contexto
    if (checkingAuth) {
        return <div>Loading...</div>; // Ou um spinner de carregamento
    }

    return (
        <AuthContext.Provider value={{ authenticated, setAuthenticated }}>
            {children}
        </AuthContext.Provider>
    )
}



export default AuthContext