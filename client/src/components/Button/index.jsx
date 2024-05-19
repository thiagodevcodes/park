import styles from "./button.module.css"
import Image from "next/image"

export default function Button({onClick, imgUrl, bgColor, color, title, width, height, padding}) {
    return (
        <button style={{backgroundColor: bgColor, width: width, height: height, padding: padding}} onClick={onClick}
            className={`flex justify-center ${styles.actionButton}`}>
            {imgUrl && <Image src={imgUrl} width={30} height={30} style={{maxWidth: width}} alt="Icone do Botão"/>}
            {title && <span style={{color: color}}>{title}</span>}
        </button>
    )
}