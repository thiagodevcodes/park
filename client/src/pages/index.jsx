import Head from "next/head";
import Image from "next/image";
import styles from "@/styles/Login.module.css";
import { useState, useContext, useEffect } from "react";
import Link from "next/link";
import axios from "axios";
import Cookies from 'js-cookie';
import UserContext from "@/contexts/UserContext";



import { useRouter } from "next/router";

export default function Home() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { authenticated } = useContext(UserContext)
  const router = useRouter()


  useEffect(() => {
    if (authenticated && typeof window !== "undefined") {
      router.replace("/home")
    }
  }, [authenticated, router])

  const handleLoginChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const login = async (username, password) => {
    try {
      const response = await axios.post(`http://localhost:8080/api/auth/login`, {username, password});

      if (response.status === 200 && response.data.token) {
        const expires = new Date(Date.now() + 2 * 60 * 60 * 1000);
        Cookies.set('auth_token', response.data.token, { expires })

        router.replace('/home')
      };

      return response.data
    } catch (error) {
        console.error('Erro ao buscar dados:', error);
    }
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
            <Image priority src={"/img/Parking.svg"} alt="Logo do SysPark" width={100} height={100}/>
            <h1>SysPark</h1>
          </div>
          
          <div className={styles.inputContainer}>
            <div>
              <input value={username} onChange={handleLoginChange} placeholder="Digite seu usuário..." type="text" />
            </div>
            <div>    
              <input value={password} onChange={handlePasswordChange} placeholder="Digite sua senha..." type="password" />
            </div>
          </div>

          <button onClick={() => login(username, password)}>Entrar</button>
          <Link href={"#"}>Esqueceu sua senha?</Link>
        </div>
      </section>

    </>
  );
}
