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

export default function Movimentacoes() {
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [width, setWidth] = useState(0)
    const [modalOpen, setModalOpen] = useState({ post: false, update: false, delete: false, finish: false });
    const [models, setModels] = useState({ tickets: [], clients: [], vehicles: [], vacancys: [], customerTypes: [] })
    const [formData, setFormData] = useState({ name: null, plate: null, make: null, model: null, idVacancy: null, idCustomerType: 1, idCustomer: null, idVehicle: null, totalPrice: null })

    const handleInputChange = (column, event) => {
        setFormData({
            ...formData,
            [column]: event.target.value,
        });
    };

    useEffect(() => {
        const fetchDataModels = async () => {
            try {
                const [clientsResponse, ticketsResponse, vacanciesResponse, customerTypesResponse] = await Promise.all([
                    fetchData("customers/mensalistas"),
                    fetchDataPage(3, currentPage, "tickets/movimentacoes"),
                    fetchData("vacancies"),
                    fetchData("customer_type")
                ]);
                if(clientsResponse && ticketsResponse && vacanciesResponse && customerTypesResponse) {
                    setModels(prevValues => ({
                        ...prevValues,
                        clients: clientsResponse.content,
                        tickets: ticketsResponse.content,
                        vacancys: vacanciesResponse,
                        customerTypes: customerTypesResponse
                    }));              
                    setTotalPages(ticketsResponse.totalPages)
                }

            } catch (error) {
                console.error("Erro ao carregar dados:", error);
            }
        };
        fetchDataModels();
    }, [currentPage, fetchData, fetchDataPage]);

    useEffect(() => {
        const fetchDataVehicles = async () => {
            if (formData.idCustomerType == "2" && formData.idCustomer) {
                try {
                    const vehiclesResponse = await fetchData(`vehicles/mensalistas/${formData.idCustomer}`);

                    setModels(prevValues => ({
                        ...prevValues,
                        vehicles: vehiclesResponse.content
                    }));
                    console.log(vehiclesResponse.content)
                } catch (error) {
                    console.error("Erro ao carregar dados dos veículos:", error);
                }
            }
        };
        fetchDataVehicles();
    }, [formData.idCustomer, fetchData]);

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
                <title>SysPark - Movimentações</title>
                <meta name="description" content="Sistema SysPark" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/img/Parking.svg" />
            </Head>

            
                <div className={styles.container}>
                    <div className={styles.headerLogo}>
                        <h1 className="d-flex-center" ><FontAwesomeIcon icon={faSquareParking} width={30} />Movimentações</h1>
                        <button onClick={() => setModalOpen({ ...modalOpen, post: true })} className={styles.addButton}>+ Adicionar</button>
                    </div>
                </div>

                {modalOpen.post &&
                    <Modal icon={faCirclePlus} action={"post"} path={`tickets/${formData.idCustomerType == 1 ? "rotativos" : "mensalistas"}`} data={formData} modalOpen={modalOpen}
                        title={"Adicionar"} setModalOpen={setModalOpen}>
                        <div className={styles.modalContainer}>
                            <Select onChange={(e) => handleInputChange("idCustomerType", e)} noOption={false}
                                value={formData.idCustomerType} title="TIpo de Cliente" data={models.customerTypes} />
                        </div>

                        {formData.idCustomerType == 2 &&
                            <div className={styles.modalContainer}>
                                <Select onChange={(e) => handleInputChange("idCustomer", e)} noOption={true}
                                    value={formData.idCustomer} title="Cliente" data={models.clients} />

                                <Select onChange={(e) => handleInputChange("idVehicle", e)} noOption={true}
                                    value={formData.idVehicle} title="Veiculo" data={models.vehicles} />
                            </div>
                        }

                        {formData.idCustomerType == 1 &&
                            <div className={styles.modalContainer}>
                                <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} />
                                <InputForm title={"Marca: "} onChange={(e) => handleInputChange("make", e)} />
                                <InputForm title={"Modelo: "} onChange={(e) => handleInputChange("model", e)} />
                                <InputForm title={"Placa: "} onChange={(e) => handleInputChange("plate", e)} />
                            </div>
                        }

                        <div className={styles.modalContainer}>
                            <Select onChange={(e) => handleInputChange("idVacancy", e)} noOption={true}
                                value={formData.idVacancy} title="Vaga" data={models.vacancys.vacanciesNotOccupiedList} />
                        </div>
                    </Modal>
                }

                {modalOpen.update &&
                    <Modal icon={faPenToSquare} action={"update"} path={`tickets/${formData.idCustomerType == 1 ? "rotativos" : "mensalistas"}`}
                        data={formData} modalOpen={modalOpen} title={"Editar"} setModalOpen={setModalOpen}>
                        {formData.idCustomerType == 2 && models.vehicles &&
                            <div className={styles.modalContainer}>
                                <Select onChange={(e) => handleInputChange("idCustomer", e)} noOption={true}
                                    value={formData.idCustomer} title="Cliente" data={models.clients} />

                                <Select onChange={(e) => handleInputChange("idVehicle", e)} noOption={true}
                                    value={formData.idVehicle} title="Veiculo" data={models.vehicles} />
                            </div>
                        }

                        {formData.idCustomerType == 1 &&
                            <div className={styles.modalContainer}>
                                <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} value={formData.name} />
                                <InputForm title={"Marca: "} onChange={(e) => handleInputChange("make", e)} value={formData.make} />
                                <InputForm title={"Modelo: "} onChange={(e) => handleInputChange("model", e)} value={formData.model} />
                                <InputForm title={"Placa: "} onChange={(e) => handleInputChange("plate", e)} value={formData.plate} />
                            </div>
                        }

                        <div className={styles.modalContainer}>
                            <Select onChange={(e) => handleInputChange("idVacancy", e)} noOption={true}
                                value={formData.idVacancy} title="Vaga" data={models.vacancys.vacanciesList} />
                        </div>
                    </Modal>

                }

                {modalOpen.delete &&
                    <Modal icon={faCircleExclamation} action={"delete"} path={"tickets"} data={formData} modalOpen={modalOpen} setModalOpen={setModalOpen} title={"Excluir"}>
                        <p style={{ textAlign: "center", marginBottom: "10px" }}>Tem certeza que deseja excluir a movimentação?</p>
                    </Modal>
                }

                {modalOpen.finish &&
                    <Modal icon={faCircleCheck} action={"update"} path={"tickets/finish"} data={formData} modalOpen={modalOpen} setModalOpen={setModalOpen} title={"Finalizar"}>
                        <p style={{ textAlign: "center", marginBottom: "10px" }}>Deseja realmente finalizar essa movimentação?</p>
                        {formData.idCustomerType == 1 &&
                            <div style={{ marginBottom: "10px" }}>
                                <InputForm type="number" title="Valor pago R$:" onChange={(e) => handleInputChange("totalPrice", e)} value={formData.totalPrice} />
                            </div>
                        }
                    </Modal>
                }
                <div className={styles.box}>
                    <Table columns={["Id", "Nome", "Placa", "Marca", "Modelo", "Vaga", "Entrada", "Tipo Cliente"]} data={models.tickets} width={"85%"} setModalOpen={setModalOpen} modalOpen={modalOpen} setFormData={setFormData}/>
                    
                    {models.tickets.length > 0 &&
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
