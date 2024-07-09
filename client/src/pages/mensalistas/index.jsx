import "react-toastify/dist/ReactToastify.css";
import styles from "../../styles/Mensalistas.module.css";
import tableStyle from "../../components/Table/table.module.css"
import { useState, useEffect } from "react";
import Head from "next/head";
import Link from "next/link";
import Image from "next/image";

import { ToastContainer } from "react-toastify";
import { fetchData } from "@/services/axios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCalendarDays, faCirclePlus, faPenToSquare, faCircleExclamation, faCircleCheck } from "@fortawesome/free-solid-svg-icons";

import Pagination from "@/components/Pagination";
import InputForm from "@/components/InputForm";
import Table from "@/components/Table";
import Modal from "@/components/Modal";
import Button from "@/components/Button";
import { useRouter } from "next/router";

export default function Mensalistas() {
    const [width, setWidth] = useState(0)
    const [modalOpen, setModalOpen] = useState({ post: false, update: false, delete: false, finish: false });
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [models, setModels] = useState({ customers: [] })
    const [formData, setFormData] = useState({ person: { name: null, cpf: null, email: null, phone: null }, paymentDay: null, idCustomerType: 2 })

    const router = useRouter()

    const handleInputChange = (column, event) => {
        const updatedFormData = {
            id: column === 'id' ? event.target.value : formData.id,
            idCustomerType: column === 'idCustomerType' ? event.target.value : formData.idCustomerType,
            paymentDay: column === 'paymentDay' ? event.target.value : formData.paymentDay,
            
            person: {
                name: column === "name" ? event.target.value : formData.person.name,
                cpf: column === "cpf" ? event.target.value : formData.person.cpf,
                email: column === "email" ? event.target.value : formData.person.email,
                phone: column === "phone" ? event.target.value : formData.person.phone     
            }
        };
        
        setFormData(updatedFormData);
    };

    useEffect(() => {
        fetchData("customers").then((response) => {
            if(response){
                setModels(prevValues => ({
                    ...prevValues,
                    customers: response
                }));
            }
        })

    }, [])

    return (
        <>
            <Head>
                <title>SysPark - Mensalistas</title>
                <meta name="description" content="Sistema SysPark" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/img/Parking.svg" />
            </Head>
                <div className={styles.container}>

                    <div className={styles.headerLogo}>
                        <h1 className="d-flex-center"><FontAwesomeIcon width={30} icon={faCalendarDays} />Mensalistas </h1>
                        <button onClick={() => setModalOpen({ ...modalOpen, post: true })} className={styles.addButton}>+ Adicionar</button>
                    </div>
                </div>

                {modalOpen.post &&
                    <Modal icon={faCirclePlus} action={"post"} path={"customers"} data={{...formData}} modalOpen={modalOpen}
                        title={"Adicionar"} setModalOpen={setModalOpen}>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} value={formData.person.name} />
                            <InputForm title={"CPF: "} onChange={(e) => handleInputChange("cpf", e)} value={formData.person.cpf} />
                        </div>

                        <div className={styles.modalContainer}>
                            <InputForm title={"Email: "} onChange={(e) => handleInputChange("email", e)} value={formData.person.email} />
                            <InputForm title={"Telefone: "} onChange={(e) => handleInputChange("phone", e)} value={formData.person.phone} />
                        </div>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Dia do Pagamento: "} onChange={(e) => handleInputChange("paymentDay", e)} value={formData.paymentDay} />
                        </div>
                    </Modal>
                }

                {modalOpen.update &&
                    <Modal icon={faPenToSquare} path={"customers"} data={{...formData}} modalOpen={modalOpen}
                        action={"update"} title={"Editar"} setModalOpen={setModalOpen}>
                        <div className={styles.modalContainer}>
                            <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} value={formData.person.name} />
                            <InputForm title={"CPF: "} onChange={(e) => handleInputChange("cpf", e)} value={formData.person.cpf} />
                            <InputForm title={"Email: "} onChange={(e) => handleInputChange("email", e)} value={formData.person.email} />
                            <InputForm title={"Telefone: "} onChange={(e) => handleInputChange("phone", e)} value={formData.person.phone} />
                            <InputForm title={"Dia de Pagamento: "} onChange={(e) => handleInputChange("paymentDay", e)} value={formData.paymentDay} />
                        </div>
                    </Modal>
                }

                {modalOpen.delete &&
                    <Modal icon={faCircleExclamation} path={"customers"} action={"delete"} data={formData} modalOpen={modalOpen} setModalOpen={setModalOpen}
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
                    <Table columns={["Id", "Nome", "Telefone", "Email", "CPF", "Dia Pagamento"]} width={"85%"} data={models.mensal} setModalOpen={setModalOpen} modalOpen={modalOpen} setFormData={setFormData}>
                        <tbody>
                            {models.customers.length > 0 ? (
                                models.customers.map((item) => (
                                    <tr key={item.id}>
                                        <td>{item.id}</td>
                                        <td>{item.person.name}</td>
                                        <td>{item.person.phone}</td>
                                        <td>{item.person.email}</td>
                                        <td>{item.person.cpf}</td>
                                        <td>{item.paymentDay}</td>
                                        <td>
                                            <div className={styles.buttonContainer}>
                                                <Button onClick={() => {
                                                    setModalOpen({ ...modalOpen, update: true });
                                                    setFormData({ ...item });
                                                }} imgUrl={"/icons/Edit.svg"} bgColor={"#E9B500"} padding={"2px"} />

                                                <Button onClick={() => {
                                                    setModalOpen({ ...modalOpen, finish: true });
                                                    setFormData({ ...item });
                                                }} imgUrl={"/icons/Done.svg"} bgColor={"#00bd1f"} padding={"2px"} />

                                                <Button onClick={() => {
                                                    setModalOpen({ ...modalOpen, delete: true });
                                                    setFormData({ ...item });
                                                }} imgUrl={"/icons/Remove.svg"} bgColor={"#FF0000"} padding={"2px"} />

                                                { router.pathname === "/mensalistas" &&
                                                    <Link href={`/mensalistas/vehicles/${item.id}`} className={`${styles.bgBlue} ${styles.actionButton}`}>
                                                        <Image src={"/icons/Cars.svg"} width={30} height={30} alt="Icone Cars" />
                                                    </Link>
                                                }
                                            </div>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr style={{ textAlign: "center" }}>
                                    <td colSpan={6}>Não possui dados</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>

                    {models.customers.length > 0 &&
                        <Pagination
                            currentPage={currentPage}
                            setCurrentPage={setCurrentPage}
                            totalPages={totalPages}
                        />
                    }
                </div>
                <ToastContainer />

          
        </>
    );
}
