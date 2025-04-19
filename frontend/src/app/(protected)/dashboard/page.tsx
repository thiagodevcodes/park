"use client"

import ChartOverview from "@/components/Chart";
import Sales from "@/components/Sales";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { GlobalContext } from "@/contexts/GlobalContext";
import axios from "axios";
import { Check, Loader, Users, Gauge, DollarSign, ArrowRightLeft, Calendar } from "lucide-react";
import { useContext, useEffect, useState } from "react";

const AdminDashboard: React.FC = () => {
  const [loading, setLoading] = useState(true);
  const [totalCount, setTotalCount] = useState<number>(); // Adicione o estado para a contagem total
  const [totalVehicles, setTotalVehicles] = useState<number>(); // Adicione o estado para a contagem total

  const { sessionInfo } = useContext(GlobalContext);
  const token = sessionInfo?.accessToken;
  const baseUrl = "http://localhost:8080";
  
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get(`${baseUrl}/api/users`, {
          headers: {
            "Authorization": `Bearer ${token}`
          },
        });

        if (response.status === 200) {
          setTotalCount(response.data.totalElements); 
        } 
      } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
          console.error("Erro ao buscar usuários:", error);
          
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, [baseUrl, token, totalCount]);

  return (
    <section className="mx-10 px-3 py-1 mb-4 min-h-s">
      <div className="my-5">
        <div className="flex items-center gap-1">
          <Gauge/>
          <h1 className="font-bold text-3xl">Dashboard</h1>
        </div>
        <span>Visão geral interativa dos principais indicadores</span>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 lg:grid-cols-4">
        <Card>
          <CardHeader>
            <div className="flex items-center justify-center">
              <CardTitle className="text-lg sm:text-xl text-gray-800 select-none">
                Usuários
              </CardTitle>
              <Users className="ml-auto w-4 h-4"/>
            </div> 
            <CardDescription>
              Total de usuários cadastrados
            </CardDescription>
          </CardHeader>

          <CardContent>
            <p className="text-base sm:text-lg font-bold">{totalCount}</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <div className="flex items-center justify-center">
              <CardTitle className="text-lg sm:text-xl text-gray-800 select-none">
                Lucro Diário
              </CardTitle>
              <DollarSign className="ml-auto w-4 h-4"/>
            </div> 
            <CardDescription>
              Lucro arrecadado diário
            </CardDescription>
          </CardHeader>

          <CardContent>
            <p className="text-base sm:text-lg font-bold">R$15,00</p>
          </CardContent>
        </Card>
      
        <Card>
          <CardHeader>
            <div className="flex items-center justify-center">
              <CardTitle className="text-lg sm:text-xl text-gray-800 select-none">
                Movimentações
              </CardTitle>
              <ArrowRightLeft className="ml-auto w-4 h-4"/>
            </div> 
            <CardDescription>
              Total de movimentações hoje
            </CardDescription>
          </CardHeader>

          <CardContent>
            <p className="text-base sm:text-lg font-bold">1</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <div className="flex items-center justify-center">
              <CardTitle className="text-lg sm:text-xl text-gray-800 select-none">
                Mensalistas
              </CardTitle>
              <Calendar className="ml-auto w-4 h-4"/>
            </div> 
            <CardDescription>
              Total de clientes mensalistas
            </CardDescription>
          </CardHeader>

          <CardContent>
            <p className="text-base sm:text-lg font-bold">1</p>
          </CardContent>
        </Card>
      </div>

      <div className="mt-4 flex flex-col md:flex-row gap-4">
        <ChartOverview />
        <Sales/>
      </div>
    </section>
  );
}

export default AdminDashboard