import { useState } from "react";
import styles from "./modal.module.css";
import { handleCreate, handleDelete, handleUpdate } from "@/services/axios";
import { useRouter } from "next/router";

export default function Modal({ children, setModalOpen, modalOpen, title, action, path, data }) {
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
                            submitUpdated(e, data.id, path, data)
                        } else if(action == "delete") {
                            submitDelete(e, data.id, path)
                        }
                    }}>Salvar</button>
                    <button type="button" onClick={() => setModalOpen({post: false, update: false, delete: false})}>Voltar</button>
                </div>
            </form>
        </div>
    );
}