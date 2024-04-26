import { useRouter } from "next/router";
import styles from "./actionButtons.module.css";
import Link from "next/link";

export default function ActionButtons({ onUpdate }) {
    const router = useRouter()

    return (
        <div className={styles.buttonContainer}>
            <button className={`${styles.bgYellow} ${styles.actionButton}`}>
                <Image src={"/icons/Edit.svg"} width={30} height={30} alt="Icone Edit" />
            </button>
            <button onClick={(e) => onUpdate(e, item.id)} className={`${styles.bgRed} ${styles.actionButton}`}>
                <Image src={"/icons/Remove.svg"} width={30} height={30} alt="Icone Remove" />
            </button>
            {router.pathname === "/mensalistas" && (
                <Link href={`/mensalistas/vehicles/${item.id}`} className={`${styles.bgBlue} ${styles.actionButton}`}>
                    <Image src={"/icons/Cars.svg"} width={30} height={30} alt="Icone Cars" />
                </Link>
            )}
        </div>
    )
}