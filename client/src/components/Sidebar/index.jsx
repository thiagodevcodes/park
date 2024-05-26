import styles from "./sidebar.module.css"
import Link from "next/link"
import { useRouter } from "next/router"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faHome, faCalendarDays, faCar, faUserGroup, faSquarePollHorizontal } from "@fortawesome/free-solid-svg-icons"

export default function Aside({ active }) {
    const router = useRouter();

    return (
        <aside className={`${styles.aside} ${active ? `${styles.open}` : ""}`} >
            <ul>
                <li className={router.pathname === "/home" ? `${styles.active}` : ""} >
                    <FontAwesomeIcon icon={faHome} color="black" width={20}></FontAwesomeIcon>
                    <Link href={"/home"}>Home</Link>
                </li>
                <li className={router.pathname === "/mensalistas" ? `${styles.active}` : ""}>
                    <FontAwesomeIcon icon={faCalendarDays} color="black" width={18}></FontAwesomeIcon>
                    <Link href={"/mensalistas"}>Mensalistas</Link>
                </li>
                <li className={router.pathname === "/movimentacoes" ? `${styles.active}` : ""}>
                    <FontAwesomeIcon icon={faCar} color="black" width={18}></FontAwesomeIcon>
                    <Link href={"/movimentacoes"}>Movimentações</Link>
                </li>
                <li className={router.pathname === "/users" ? `${styles.active}` : ""}>
                    <FontAwesomeIcon icon={faUserGroup} color="black" width={20}></FontAwesomeIcon>
                    <Link href={"/users"}>Usuários</Link>
                </li>
                <li className={router.pathname === "/relatorios" ? `${styles.active}` : ""}> 
                    <FontAwesomeIcon icon={faSquarePollHorizontal} color="black" width={18}></FontAwesomeIcon>
                    <Link href={"#"}>Relatórios</Link>
                </li>
            </ul>
        </aside>
    )
}