"use client"

import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import Spinner from "@/components/Spinner";
import { useEffect, useState } from "react";

interface SessionLayoutProps {
    children: React.ReactNode;
}

const SessionLayout = ({ children }: SessionLayoutProps) => {
    const { status } = useSession();
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        if (status === "loading") {
            setLoading(true);
        } else if (status === "authenticated") {
            router.replace("/dashboard");
        } else if (status === "unauthenticated") {
            setLoading(false);
        }
    }, [status, router]);

    if(loading) {
        return (
            <div className="flex items-center justify-center h-dvh">
                <Spinner/>
            </div>  
        )
    }

    return <>
        { children }   
    </>       
       
}

export default SessionLayout;
