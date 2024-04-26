import Head from "next/head";
import { useState, useEffect } from "react";
import styles from "../../styles/Mensalistas.module.css";
import Layout from "@/components/Layout";
import Table from "@/components/Table";
import Modal from "@/components/Modal";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { formatDate } from "@/services/utils";
import Image from "next/image";
import { handleUpdate, fetchData, fetchDataPage } from "@/services/axios";
import Pagination from "@/components/Pagination";
import InputForm from "@/components/InputForm";

export default function Movimentacoes() {
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [width, setWidth] = useState(0)
    const [modalOpen, setModalOpen] = useState({ post: false, update: false, delete: false });
    const [models, setModels] = useState({ tickets: [], clients: [], vehicles: [], vacancys: [] })
    const [formData, setFormData] = useState({ name: null, plate: null, make: null, model: null, idVacancy: null, idCustomerType: 1, idCustomer: null, idVehicle: null })

    const handleInputChange = (column, event) => {
        setFormData({
            ...formData,
            [column]: event.target.value,
        });
        console.log(formData);
    };

    useEffect(() => {
        const fetchDataModels = async () => {
            try {
                const [clientsResponse, ticketsResponse, vacanciesResponse] = await Promise.all([
                    fetchData("customers/mensalistas"),
                    fetchDataPage(3, currentPage, "tickets/movimentacoes"),
                    fetchData("vacancies")
                ]);
                setModels(prevValues => ({
                    ...prevValues,
                    clients: clientsResponse.content,
                    tickets: ticketsResponse.content,
                    vacancys: vacanciesResponse
                }));
                setTotalPages(ticketsResponse.totalPages)
            } catch (error) {
                console.error("Erro ao carregar dados:", error);
            }
        };
        fetchDataModels();
    }, [currentPage]);

    useEffect(() => {
        const fetchDataVehicles = async () => {
            if (formData.idCustomerType == "2") {
                const vehiclesResponse = await fetchData(`vehicles/mensalistas/${formData.idCustomer}`);
                setModels(prevValues => ({
                    ...prevValues,
                    vehicles: vehiclesResponse.content
                }));
            }
        }

        fetchDataVehicles()
    }, [formData.idCustomer])

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

            <Layout>
                <div className={styles.container}>
                    <div className={styles.headerLogo}>
                        <h1>Movimentações</h1>
                        <button onClick={() => setModalOpen({ ...modalOpen, post: true })} className={styles.addButton}>+ Adicionar</button>
                    </div>
                </div>

                {modalOpen.post &&
                    <Modal action={"post"} path={`tickets/${formData.idCustomerType == 1 ? "rotativos" : "mensalistas"}`} data={formData} modalOpen={modalOpen} 
                        title={"Adicionar"} setModalOpen={setModalOpen}>
                        <div className={styles.modalContainer}>
                            <div className={styles.modalInputContainer}>
                                <label htmlFor="">Tipo de Cliente</label>
                                <select value={formData.idCustomerType} name="" id="" onChange={(e) => handleInputChange("idCustomerType", e)}>
                                    <option key={2} value={2}>Mensalista</option>
                                    <option key={1} value={1}>Rotativo</option>
                                </select>
                            </div>
                        </div>

                        {formData.idCustomerType == 2 && models.clients.length > 0 ?
                            <div className={styles.modalContainer}>
                                <div className={styles.modalInputContainer}>
                                    <label htmlFor="">Cliente</label>
                                    <select value={formData.idCustomer} name="" id="" onChange={(e) => handleInputChange("idCustomer", e)}>
                                        <option value={null} key={0}>Nenhum Cliente Selecionado</option>
                                        {models.clients.map((item) => (
                                            <option key={item.id} value={item.id}>{item.id} - {item.name}</option>
                                        ))}
                                    </select>
                                </div>
                                <div className={styles.modalInputContainer}>
                                    <label htmlFor="">Veiculo</label>
                                    <select value={formData.idVehicle} name="" id="" onChange={(e) => handleInputChange("idVehicle", e)}>
                                        <option key={0} value={0}>Nenhum Veículo Selecionado</option>
                                        {
                                            models.vehicles.map((item) => (
                                                <option key={item.id} value={item.id}> {item.plate} - {item.model}</option>
                                            ))
                                        }
                                    </select>
                                </div>

                            </div>
                            :
                            null
                        }

                        {formData.idCustomerType == 1 ?
                            <div>
                                <div className={styles.modalContainer}>
                                    <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} />
                                    <InputForm title={"Marca: "} onChange={(e) => handleInputChange("make", e)} />
                                </div>
                                <div className={styles.modalContainer}>
                                    <InputForm title={"Modelo: "} onChange={(e) => handleInputChange("model", e)} />
                                    <InputForm title={"Placa: "} onChange={(e) => handleInputChange("plate", e)} />
                                </div>
                            </div>
                            :
                            null
                        }

                        <div className={styles.modalContainer}>
                            <div className={styles.modalInputContainer}>
                                <label htmlFor="">Vaga: </label>
                                <select defaultValue={null} value={formData.idVacancy} name="" id="" onChange={(e) => handleInputChange("idVacancy", e)}>
                                    <option value={null}>Nenhuma Vaga Selecionada</option>
                                    {models.vacancys.vacanciesList.map((item) => (
                                        item.situation &&
                                        <option key={item.id} value={item.id}>{item.id}</option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    </Modal>
                }

                {modalOpen.update &&
                    <Modal action={"update"} path={`tickets/${formData.idCustomerType == 1 ? "rotativos" : "mensalistas"}`} 
                        data={formData} modalOpen={modalOpen} title={"Editar"} setModalOpen={setModalOpen}>
                        {formData.idCustomerType == 2 && models.clients.length > 0 ?
                            <div className={styles.modalContainer}>
                                <div className={styles.modalInputContainer}>
                                    <label htmlFor="">Cliente</label>
                                    <select value={formData.idCustomer} name="" id="" onChange={(e) => handleInputChange("idCustomer", e)}>
                                        <option value={0} key={0}>Nenhum Cliente Selecionado</option>
                                        {models.clients.map((item) => (
                                            <option key={item.id} value={item.id}>{item.id} - {item.name}</option>
                                        ))}
                                    </select>
                                </div>
                                <div className={styles.modalInputContainer}>
                                    <label htmlFor="">Veiculo</label>
                                    <select value={formData.idVehicle} name="" id="" onChange={(e) => handleInputChange("idVehicle", e)}>
                                        <option key={0} value={0}>Nenhum Veículo Selecionado</option>
                                        {
                                            models.vehicles.map((item) => (
                                                <option key={item.id} value={item.id}> {item.plate} - {item.model}</option>
                                            ))
                                        }
                                    </select>
                                </div>
                            </div>
                            :
                            null
                        }

                        {formData.idCustomerType == 1 ?
                            <div>
                                <div className={styles.modalContainer}>
                                    <InputForm title={"Nome: "} onChange={(e) => handleInputChange("name", e)} value={formData.name} />
                                    <InputForm title={"Marca: "} onChange={(e) => handleInputChange("make", e)} value={formData.make} />
                                </div>

                                <div className={styles.modalContainer}>
                                    <InputForm title={"Modelo: "} onChange={(e) => handleInputChange("model", e)} value={formData.model} />
                                    <InputForm title={"Placa: "} onChange={(e) => handleInputChange("plate", e)} value={formData.plate} />
                                </div>
                            </div>
                            :
                            null
                        }

                        <div className={styles.modalContainer}>
                            <div className={styles.modalInputContainer}>
                                <label htmlFor="">Vaga: </label>
                                <select defaultValue={formData.idVacancy} value={formData.idVacancy} name="" id="" onChange={(e) => handleInputChange("idVacancy", e)}>
                                    <option value={null}>Nenhuma Vaga Selecionada</option>
                                    {models.vacancys.vacanciesList.map((item) => (
                                        item.situation &&
                                        <option key={item.id} value={item.id}>{item.id}</option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    </Modal>

                }

                {modalOpen.delete &&
                    <Modal action={"delete"} path={"tickets"} data={formData} modalOpen={modalOpen} setModalOpen={setModalOpen} title={"Excluir"}>
                        <p>Tem certeza que deseja excluir a movimentacão?</p>
                    </Modal>
                }
                <div className={styles.box}>
                    <Table columns={["Nome", "Placa", "Marca", "Modelo", "Vaga", "Tipo Cliente", "Entrada"]}>
                        <tbody>
                            {models.tickets && models.tickets.length > 0 ? (
                                models.tickets.map((item) => (
                                    <tr key={item.id}>
                                        <td>{item.name}</td>
                                        <td>{item.plate}</td>
                                        <td>{item.make}</td>
                                        <td>{item.model}</td>
                                        <td>{item.idVacancy}</td>
                                        <td>{item.idCustomerType == 1 ? "Rotativo" : "Mensalista"}</td>
                                        <td>{formatDate(item.entryTime)}</td>
                                        <td>
                                            <div className={styles.buttonContainer}>
                                                <button onClick={() => {
                                                    setModalOpen({ ...modalOpen, update: true })
                                                    setFormData({ ...item })
                                                }}
                                                    className={`${styles.bgYellow} ${styles.actionButton}`}>
                                                    <Image src={"/icons/Edit.svg"} width={30} height={30} alt="Icone Edit" />
                                                </button>
                                                <button onClick={() => handleUpdate(item.id, "tickets/finish", { ...item })} className={`${styles.bgGreen} ${styles.actionButton}`}>
                                                    <Image src={"/icons/Done.svg"} width={30} height={30} alt="Icone Remove" />
                                                </button>
                                                <button onClick={() => {
                                                    setModalOpen({ ...modalOpen, delete: true })
                                                    setFormData({ ...item })
                                                }} className={`${styles.bgRed} ${styles.actionButton}`}>
                                                    <Image src={"/icons/Remove.svg"} width={30} height={30} alt="Icone Remove" />
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan={9}>Não possui movimentações!</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                    {models.tickets.length > 0 ?
                        <Pagination
                            currentPage={currentPage}
                            setCurrentPage={setCurrentPage}
                            totalPages={totalPages}
                        /> :
                        null
                    }
                </div>
                <ToastContainer />
            </Layout >
        </>
    );
}
