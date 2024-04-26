import styles from "./table.module.css"

export default function Table({ columns, children }) {
    return (
        <div className={styles.container}>
            <table className={styles.table}>
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