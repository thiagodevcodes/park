import Head from "next/head";
import { useState, useEffect } from "react";
import styles from "../../styles/Home.module.css";
import Layout from "@/components/Layout";
import { fetchData } from "@/services/axios";

export default function Home() {
  const [width, setWidth] = useState(0)
  const [model, setModel] = useState([])
  const [ocuppied, setOcuppied] = useState(0)
  const [notOcuppied, setNotOcuppied] = useState(0)
  
  useEffect(() => {
    fetchData("vacancies").then((response) => {
      setModel(response.vacanciesList);   
      setOcuppied(response.vacanciesOccupied);
      setNotOcuppied(response.vacanciesNotOccupied);
    })
  }, [notOcuppied, ocuppied])

  useEffect(() => {
      const handleResize = () => {
        setWidth(window.innerWidth);
      };
  
      if (typeof window !== 'undefined') {
        window.addEventListener('resize', handleResize);
  
        setWidth(window.innerWidth);
  
        return () => {
          window.removeEventListener('resize', handleResize);
        };
      }
  }, [width])

  return (
    <>
        <Head>
            <title>SysPark - Home</title>
            <meta name="description" content="Sistema SysPark" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <link rel="icon" href="/img/Parking.svg" />
        </Head>

        <Layout>
          <div className={styles.container}>
            <h1>Bem-vindo, Thiago</h1>

            <div className={styles.infoContainer}>
              <div className={styles.info}>
                <div className={styles.infoChild}>
                  <div className={styles.textContainer}>
                    <h2>Vagas Livres</h2>
                    <p> { notOcuppied } </p>
                  </div>

                  <div className={styles.textContainer}>
                    <h2>Vagas Ocupadas</h2>
                    <p>{ ocuppied }</p>
                  </div>
                </div>

                <div className={styles.infoChild}>
                  <div className={styles.textContainer}>
                    <h2>Total Diário</h2>
                    <p>R$10,00</p>
                  </div>

                </div>
              </div>

              <div className={styles.vagasContainer}>
                <h2>Vagas</h2>
                <div className={styles.vagas}>
                  {
                    model.map((item, index) => (
                      <div key={index} className={item.situation ? styles.green : styles.red}>
                        <p >{item.id}</p>
                      </div>
                    ))
                  }
                </div>
              </div>
            </div>
          </div>
        </Layout>
    </>
  );
}
