import styles from "./nav.module.css"
import Link from "next/link"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faRightFromBracket } from "@fortawesome/free-solid-svg-icons"
import Cookies from 'js-cookie';
import { useRouter } from "next/router";

export default function Nav() {
    const router = useRouter()

    const logout = () => {
        Cookies.remove('auth_token')
        router.push("/")
    }

    return (
        <nav className={styles.nav}>
            <ul>
                <li className={styles.navOptions}>
                    <Link onClick={logout} href={"/"}>Sair</Link>
                    <FontAwesomeIcon style={{marginLeft: "10px"}} icon={faRightFromBracket} width={20}/>
                </li>
            </ul>
        </nav>
    )
}

