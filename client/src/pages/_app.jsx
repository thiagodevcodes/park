import "@/styles/globals.css";
import UserContext from "@/contexts/UserContext";
import Layout from "@/components/Layout";

export default function App({ Component, pageProps }) {
    return (
        <UserContext.Provider value={{username: 'thiagodev', name: 'Thiago' }}>    
            <Layout>    
                <Component {...pageProps} />  
            </Layout>   
        </UserContext.Provider>
    )
}
