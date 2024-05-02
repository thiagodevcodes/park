import "react-toastify/dist/ReactToastify.css";
import styles from "../../../styles/Mensalistas.module.css"
import { useEffect, useState } from "react";
import Head from "next/head";
import Image from "next/image";
import { useRouter } from "next/router";

import { ToastContainer } from "react-toastify";
import { fetchData, handleUpdate } from "@/services/axios";

import Modal from "@/components/Modal";
import Table from "@/components/Table";
import Layout from "@/components/Layout";
import Pagination from "@/components/Pagination";
import InputForm from "@/components/InputForm";


export default function VehicleById() {
    const router = useRouter();
    const id = router.query.id;

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [vehicles, setVehicles] = useState([]);
    const [modalOpen, setModalOpen] = useState({ post: false, update: false, delete: false });
    const [formData, setFormData] = useState({ plate: null, model: null, make: null, idCustomer: null, monthlyVehicle: true })

    const handleInputChange = (column, event) => {
        setFormData({
            ...formData,
            [column]: event.target.value,
        });
    };

    useEffect(() => {
        if (id) {
            setFormData(prevState => ({
                ...prevState,
                idCustomer: id
            }));
        }
    }, [id]);

    useEffect(() => {
        if (id) {
            fetchData(`vehicles/mensalistas/${id}`)
                .then((response) => {
                    setVehicles(response.content);
                    setTotalPages(response.totalPages);
                })
                .catch(error => {
                    console.error("Erro ao carregar dados dos veículos:", error);
                });
        }
    }, [id, fetchData]);

    if (!vehicles) {
        return <div>Ação não encontrada</div>;
    }

    return (
        <Layout>
            <div className={styles.container}>
                <div className={styles.headerLogo}>
                    <h1>Veículos</h1>
                    <button onClick={() => setModalOpen({ ...modalOpen, post: true })} className={styles.addButton}>+ Adicionar</button>
                </div>
            </div>

            <Head>
                <title>SysPark - Veiculos</title>
                <meta name="description" content="Sistema SysPark" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/img/Parking.svg" />
            </Head>

            {modalOpen.post &&

                <Modal action={"post"} data={formData} path={"vehicles"} title={"Adicionar"} setModalOpen={setModalOpen} modalOpen={modalOpen}>
                    <div className={styles.modalContainer}>
                        <InputForm title={"Marca: "} onChange={(e) => handleInputChange("make", e)} value={formData.make} />
                    </div>

                    <div className={styles.modalContainer}>
                        <InputForm title={"Modelo: "} onChange={(e) => handleInputChange("model", e)} value={formData.model} />
                        <InputForm title={"Placa: "} onChange={(e) => handleInputChange("plate", e)} value={formData.plate} />
                    </div>
                </Modal>
            }

            {modalOpen.update &&

                <Modal action={"update"} data={formData} path={"vehicles"} title={"Adicionar"} setModalOpen={setModalOpen} modalOpen={modalOpen}>
                    <div className={styles.modalContainer}>
                        <InputForm title={"Marca: "} onChange={(e) => handleInputChange("make", e)} value={formData.make} />
                    </div>

                    <div className={styles.modalContainer}>
                        <InputForm title={"Modelo: "} onChange={(e) => handleInputChange("model", e)} value={formData.model} />
                        <InputForm title={"Placa: "} onChange={(e) => handleInputChange("plate", e)} value={formData.plate} />
                    </div>
                </Modal>
            }

            <div className={styles.box}>
                <Table tableName={"Veiculos"} columns={["Placa", "Marca", "Modelo"]} model={vehicles} colSpan={6}>
                    <tbody>
                        {vehicles && vehicles.length > 0 ? (
                            vehicles.map((item) => (
                                <tr key={item.id}>
                                    <td>{item.plate}</td>
                                    <td>{item.make}</td>
                                    <td>{item.model}</td>

                                    <td>
                                        <div className={styles.buttonContainer}>
                                            <button onClick={() => {
                                                setModalOpen({ ...modalOpen, update: true })
                                                setFormData({ ...item })
                                            }} className={`${styles.bgYellow} ${styles.actionButton}`}>
                                                <Image src={"/icons/Edit.svg"} width={30} height={30} alt="Icone Edit" />
                                            </button>
                                            <button onClick={(e) => {
                                                e.preventDefault();
                                                handleUpdate(item.id, "vehicles", { ...item, monthlyVehicle: false }).then(() => {
                                                    setModalOpen({ ...modalOpen, update: false })
                                                    setTimeout(() => router.reload(), 3000);
                                                })
                                            }} className={`${styles.bgRed} ${styles.actionButton}`}>
                                                <Image src={"/icons/Remove.svg"} width={30} height={30} alt="Icone Remove" />
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={6}>Não possui dados!</td>
                            </tr>
                        )}
                    </tbody>
                </Table>

                {vehicles.length > 0 &&
                    <Pagination
                        currentPage={currentPage}
                        setCurrentPage={setCurrentPage}
                        totalPages={totalPages}
                    />
                }
            </div>
            <ToastContainer />
        </Layout>
    );
};