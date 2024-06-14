import Head from "next/head";
import { useState, useEffect, useContext } from "react";
import styles from "../../styles/Home.module.css";
import Layout from "@/components/Layout";
import { fetchData } from "@/services/axios";

import { AuthContext } from "@/contexts/UserContext";
import { useRouter } from "next/router";
import { getServerSession } from "next-auth";
import { redirect } from "next/dist/server/api-utils";

export default async function Home() {
    const [width, setWidth] = useState(0)
    const [model, setModel] = useState([])
    const [ocuppied, setOcuppied] = useState(0)
    const [notOcuppied, setNotOcuppied] = useState(0)
    const [totalPrice, setTotalPrice] = useState(0)
    const { auth } = useContext(AuthContext);
    const router = useRouter()

    const session = await getServerSession();

    if(!session) {
        redirect("/")
    }
    // useEffect(() => {
    //     fetchData("vacancies").then((response) => {
    //         if(response){
    //             setModel(response.vacanciesList);
    //             setOcuppied(response.vacanciesOccupied);
    //             setNotOcuppied(response.vacanciesNotOccupied);
    //         }

    //     })
    // }, [notOcuppied, ocuppied])

    // useEffect(() => {
    //     fetchData("tickets/registerdate").then((res) => {
    //         let totalPriceSum = 0;

    //         if(res) {
    //             res.forEach(element => {
    //                 totalPriceSum += element.totalPrice;
    //             });
    
    //             setTotalPrice(totalPriceSum);
    //         }

    //     });
    // }, []);

    // useEffect(() => {
    //     const handleResize = () => {
    //         setWidth(window.innerWidth);
    //     };

    //     if (typeof window !== 'undefined') {
    //         window.addEventListener('resize', handleResize);

    //         setWidth(window.innerWidth);

    //         return () => {
    //             window.removeEventListener('resize', handleResize);
    //         };
    //     }
    // }, [width])

    return (
        <>
            <Head>
                <title>SysPark - Home</title>
                <meta name="description" content="Sistema SysPark" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/img/Parking.svg" />
            </Head>
                <div className={styles.container}>
                    <h1>Bem-vindo, {session.user.name}</h1>

                    <div className={styles.infoContainer}>
                        <div className={styles.info}>
                            <div className={styles.infoChild}>
                                <div className={styles.textContainer}>
                                    <h2>Vagas Livres</h2>
                                    <p> {notOcuppied} </p>
                                </div>

                                <div className={styles.textContainer}>
                                    <h2>Vagas Ocupadas</h2>
                                    <p>{ocuppied}</p>
                                </div>
                            </div>

                            <div className={styles.infoChild}>
                                <div className={styles.textContainer}>
                                    <h2>Total Diário Rotativo</h2>
                                    <p>{totalPrice.toLocaleString("pt-br", {
                                        style: "currency",
                                        currency: "BRL"
                                    })}</p>
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
        </>
    );
}
