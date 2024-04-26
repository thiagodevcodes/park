import styles from "./layout.module.css"
import Header from "../Header";
import Footer from "../Footer";
import Aside from "../Sidebar";
import MobileButton from "../MobileButton";
import { useEffect, useState } from "react" 

export default function Layout({ children }) {
    const [width, setWidth] = useState(0)
    const [active, setActive] = useState(false);

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
        <div className={styles.root}>
            <Header>
                { width < 800 && <MobileButton active={active} setActive={setActive}/> }
            </Header>

            { width > 800 && <Aside/> }

            <main className={styles.main}>
                { width < 800 && <Aside active={active}/> }
                { children }
            </main>

            <Footer></Footer>
        </div>
    )
}