'use-client'

import Head from "next/head";
import Image from "next/image";
import styles from "@/styles/Login.module.css";
import { useState } from "react";
import Link from "next/link";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import { signIn } from 'next-auth/react'


export default function Home() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLoginChange = (event) => {
        setEmail(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const login = (email, password) => {
        signIn('credentials', {
            email: email,
            password: password,
            callbackUrl: '/home'
        })
    }

    return (
        <>
            <Head>
                <title>SysPark - Login</title>
                <meta name="description" content="Sistema SysPark" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/img/Parking.svg" />
            </Head>

            <section className={styles.loginSection}>
                <div className={styles.loginContainer}>
                    <div className={styles.logo}>
                        <Image priority src={"/img/Parking.svg"} alt="Logo do SysPark" width={100} height={100} />
                        <h1>SysPark</h1>
                    </div>
                    <form className={styles.inputContainer} method="POST" onSubmit={login}>
                        <div>
                            <input name="email" value={email} onChange={handleLoginChange} placeholder="Digite seu usuário..." type="text" />
                        </div>
                        <div>
                            <input name="password" value={password} onChange={handlePasswordChange} placeholder="Digite sua senha..." type="password" />
                        </div>
                        <button type="submit">Entrar</button>
                        <Link href={"#"}>Esqueceu sua senha?</Link>
                    </form>
                </div>
            </section>
            <ToastContainer />
        </>
    );
}
