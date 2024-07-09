import styles from "./layout.module.css"
import Header from "../Header";
import Footer from "../Footer";
import Aside from "../Sidebar";
import MobileButton from "../MobileButton";
import { useEffect, useState } from "react"
import { useRouter } from "next/router";

export default function Layout({ children }) {
    const [width, setWidth] = useState(0)
    const [active, setActive] = useState(false);
    const router = useRouter()

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
        <>
            {router.pathname === "/" ?
                <div>
                    <main className={styles.main}>
                        {width < 1039 && <Aside active={active} />}
                        {children}
                    </main>
                </div> :
                <div className={styles.root} >
                    <Header>
                        {width < 1039 && <MobileButton active={active} setActive={setActive} />}
                    </Header>

                    {width > 1039 && <Aside />}

                    <main className={styles.main}>
                        {width < 1039 && <Aside active={active} />}
                        {children}
                    </main>

                    <Footer />
                </div>
            }
        </>


    )
}