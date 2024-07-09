import "@/styles/globals.css";
import { AuthProvider } from "@/contexts/UserContext";
import Layout from "@/components/Layout";

export default function App({ Component, pageProps }) {
    return (
        <Layout>    
            <Component {...pageProps} />  
        </Layout>   
    )
}
