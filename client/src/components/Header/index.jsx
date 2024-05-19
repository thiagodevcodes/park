import styles from "./header.module.css"
import Nav from "../Nav"
import { useState, useEffect } from "react"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faP, faParking } from "@fortawesome/free-solid-svg-icons"


export default function Header({ children }) {
    const [width, setWidth] = useState(0)

    useEffect(() => {
        const handleResize = () => {
            setWidth(window.innerWidth);
        };

        if (typeof window !== 'undefined') {
            window.addEventListener('resize', handleResize);

            setWidth(window.innerWidth);

            return () => {
                window.removeEventListener('resize', handleResize);
            };
        }
    }, [])

    return (
        <header className={styles.header}>
            <div className={styles.flexCenter}>

                {children}

                {width > 800 &&
                    <>
                        <FontAwesomeIcon style={{ marginLeft: "15px" }} icon={faParking} width={30}></FontAwesomeIcon>
                        <h2 style={{ marginLeft: "5px" }}>SysPark</h2>
                    </>
                }
            </div>

            <Nav />
        </header>
    )
}