"use client"

import "@/styles/admin.css"
import ButtonLogout from '@/components/ButtonLogout';
import Footer from '@/components/Footer';
import Header from '@/components/Header';
import MobileButton from '@/components/MobileButton';
import NavAdmin from '@/components/NavAdmin';
import { GlobalContext } from '@/contexts/GlobalContext';
import { redirect } from 'next/navigation';
import { useContext, useState } from 'react'
import Image from 'next/image';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { ChevronDown, ChevronUp } from "lucide-react";

interface SessionLayoutProps {
    children: React.ReactNode;
}

const Login = ({ children }: SessionLayoutProps) => {
    const { screenWidth, sessionInfo, status } = useContext(GlobalContext);
    const [isOpen, setIsOpen] = useState(false);

    const toggleDropdown = () => {
      setIsOpen(!isOpen);
    };

    return (
        <div className="root">
            <Header>
                <div className="px-5 bg-white flex sticky items-center justify-between w-full shadow-lg h-20">
                    <div className="flex items-center">
                        <Image alt="Logo SOS" className="flex md:hidden" src="/img/Parking.svg" width={60} height={60} />
                        <p className="block md:hidden text-3xl font-bold">SysPark</p>
                    </div>

                    <div className="md:flex items-center hidden">                    
                        <DropdownMenu open={isOpen} onOpenChange={setIsOpen}>
                            <DropdownMenuTrigger className="focus-visible:outline-none flex gap-1" onClick={toggleDropdown}>
                                {`Bem-vindo, ${sessionInfo?.name}`}
                                { isOpen ? <ChevronUp color="black"/> : <ChevronDown color="black"/> }
                            </DropdownMenuTrigger>
                            <DropdownMenuContent>
                                <DropdownMenuLabel>Opções</DropdownMenuLabel>
                                <DropdownMenuSeparator />
                                <DropdownMenuItem><ButtonLogout /></DropdownMenuItem>
                            </DropdownMenuContent>  
                        </DropdownMenu>
                    </div>
                    <MobileButton color="bg-black" />
                </div>
            </Header>

            <aside className="aside bg-black hidden md:block">
                {screenWidth > 768 && <NavAdmin isMobile={false} />}
            </aside>

            <main className="main bg-zinc-200">
                {screenWidth < 768 && <NavAdmin isMobile={true} />}
                {children}
            </main>
            <Footer />
        </div>
    )
}

export default Login;