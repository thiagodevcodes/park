import styles from "./inputForm.module.css"

export default function InputForm({ title, onChange, value }) {
    return (
        <div className={styles.modalInputContainer}>
            <label htmlFor="">{title}</label>
            <input value={value} onChange={onChange} type="text" />
        </div>
    )
}