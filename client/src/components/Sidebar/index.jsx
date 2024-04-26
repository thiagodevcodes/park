import styles from "./sidebar.module.css"
import Link from "next/link"
import Image from "next/image"
import { useRouter } from "next/router"

export default function Aside({ active }) {
    const router = useRouter();

    return (
        <aside className={`${styles.aside} ${active ? `${styles.open}` : ""}`} >
            <ul>
                <li className={router.pathname === "/home" ? `${styles.active}` : ""} >
                    <Image src={"/icons/Home.svg"} width={25} height={25} alt="Logo Home"></Image>
                    <Link href={"/home"}>Home</Link>
                </li>
                <li className={router.pathname === "/mensalistas" ? `${styles.active}` : ""}>
                    <Image src={"/icons/Calendar.svg"} width={25} height={25} alt="Logo Home"></Image>
                    <Link href={"/mensalistas"}>Mensalistas</Link>
                </li>
                <li className={router.pathname === "/movimentacoes" ? `${styles.active}` : ""}>
                    <Image src={"/icons/Movement.svg"} width={25} height={25} alt="Logo Home"></Image>
                    <Link href={"/movimentacoes"}>Movimentações</Link>
                </li>
                <li className={router.pathname === "/precos" ? `${styles.active}` : ""}>
                    <Image src={"/icons/Money.svg"} width={25} height={25} alt="Logo Home"></Image>
                    <Link href={"#"}>Preços</Link>
                </li>
                <li className={router.pathname === "/usuarios" ? `${styles.active}` : ""}>
                    <Image src={"/icons/Users.svg"} width={25} height={25} alt="Logo Home"></Image>
                    <Link href={"#"}>Usuários</Link>
                </li>
                <li className={router.pathname === "/relatorios" ? `${styles.active}` : ""}> 
                    <Image src={"/icons/Relatorio.svg"} width={25} height={25} alt="Logo Home"></Image>
                    <Link href={"#"}>Relatórios</Link>
                </li>
            </ul>
        </aside>
    )
}