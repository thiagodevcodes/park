import styles from "./nav.module.css"
import Link from "next/link"
import Image from "next/image"

export default function Nav() {
    return (
        <nav className={styles.nav}>
            <ul>
                <li className={styles.navOptions}>
                    <Link href={"/"}>Sair</Link>
                    <Image src="/img/Logout.svg" width={30} height={30} alt="Logout"></Image>
                </li>
            </ul>
        </nav>
    )
}

