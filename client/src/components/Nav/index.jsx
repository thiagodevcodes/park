import styles from "./nav.module.css"
import Link from "next/link"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faRightFromBracket } from "@fortawesome/free-solid-svg-icons"

export default function Nav() {
    return (
        <nav className={styles.nav}>
            <ul>
                <li className={styles.navOptions}>
                    <Link href={"/"}>Sair</Link>
                    <FontAwesomeIcon style={{marginLeft: "10px"}} icon={faRightFromBracket} width={20}/>
                </li>
            </ul>
        </nav>
    )
}

