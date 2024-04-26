import styles from "./Pagination.module.css"

const MAX_ITENS = 7
const MAX_LEFT = (MAX_ITENS - 1) / 2;

export default function Pagination({ currentPage, setCurrentPage, totalPages }) {
    const current = currentPage;
    const pages = totalPages;
    const maxFirst = Math.max(pages - (MAX_ITENS - 1), 1);

    const first = Math.min(
        Math.max(current - MAX_LEFT, 1),
        maxFirst
    );

    return (
        <ul className={styles.pageBar}>
            <li>
                <button disabled={current == 0} onClick={() => setCurrentPage(current - 1)}>Anterior</button>
            </li>

            {
                Array.from({ length: Math.min(MAX_ITENS, pages) })
                    .map((_, index) => index + first)
                    .map((page) => (
                        <li key={page}>
                            <button className={
                                page - 1 === current ? styles.pageBarButtonActive : null
                            } onClick={() => setCurrentPage(page - 1)}>{page}
                            </button>
                        </li>
                    ))
            }

            <li>
                <button disabled={current == pages - 1} onClick={() => setCurrentPage(current + 1)}>Próximo</button>
            </li>
        </ul>
    )
}