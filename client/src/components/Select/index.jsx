import styles from "./select.module.css"

export default function Select({ data, value, onChange, title, noOption }) {
    if (!data) return

    return (
        <>
            <label htmlFor="">{title}</label>
            <select className={styles.select} value={value} name="" id="" onChange={onChange}>
                {noOption && <option value={null} key={0}>Nenhuma Opção Selecionada</option> } 
                
                {data.length > 0 && data.map((item) => (
                    <option key={item.id} value={item.id}>{item.plate ? item.plate  : item.id} {title == "Vaga" ? "" : " - "} {item.name ? item.name : item.model}</option>
                ))}
            </select>
        </>

    )
}