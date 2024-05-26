import styles from "./table.module.css"
import { formatDate } from "@/services/utils";
import Button from "@/components/Button";
import Link from "next/link";
import Image from "next/image";
import { useState } from "react";
import { useRouter } from "next/router";


export default function Table({ columns, children, width, data, setModalOpen, modalOpen, setFormData }) {
    const router = useRouter()

    return (
        <div className={styles.container}>
            <table className={styles.table} style={{ width: width }}>
                <thead>
                    <tr> 
                        {columns.map((item, index) => (
                            <th key={index}>{item}</th>
                        ))}
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {data && data.length > 0 ?
                        (data.map((item) => (
                            <tr key={item.id}>
                                {
                                    Object.keys(item).map((key, index) => (
                                        <td key={index}>
                                        {
                                            key === 'idCustomerType' ? (item[key] == 1 ? "Rotativo" : "Mensalista"): 
                                            key === 'entryTime' ? formatDate(item[key]) : 
                                            key === 'userType' ? (item[key] == 1 ? "Admin" : "Padrão") : item[key]
                                        }
                                    </td>                
                                    ))
                                }
                                <td>
                                    <div className={styles.buttonContainer}>
                                        <Button onClick={() => {
                                            setModalOpen({ ...modalOpen, update: true })
                                            setFormData({ ...item })
                                        }} imgUrl={"/icons/Edit.svg"} bgColor={"#E9B500"} padding={"2px"} />

                                        <Button onClick={() => {
                                            setModalOpen({ ...modalOpen, finish: true })
                                            setFormData({ ...item })
                                        }} imgUrl={"/icons/Done.svg"} bgColor={"#00bd1f"} padding={"2px"} />

                                        <Button onClick={() => {
                                            setModalOpen({ ...modalOpen, delete: true })
                                            setFormData({ ...item })
                                        }} imgUrl={"/icons/Remove.svg"} bgColor={"#FF0000"} padding={"2px"} />
                                        
                                        { router.pathname === "/mensalistas" &&
                                        <Link href={`/mensalistas/vehicles/${item.id}`} className={`${styles.bgBlue} ${styles.actionButton}`}>
                                            <Image src={"/icons/Cars.svg"} width={30} height={30} alt="Icone Cars" />
                                        </Link>
                                        }
                                    </div>
                                </td>
                            </tr>
                        ))) : <tr style={{textAlign: "center"}}> <td colSpan={columns.length + 1}>Não possui dados</td> </tr>
                    }
                    {children}
                </tbody>

            </table>
        </div>
    );
}