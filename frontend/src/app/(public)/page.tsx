'use client'

import { signIn } from "next-auth/react";
import { useState, FormEvent } from "react";
import { NextPage } from "next";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

const Admin: NextPage = () => {
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [error, setError] = useState<string>("");
    const router = useRouter();

    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        
        const result = await signIn('credentials', {
            email,
            password,
            redirect: false
        });

        if (result?.error) {
            if (result.status === 401) {
                setError("Usuário ou senha inválidos");
            } else {
                setError("Não foi possível realizar o login");
            }   
            return
        }
        router.refresh()   
    }

    return (
        <div className="flex flex-col gap-2 justify-center items-center h-screen">
            
            <form onSubmit={handleSubmit}>
                <div className="flex flex-col gap-4 justify-center items-center p-10">
                    <div className="flex items-center flex-col">
                        <Image src={"/img/Parking.svg"} width={80} height={80} alt="logo"/>
                        <p className="font-bold text-3xl">SysPark</p>
                    </div>

                    <Input type="text" placeholder="Digite seu email..." name="username" id="username" className=" w-64" onChange={(e) => setEmail(e.target.value)}/>        
                    <Input type="password" placeholder="Digite sua senha..." name="username" id="username" className="w-64" onChange={(e) => setPassword(e.target.value)} />
                    
                    { error && <div className="text-red-700 text-sm">{error}</div>}
                    
                    <Button type="submit" className="w-full bg-black">Entrar</Button>
                </div>
                
            </form>
        </div>
    );
}

export default Admin