'use client'

import Link from "next/link";
import Image from "next/image";
import { useContext, useEffect, useState } from "react";
import { GlobalContext } from "@/contexts/GlobalContext";
import ButtonLogout from "../ButtonLogout";
import { usePathname } from "next/navigation";
import { Calendar, Car, ChevronDown, CircleGauge, Users } from "lucide-react";

interface NavAdminProps {
  isMobile: boolean;
}

const NavAdmin: React.FC<NavAdminProps> = ({ isMobile }) => {
  const { sessionInfo, mobileActive } = useContext(GlobalContext);
  const path = usePathname();
  const [ isAdmin, setIsAdmin ] = useState<boolean>(false)

  useEffect(() => {
    if (sessionInfo?.role) {
      const hasAdminRole = sessionInfo?.role === "ADMIN";
      setIsAdmin(hasAdminRole);
    }
  }, [sessionInfo]);

  return (
    <nav> 
      { !isMobile ? 
        <div className="hidden md:flex items-center flex-col">  
          <div className="flex items-center justify-center gap-2 mt-4 mb-4">
            <Image alt="Logo SOS" src="/img/ParkingWhite.svg" color="white" width={40} height={40} />
            <p className="text-3xl font-bold">SysPark</p>
          </div>   
  
          <Link className={`${path === "/dashboard" ? "bg-zinc-900" : ""} hover:bg-zinc-900 transition-all flex items-center gap-2 px-5 py-2 w-full text-start text-white font-light`} href="/dashboard">
            <CircleGauge width={18}/>
            Dashboard
          </Link>
          <Link className={`${path === "/mensalistas" ? "bg-zinc-900" : ""} hover:bg-zinc-900 transition-all flex items-center gap-2 px-5 py-2 w-full text-start text-white font-light`} href="/mensalistas">
            <Calendar width={18}/>
             Mensalistas
          </Link>
          <Link className={`${path === "/movimentacoes" ? "bg-zinc-900" : ""} hover:bg-zinc-900 transition-all flex items-center gap-2 px-5 py-2 w-full text-start text-white font-light`} href="/movimentacoes">
            <Car width={18}/>
            Movimentações
          </Link>
          { isAdmin &&
            <Link className={`${path === "/users" ? "bg-zinc-900" : ""} hover:bg-zinc-900 transition-all flex items-center gap-2 px-5 py-2 w-full text-start text-white font-light`} href="/users">
               <Users width={18}/>
               Usuários
            </Link>    
          }
              
        </div> 
        :                 
        <div className={`flex transition-all ${"-left-full"} gap-6 items-center flex-col h-screen bg-black fixed w-full z-20 ${mobileActive ? "left-0" : "-left-full"}`}>
          <Link className="flex items-center justify-center gap-2 px-5 py-2 w-full text-start text-white font-bold" href="/admin/dashboard">
             Dashboard
          </Link>
          <Link className="flex items-center justify-center gap-2 px-5 py-2 w-full text-start text-white font-bold" href="/admin/equipe">
            Equipe
          </Link>
          <Link className="flex items-center justify-center gap-2 px-5 py-2 w-full text-start text-white font-bold" href="/admin/projetos">
             Projetos
          </Link>  
          { isAdmin &&
            <Link className={`${path === "/users" ? "bg-zinc-900" : ""} flex items-center justify-center gap-2 px-5 py-2 w-full text-start text-white font-bold`} href="/admin/users">
               Usuários
            </Link>    
          }
          <div className="flex gap-2 items-center">
            <span className="text-white">{ `Bem vindo, ${sessionInfo?.name}` }</span>
          </div>
          <ButtonLogout/>                
        </div>
      }    
    </nav>
  );
}

export default NavAdmin;