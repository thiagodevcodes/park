import "react-toastify/dist/ReactToastify.css";
import styles from "../../styles/Mensalistas.module.css";
import { useState, useEffect } from "react";
import Head from "next/head";
import Link from "next/link";
import Image from "next/image";


import { ToastContainer } from "react-toastify";
import { fetchDataPage } from "@/services/axios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCalendarDays, faCirclePlus, faPenToSquare, faCircleExclamation, faCircleCheck } from "@fortawesome/free-solid-svg-icons";


import Pagination from "@/components/Pagination";
import InputForm from "@/components/InputForm";
import Layout from "@/components/Layout";
import Table from "@/components/Table";
import Modal from "@/components/Modal";
import Button from "@/components/Button";


export default function Mensalistas() {
    const [width, setWidth] = useState(0)
    const [modalOpen, setModalOpen] = useState({ post: false, update: false, delete: false, finish: false });
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [models, setModels] = useState({ mensal: [] })
    const [formData, setFormData] = useState({ name: null, cpf: null, email: null, phone: null, paymentDay: null, clientType: 2 })

    const handleInputChange = (column, event) => {
        setFormData({
            ...formData,
            [column]: event.target.value,
        });
    };

    useEffect(() => {
        fetchDataPage(5, currentPage, "customers/mensalistas")
            .then(({ content, totalPages }) => {
                setModels(prevModels => ({
                    ...prevModels,
                    mensal: content
                }));
                setTotalPages(totalPages);
            })
            .catch(error => {
                console.error("Erro ao carregar dados:", error);
            });
    }, [currentPage, fetchDataPage]);

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
                        <h1 className="d-flex-center"><FontAwesomeIcon width={30} icon={faCalendarDays} />Mensalistas </h1>
                        <button onClick={() => setModalOpen({ ...modalOpen, post: true })} className={styles.addButton}>+ Adicionar</button>
                    </div>
                </div>

                {modalOpen.post &&
                    <Modal icon={faCirclePlus} action={"post"} path={"customers"} data={formData} modalOpen={modalOpen}
                        title={"Adicionar"} setModalOpen={setModalOpen}>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} value={formData.name} />
                            <InputForm title={"CPF: "} onChange={(e) => handleInputChange("cpf", e)} value={formData.cpf} />
                        </div>

                        <div className={styles.modalContainer}>
                            <InputForm title={"Email: "} onChange={(e) => handleInputChange("email", e)} value={formData.email} />
                            <InputForm title={"Telefone: "} onChange={(e) => handleInputChange("phone", e)} value={formData.phone} />
                        </div>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Dia do Pagamento: "} onChange={(e) => handleInputChange("paymentDay", e)} value={formData.paymentDay} />
                        </div>
                    </Modal>
                }

                {modalOpen.update &&
                    <Modal icon={faPenToSquare} path={"customers/mensalistas"} data={formData} modalOpen={modalOpen}
                        action={"update"} title={"Editar"} setModalOpen={setModalOpen}>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} value={formData.name} />
                            <InputForm title={"CPF: "} onChange={(e) => handleInputChange("cpf", e)} value={formData.cpf} />
                            <InputForm title={"Email: "} onChange={(e) => handleInputChange("email", e)} value={formData.email} />
                            <InputForm title={"Telefone: "} onChange={(e) => handleInputChange("phone", e)} value={formData.phone} />
                            <InputForm title={"Dia de Pagamento: "} onChange={(e) => handleInputChange("paymentDay", e)} value={formData.paymentDay} />
                        </div>
                    </Modal>
                }

                {modalOpen.delete &&
                    <Modal icon={faCircleExclamation} path={"customers"} action={"update"} data={formData} modalOpen={modalOpen} setModalOpen={setModalOpen}
                        title={"Excluir"}>
                        <p style={{ textAlign: "center", marginBottom: "10px" }}>Tem certeza que deseja excluir o cliente mensalista?</p>
                    </Modal>
                }

                {modalOpen.finish &&
                    <Modal icon={faCircleCheck} action={"update"} path={"customers/finish"} data={formData} modalOpen={modalOpen} setModalOpen={setModalOpen} title={"Finalizar"}>
                        <p style={{ textAlign: "center", marginBottom: "10px" }}>Deseja realmente finalizar esse cliente mensalista?</p>
                        {formData.idCustomerType == 1 &&
                            <div style={{ marginBottom: "10px" }}>
                                <InputForm type="number" title="Valor pago R$:" onChange={(e) => handleInputChange("totalPrice", e)} value={formData.totalPrice} />
                            </div>
                        }
                    </Modal>
                }

                <div className={styles.box}>
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
                                                <Button onClick={() => { setModalOpen({ ...modalOpen, update: true }) 
                                                    setFormData({ ...item })}} imgUrl={"/icons/Edit.svg"} bgColor={"#E9B500"} padding={"2px"}/>

                                                <Button onClick={() => { setModalOpen({ ...modalOpen, finish: true }) 
                                                    setFormData({ ...item })}} imgUrl={"/icons/Done.svg"} bgColor={"#00bd1f"} padding={"2px"}/>

                                                <Button onClick={() => {setModalOpen({ ...modalOpen, delete: true })
                                                    setFormData({ ...item })}} imgUrl={"/icons/Remove.svg"} bgColor={"#FF0000"} padding={"2px"}/>

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

                    {models.mensal.length > 0 ?
                        <Pagination
                            currentPage={currentPage}
                            setCurrentPage={setCurrentPage}
                            totalPages={totalPages}
                        /> :
                        null
                    }
                </div>
                <ToastContainer />

            </Layout>
        </>
    );
}
