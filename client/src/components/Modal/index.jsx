import { useState } from "react";
import styles from "./modal.module.css";
import { handleCreate, handleDelete, handleUpdate } from "@/services/axios";
import { useRouter } from "next/router";

export default function Modal({ children, setModalOpen, modalOpen, title, action, path, data }) {
    const router = useRouter()

    //console.log(data)

    const submitUpdated = async (e, id, path, data) => {
        e.preventDefault();
        if (id) {
            try {
                const res = await handleUpdate(id, path, data)
                
                if(res && res.status === 200) {
                    setModalOpen({ ...modalOpen, update: false, finish: false })
                    setTimeout(() => {
                        router.reload(); 
                    }, 3000);
                }
            } catch (error) {
                console.error("Erro ao criar:", error);
            }
        }
    };

    const handleSubmit = async (e, path, data) => {
        e.preventDefault();
        try {
            const res = await handleCreate(data, path);
            
            if(res && res.status === 200) {
                setModalOpen({ ...modalOpen, post: false })
                setTimeout(() => {
                    router.reload(); 
                }, 3000);
            }
        } catch (error) {
            console.error("Erro ao criar:", error);
        }
    };

    const submitDelete = async (e, id, path) => {
        e.preventDefault();
        if (id) {
            try {
                const res = await handleDelete(id, path)

                if(res && res.status === 204) {
                    setModalOpen({...modalOpen, delete: false})
                    setTimeout(() => {
                        router.reload(); 
                    }, 3000);
                }
            } catch (error) {
                console.error("Erro ao criar:", error);
            }
        }
    }

    return (
        <div className={styles.modalOverlay}>
            <form className={styles.modal} method="POST">
                <h2>{title}</h2>

                {children}

                <div className={styles.buttons}>
                    <button type="submit" onClick={ (e) => {
                        if (action == "post") {
                            handleSubmit(e, path, data);
                        } else if(action == "update") {
                            console.log(data)
                            submitUpdated(e, data.id, path, data)
                        } else if(action == "delete") {
                            submitDelete(e, data.id, path)
                        }
                    }}>Salvar</button>
                    <button type="button" onClick={() => setModalOpen({post: false, update: false, delete: false, finish: false})}>Voltar</button>
                </div>
            </form>
        </div>
    );
}