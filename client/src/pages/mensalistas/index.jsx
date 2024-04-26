import Head from "next/head";
import { useState, useEffect } from "react";
import styles from "../../styles/Mensalistas.module.css";
import Layout from "@/components/Layout";
import Table from "@/components/Table";
import Modal from "@/components/Modal";
import { fetchDataPage, handleCreate, handleUpdate, handleDelete } from "@/services/axios";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useRouter } from "next/router";
import Link from "next/link";
import Image from "next/image";
import Pagination from "@/components/Pagination";


export default function Mensalistas() {
    const [width, setWidth] = useState(0)
    const [modalOpen, setModalOpen] = useState({ post: false, update: false, delete: false });
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [models, setModels] = useState({ mensal: [] })
    const [formData, setFormData] = useState({ name: null, cpf: null, email: null, phone: null, paymentDay: null, clientType: 2 })
    const router = useRouter()

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

    const submitDelete = (e, id, path) => {
        e.preventDefault();
        if (id) {
            try {
                handleDelete(id, path).then(() => {
                    setModalOpen({...modalOpen, delete: false})
                    setTimeout(() => {
                        router.reload(); 
                    }, 3000);
                });
            } catch (error) {
                console.error("Erro ao criar:", error);
            }
        }
    }

    const handleInputChange = (column, event) => {
        setFormData({
            ...formData,
            [column]: event.target.value,
        });

    };

    useEffect(() => {
        Promise.all([
            fetchDataPage(5, currentPage, "customers/mensalistas")
        ]).then(([mensalResponse]) => {
            setModels(prevModels => ({
                ...prevModels,
                mensal: mensalResponse.content,
            }));
            setTotalPages(mensalResponse.totalPages);
        }).catch(error => {
            console.error("Erro ao carregar dados:", error);
        });
    }, [currentPage]);

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
                <title>SysPark - Mensalistas</title>
                <meta name="description" content="Sistema SysPark" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/img/Parking.svg" />
            </Head>

            <Layout>

                <div className={styles.container}>

                    <div className={styles.headerLogo}>
                        <h1>Mensalistas </h1>
                        <button onClick={() => setModalOpen({ ...modalOpen, post: true })} className={styles.addButton}>+ Adicionar</button>
                    </div>
                </div>

                {modalOpen.post &&
                    <Modal handleSubmit={(e) => handleSubmit(e, "customers", formData)} title={"Adicionar"} setModalOpen={setModalOpen}>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} value={formData.name}/>
                            <InputForm title={"CPF: "} onChange={(e) => handleInputChange("cpf", e)} value={formData.cpf}/>
                        </div>

                        <div className={styles.modalContainer}>
                            <InputForm title={"Email: "} onChange={(e) => handleInputChange("email", e)} value={formData.email}/>
                            <InputForm title={"Telefone: "} onChange={(e) => handleInputChange("phone", e)} value={formData.phone}/>
                        </div>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Dia do Pagamento: "} onChange={(e) => handleInputChange("paymentDay", e)} value={formData.paymentDay}/>
                        </div>
                    </Modal>
                }

                {modalOpen.update &&
                    <Modal handleSubmit={(e) => submitUpdated(e, formData.id, "customers/mensalistas", formData)} title={"Adicionar"} setModalOpen={setModalOpen}>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} value={formData.name}/>
                            <InputForm title={"CPF: "} onChange={(e) => handleInputChange("cpf", e)} value={formData.cpf}/>
                        </div>

                        <div className={styles.modalContainer}>
                            <InputForm title={"Email: "} onChange={(e) => handleInputChange("email", e)} value={formData.email}/>
                            <InputForm title={"Telefone: "} onChange={(e) => handleInputChange("phone", e)} value={formData.phone}/>
                        </div>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Dia de Pagamento: "} onChange={(e) => handleInputChange("paymentDay", e)} value={formData.paymentDay}/>
                        </div>
                    </Modal>
                }

                {modalOpen.delete &&
                    <Modal setModalOpen={setModalOpen} handleSubmit={(e) => submitDelete(e, formData.id, "customers")} title={"Excluir"}>
                        <p>Tem certeza que deseja excluir o cliente mensalista?</p>
                    </Modal>
                }

                <Table columns={["Nome", "Telefone", "Email", "CPF", "Dia Pagamento"]}>
                    <tbody>
                        {models.mensal && models.mensal.length > 0 ? (
                            models.mensal.map((item) => (
                                <tr key={item.id}>
                                    <>
                                        <td>{item.name}</td>
                                        <td>{item.phone}</td>
                                        <td>{item.email}</td>
                                        <td>{item.cpf}</td>
                                        <td>{item.paymentDay}</td>
                                    </>

                                    <td>
                                        <div className={styles.buttonContainer}>
                                            <button onClick={() => {
                                                setModalOpen({ ...modalOpen, update: true })
                                                setFormData({ ...item })
                                            }} className={`${styles.bgYellow} ${styles.actionButton}`}>
                                                <Image src={"/icons/Edit.svg"} width={30} height={30} alt="Icone Edit" />
                                            </button>
                                            <button onClick={() => handleUpdate(item.id, "customers/finish", { ...item, isActive: false })} className={`${styles.bgGreen} ${styles.actionButton}`}>
                                                <Image src={"/icons/Done.svg"} width={30} height={30} alt="Icone Remove" />
                                            </button>
                                            <button onClick={() => {
                                                setModalOpen({ ...modalOpen, delete: true })
                                                setFormData({...item})
                                            }} className={`${styles.bgRed} ${styles.actionButton}`}>
                                                <Image src={"/icons/Remove.svg"} width={30} height={30} alt="Icone Remove" />
                                            </button>
                                            <Link href={`/mensalistas/vehicles/${item.id}`} className={`${styles.bgBlue} ${styles.actionButton}`}>
                                                <Image src={"/icons/Cars.svg"} width={30} height={30} alt="Icone Cars" />
                                            </Link>
                                        </div>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={7}>Não possui dados!</td>
                            </tr>
                        )}
                    </tbody>
                </Table>
                <ToastContainer />
                {models.mensal.length > 0 ?
                    <Pagination
                        currentPage={currentPage}
                        setCurrentPage={setCurrentPage}
                        totalPages={totalPages}
                    /> :
                    null
                }
            </Layout>
        </>
    );
}
