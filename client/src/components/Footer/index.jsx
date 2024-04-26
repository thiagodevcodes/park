import styles from "./footer.module.css"
import Link from "next/link"


export default function Footer() {

    return (
        <footer className={styles.footer}>
            <div className={styles.container}>
                <p>Copyright 2024 &copy; Developed by <Link href={"https://thiagodev.vercel.app"}>Thiago Silva Rodrigues</Link></p>
            </div>
        </footer>
    )
}