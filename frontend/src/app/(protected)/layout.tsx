"use client"

import Login from "@/components/Admin";
import { useSession } from "next-auth/react";
import Spinner from "@/components/Spinner";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { Session } from "next-auth";
import { User } from "next-auth";

interface SessionLayoutProps {
    children: React.ReactNode;
}

interface CustomUser extends User {
    role: string;
}

interface CustomSession extends Session {
    user: CustomUser;
}

const SessionLayout = ({ children }: SessionLayoutProps) => {
    const { data: session, status } = useSession();
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        if (status === "loading") {
            setLoading(true);
        } else if (status === "authenticated") {
            const customSession = session as CustomSession;
            if(customSession.user.role != "ADMIN") {
                console.log(session)
                router.replace("/dashboard")
            }
            setLoading(false);
        } else if (status === "unauthenticated") {
            router.replace("/");
        }
    }, [status, router]);

    if(loading) {
        return (
            <div className="flex items-center justify-center h-dvh">
                <Spinner/>
            </div>  
        )
    }

    return (
        <Login>
            {children}
        </Login>
        
    );
}

export default SessionLayout;