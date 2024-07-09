import Head from "next/head";
import Image from "next/image";
import styles from "@/styles/Login.module.css";
import { useState } from "react";
import Link from "next/link";

export default function Home() {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");

  const handleLoginChange = (event) => {
    setLogin(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

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
              <input value={login} onChange={handleLoginChange} placeholder="Digite seu usuário..." type="text" />
            </div>
            <div>    
              <input value={password} onChange={handlePasswordChange} placeholder="Digite sua senha..." type="password" />
            </div>
          </div>

          <button>Entrar</button>
          <Link href={"#"}>Esqueceu sua senha?</Link>
        </div>
      </section>

    </>
  );
}
