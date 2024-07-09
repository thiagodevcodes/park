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
                
                {children}

            </table>
        </div>
    );
}