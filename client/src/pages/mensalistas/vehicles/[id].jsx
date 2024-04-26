import { useEffect, useState } from "react";
import Head from "next/head";
import { useRouter } from "next/router";
import { fetchData } from "@/services/axios";
import "react-toastify/dist/ReactToastify.css";
import Table from "@/components/Table";
import Layout from "@/components/Layout";
import { ToastContainer } from "react-toastify";
import Modal from "@/components/Modal";
import styles from "../../../styles/Mensalistas.module.css"
import Image from "next/image";
import { handleUpdate, handleCreate } from "@/services/axios";
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

    const submitUpdated = (e, id, path, data) => {
        e.preventDefault();
        if (id) {
            try {
                handleUpdate(id, path, data).then(() => {
                    setModalOpen({ ...modalOpen, update: false })
                    setTimeout(() => {
                        router.reload(); 
                    }, 3000);
                });
            } catch (error) {
                console.error("Erro ao criar:", error);
            }
        }
    };

    const handleSubmit = (e, path, data) => {
        e.preventDefault();
        try {
            handleCreate(data, path).then(() => {
                setModalOpen({ ...modalOpen, post: false })
                setTimeout(() => {
                    router.reload(); 
                }, 3000); 
            });
        } catch (error) {
            console.error("Erro ao criar:", error);
        }

    };

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
            fetchData(`vehicles/mensalistas/${id}`).then((response) => {
                setVehicles(response.content);
                setTotalPages(response.totalPages);
            })
        }
    }, [id])

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

                <Modal handleSubmit={(e) => handleSubmit(e, "vehicles", formData)} title={"Adicionar"} setModalOpen={setModalOpen} modalOpen={modalOpen}>
                    <div className={styles.modalContainer}>
                        <InputForm title={"Marca: "} onChange={(e) => handleInputChange("make", e)} value={formData.make}/>
                    </div>

                    <div className={styles.modalContainer}>
                        <InputForm title={"Modelo: "} onChange={(e) => handleInputChange("model", e)} value={formData.model}/>
                        <InputForm title={"Placa: "} onChange={(e) => handleInputChange("plate", e)} value={formData.plate}/>
                    </div>
                </Modal>
            }

            {modalOpen.update &&

                <Modal handleSubmit={(e) => submitUpdated(e, formData.id, "vehicles", formData)} title={"Adicionar"} setModalOpen={setModalOpen} modalOpen={modalOpen}>
                    <div className={styles.modalContainer}>
                        <InputForm title={"Marca: "} onChange={(e) => handleInputChange("make", e)} value={formData.make}/>
                    </div>

                    <div className={styles.modalContainer}>
                        <InputForm title={"Modelo: "} onChange={(e) => handleInputChange("model", e)} value={formData.model}/>
                        <InputForm title={"Placa: "} onChange={(e) => handleInputChange("plate", e)} value={formData.plate}/>
                    </div>
                </Modal>
            }

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
                                            submitUpdated(e, item.id, "vehicles", { ...item, monthlyVehicle: false })
                                        } } className={`${styles.bgRed} ${styles.actionButton}`}>
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

            {vehicles.length > 0 ?
                    <Pagination
                        currentPage={currentPage}
                        setCurrentPage={setCurrentPage}
                        totalPages={totalPages}
                    /> :
                    null
                }
            <ToastContainer />
        </Layout>
    );
};