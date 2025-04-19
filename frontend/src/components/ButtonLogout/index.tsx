'use client'

import { signOut } from "next-auth/react";
import { ChevronDown } from "lucide-react"

const ButtonLogout: React.FC = () => {

    const logout = async () => {
        await signOut({
          redirect: true
        });
    }

    return (
        <button onClick={logout} className="w-full text-start">
            Sair
        </button>
    );
}

export default ButtonLogout;