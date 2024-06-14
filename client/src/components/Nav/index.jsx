import styles from "./nav.module.css"
import Link from "next/link"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faRightFromBracket } from "@fortawesome/free-solid-svg-icons"
import { AuthContext } from "@/contexts/UserContext"
import { useContext } from "react"

export default function Nav() {
    const { auth, logout } = useContext(AuthContext);

    const handleSubmit = async (e) => {
        await logout();
    };

    return (
        <nav className={styles.nav}>
            <ul>
                <li className={styles.navOptions}>
                    <Link onClick={handleSubmit} href={"/"}>Sair</Link>
                    <FontAwesomeIcon style={{marginLeft: "10px"}} icon={faRightFromBracket} width={20}/>
                </li>
            </ul>
        </nav>
    )
}

