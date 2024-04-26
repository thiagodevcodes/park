import styles from "./modal.module.css";

export default function Modal({ children, setModalOpen, title, handleSubmit }) {
    return (
        <div className={styles.modalOverlay}>
            <form className={styles.modal} method="POST">
                <h2>{title}</h2>

                {children}

                <div className={styles.buttons}>
                    <button type="submit" onClick={handleSubmit}>Salvar</button>
                    <button type="button" onClick={() => setModalOpen({post: false, update: false, delete: false})}>Voltar</button>
                </div>
            </form>
        </div>
    );
}