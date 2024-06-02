import "react-toastify/dist/ReactToastify.css";
import styles from "../../styles/Mensalistas.module.css";
import { useState, useEffect } from "react";
import Head from "next/head";

import { ToastContainer } from "react-toastify";
import { formatDate } from "@/services/utils";
import { handleUpdate, fetchData, fetchDataPage } from "@/services/axios";

import Pagination from "@/components/Pagination";
import InputForm from "@/components/InputForm";
import Layout from "@/components/Layout";
import Table from "@/components/Table";
import Modal from "@/components/Modal";
import Select from "@/components/Select";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSquareParking, faCirclePlus, faPenToSquare, faCircleExclamation, faCircleCheck } from "@fortawesome/free-solid-svg-icons";


export default function Users() {
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [width, setWidth] = useState(0)
    const [modalOpen, setModalOpen] = useState({ post: false, update: false, delete: false, finish: false });
    const [models, setModels] = useState({ users: [], userTypes: ["Admin", "Default"] })
    const [formData, setFormData] = useState({ name: null, phone: null, email: null, username: null, password: null, userType: null })

    const handleInputChange = (column, event) => {
        setFormData({
            ...formData,
            [column]: event.target.value,
        });
    };

    useEffect(() => {
        const fetchDataVehicles = async () => {
            try {
                const usersResponse = await fetchData("users/active");
                if(usersResponse) {
                    setModels(prevValues => ({
                        ...prevValues,
                        users: usersResponse.content
                    }));
                    setTotalPages(usersResponse.totalPages)
                    console.log(usersResponse)
                }
              
            } catch (error) {
                console.error("Erro ao carregar dados dos usuários:", error);
            }
        }
        fetchDataVehicles();
    }, []);

    useEffect(() => {
        const fetchDataVehicles = async () => {
            try {
                const userTypesResponse = await fetchData(`user_types`);

                setModels(prevValues => ({
                    ...prevValues,
                    userTypes: userTypesResponse
                }));
                console.log(userTypesResponse)
            } catch (error) {
                console.error("Erro ao carregar dados dos usuários:", error);
            }
        }
        fetchDataVehicles();
    }, []);

    return (
        <>
            <Head>
                <title>SysPark - Usuários</title>
                <meta name="description" content="Sistema SysPark" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/img/Parking.svg" />
            </Head>
            <div className={styles.container}>
                <div className={styles.headerLogo}>
                    <h1 className="d-flex-center" ><FontAwesomeIcon icon={faSquareParking} width={30} />Usuários</h1>
                    <button onClick={() => setModalOpen({ ...modalOpen, post: true })} className={styles.addButton}>+ Adicionar</button>
                </div>
            </div>

            {modalOpen.post &&
                <Modal icon={faCirclePlus} action={"post"} path={`users`} data={formData} modalOpen={modalOpen}
                    title={"Adicionar"} setModalOpen={setModalOpen}>
                    <div className={styles.modalContainer}>
                        <Select onChange={(e) => handleInputChange("userType", e)} noOption={true}
                            value={formData.userType} title="Tipo de Usuário" data={models.userTypes} />
                    </div>

                    <div className={styles.modalContainer}>
                        <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} />
                        <InputForm title={"Email: "} onChange={(e) => handleInputChange("email", e)} />
                        <InputForm title={"Telefone: "} onChange={(e) => handleInputChange("phone", e)} />
                        <InputForm title={"Nome de Usuário: "} onChange={(e) => handleInputChange("username", e)} />
                        <InputForm title={"Senha: "} onChange={(e) => handleInputChange("password", e)} type={"password"}/>
                        <InputForm title={"Confirmação de Senha: "} onChange={(e) => handleInputChange("confirmPassword", e)} type={"password"}/>
                    </div>
                    
                </Modal>
            }

            {modalOpen.update &&
                <Modal icon={faPenToSquare} action={"update"} path={"users"}
                    data={formData} modalOpen={modalOpen} title={"Editar"} setModalOpen={setModalOpen}>
                    <div className={styles.modalContainer}>
                        <Select onChange={(e) => handleInputChange("userType", e)} noOption={true}
                            value={formData.userType} title="Tipo de Usuário" data={models.userTypes} />
                    </div>
   
                    <div className={styles.modalContainer}>
                        <InputForm value={formData.name} title={"Nome: "} onChange={(e) => handleInputChange("name", e)} />
                        <InputForm value={formData.email} title={"Email: "} onChange={(e) => handleInputChange("email", e)} />
                        <InputForm value={formData.phone} title={"Telefone: "} onChange={(e) => handleInputChange("phone", e)} />
                        <InputForm value={formData.username} title={"Nome de Usuário: "} onChange={(e) => handleInputChange("username", e)} />
                    </div>
                </Modal>

            }

            {modalOpen.delete &&
                <Modal icon={faCircleExclamation} action={"delete"} path={"users"} data={formData} modalOpen={modalOpen} setModalOpen={setModalOpen} title={"Excluir"}>
                    <p style={{ textAlign: "center", marginBottom: "10px" }}>Tem certeza que deseja excluir o usuário?</p>
                </Modal>
            }

            {modalOpen.finish &&
                <Modal icon={faCircleCheck} action={"update"} path={"users/finish"} data={formData} modalOpen={modalOpen} setModalOpen={setModalOpen} title={"Finalizar"}>
                    <p style={{ textAlign: "center", marginBottom: "10px" }}>Deseja realmente finalizar esse usuário?</p>
                </Modal>
            }
            <div className={styles.box}>
                <Table columns={["Id", "Usuário", "Nome", "Telefone", "Email", "Tipo de Usuário"]} data={models.users} width={"85%"} setModalOpen={setModalOpen} modalOpen={modalOpen} setFormData={setFormData} />

                {models.users.length > 0 &&
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
