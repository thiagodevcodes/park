"use client"

import React, { useContext, useEffect, useState } from "react";
import FormField from "@/components/FormField";
import { GlobalContext } from "@/contexts/GlobalContext";
import { useForm, Controller } from "react-hook-form";
import { z } from 'zod'
import { zodResolver } from "@hookform/resolvers/zod"
import { DialogClose } from "@/components/ui/dialog";
import { Label } from "@/components/ui/label"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { handleDelete, fetchData, fetchDataAll, handleCreate, handleUpdate } from "@/services/axios";
import { useToast } from "@/hooks/use-toast";
import Link from "next/link";
import Table from "@/components/Table";
import { Button } from "@/components/ui/button";
import { Calendar, Trash, UserPen } from "lucide-react";
import PaginationBox from "@/components/Pagination";

const formSchema = z.object({
  plate: z.string().min(1, "Placa é obrigatória"),
  make: z.string().min(1, "Marca é obrigatória"),
  model: z.string().min(1, "Modelo é obrigatório"),
  monthlyVehicle: z.preprocess(() => true, z.boolean()),
  idCustomer: z.number()
});

type VehicleSchema = z.infer<typeof formSchema>

interface Vehicle {
  id: number,
  plate: string,
  make: string,
  model: string,
  idCustomer: number,
  monthlyVehicle: boolean
}

const Veiculos = ({ params }: { params: { id: number } }) => {
  const [message, setMessage] = useState("");
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [size, setSize] = useState<number>(5);
  const { sessionInfo } = useContext(GlobalContext)
  const token = sessionInfo?.accessToken;
  const [editId, setEditId] = useState<number | null>(null);
  const { toast } = useToast()
  const idCustomer = params.id
  console.log(idCustomer)

  const { register, handleSubmit, control, setValue, reset, formState: { errors } } = useForm<VehicleSchema>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      monthlyVehicle: true,
      plate: "",
      make: "",
      model: "",
      idCustomer: Number(idCustomer)
    }
  });

  useEffect(() => {
    fetchDataAll(token, currentPage, "vehicles/customer", `id=${idCustomer}`).then((res: any) => {
      console.log(res.content)
      setVehicles(res.content);
      setTotalPages(res.totalPages);
      console.log(vehicles)
    });
  }, [token, currentPage, size]);


  useEffect(() => {
    if (editId !== null) {
      fetchData(token, editId, "vehicles").then((vehicle: Vehicle) => {
        console.log(vehicle)
        reset({
          monthlyVehicle: vehicle.monthlyVehicle,
          plate: vehicle.plate,
          make: vehicle.make,
          model: vehicle.model,
          idCustomer: Number(idCustomer)
        });
      });
    } else {
      reset({
        monthlyVehicle: true,
        plate: "",
        make: "",
        model: "",
        idCustomer: Number(idCustomer)
      })
    }
  }, [editId, token, reset]);

  const createData = async (data: VehicleSchema) => {
    const result = await handleCreate(data, token, "vehicles");
    console.log(data)

    if (result) {
      setMessage(result.message);

      if (!result.success) {
        toast({
          variant: "destructive",
          title: "Erro :(",
          description: result.message,
        })
      } else {
        toast({
          variant: "success",
          title: "Sucesso!",
          description: result.message,
        })
        fetchDataAll(token, currentPage, "vehicles/customer", `id=${idCustomer}`).then((res: any) => {
          setVehicles(res.content);
          setTotalPages(res.totalPages);
        });
      }
    }
  };

  const updateData = async (data: VehicleSchema) => {
    if (editId !== null) {
      const result = await handleUpdate(data, token, "vehicles", editId);

      if (result) {
        setMessage(result.message);

        if (!result.success) {
          toast({
            variant: "destructive",
            title: "Erro :(",
            description: result.message,
          })
        } else {
          toast({
            variant: "success",
            title: "Sucesso!",
            description: result.message,
          })
          fetchDataAll(token, currentPage, "vehicles/customer", `id=${idCustomer}`).then((res: any) => {
            setVehicles(res.content);
            setTotalPages(res.totalPages);
          });
        }
      }
    }
  };

  const deleteData = async (id: number) => {
    const result = await handleDelete(token, id, "vehicles");

    if (result) {
      setMessage(result.message);

      if (result) {
        setMessage(result.message);

        if (!result.success) {
          toast({
            variant: "destructive",
            title: "Erro :(",
            description: result.message,
          })
        } else {
          toast({
            variant: "success",
            title: "Sucesso!",
            description: result.message,
          })
          fetchDataAll(token, currentPage, "vehicles/customer", `id=${idCustomer}`).then((res: any) => {
            setVehicles(res.content);
            setTotalPages(res.totalPages);
          });
        }
      }
    }
  };

  return (
    <section>
      <div className="flex justify-center gap-96 my-5 items-center">
        <div className="flex flex-col">
          <div className="flex items-center gap-1">
            <Calendar />
            <h1 className="font-bold text-3xl">Veículos</h1>
          </div>
          <span>Lista de todos os usuários do sistema</span>
        </div>

        <Dialog>
          <DialogContent>
            <DialogHeader>
              <DialogTitle className="text-3xl font-bold">Adicionar Veículo</DialogTitle>
              <DialogDescription>
                Preencha os Dados abaixo para cadastrar um veículo
              </DialogDescription>
            </DialogHeader>

            <form onSubmit={handleSubmit(createData)} className="flex flex-col gap-2">
              <div className="flex justify-center gap-5">
                <div className="w-full">
                  <Label htmlFor="plate">Placa</Label>
                  <FormField name="plate" control={control} placeholder="Placa" />
                  {errors.plate && <p className="text-red-600">{errors.plate.message}</p>}
                </div>

                <div className="w-full">
                  <Label htmlFor="make">Marca</Label>
                  <FormField name="make" control={control} placeholder="Marca" />
                  {errors.make && <p className="text-red-600">{errors.make.message}</p>}
                </div>
              </div>


              <div className="flex justify-center gap-5">

                <div className="w-full">
                  <Label htmlFor="model">Modelo</Label>
                  <FormField name="model" control={control} placeholder="Modelo" />
                  {errors.model && <p className="text-red-600">{errors.model.message}</p>}

                  {errors.monthlyVehicle && <p className="text-red-600">{errors.monthlyVehicle.message}</p>}
                  {errors.idCustomer && <p className="text-red-600">{errors.idCustomer.message}</p>}
                </div>
              </div>


              <Button className="mt-5 bg-black" type="submit">Salvar</Button>
            </form>
          </DialogContent>
          <DialogTrigger onClick={() => setEditId(null)} className=" px-4 py-1 bg-black text-white rounded-lg h-full">Adicionar Veículo</DialogTrigger>
        </Dialog>
      </div>

      <div className="overflow-auto flex justify-center">
        <Table columns={["id", "Placa", "Marca", "Modelo"]}>
          {vehicles.map((vehicle) => (
            <tr key={vehicle.id}>
              <td className="p-3 text-center">{vehicle.id}</td>
              <td className="p-3 text-center">{vehicle.plate}</td>
              <td className="p-3 text-center">{vehicle.make}</td>
              <td className="p-3 text-center">{vehicle.model}</td>
              <td className="p-3 text-center">
                <div className="flex gap-2 items-center justify-center">
                  <Dialog>
                    <DialogContent>
                      <DialogHeader>
                        <DialogTitle className="text-3xl font-bold">Deletar Veículo</DialogTitle>
                        <DialogDescription>
                          Tem certeza que deseja deletar este usuário? Está ação não poderá ser revertida!
                        </DialogDescription>
                      </DialogHeader>
                      <div className="flex justify-end items-center gap-2" >
                        <DialogClose className="bg-black w-32 text-white rounded-lg py-2" onClick={() => deleteData(vehicle.id)}>Sim</DialogClose>
                        <DialogClose className="bg-black w-32 text-white rounded-lg py-2">Não</DialogClose>
                      </div>
                    </DialogContent>
                    <DialogTrigger className=" px-4 py-1 bg-red-600 text-white rounded-lg"><Trash /></DialogTrigger>
                  </Dialog>

                  <Dialog>
                    <DialogContent>
                      <DialogHeader>
                        <DialogTitle className="text-3xl font-bold">Editar Veículo</DialogTitle>
                        <DialogDescription>
                          Preencha os Dados abaixo para atualizar o veículo
                        </DialogDescription>
                      </DialogHeader>

                      <form onSubmit={handleSubmit(updateData)} className="flex flex-col gap-2">
                        <div className="flex justify-center gap-5">
                          <div className="w-full">
                            <Label htmlFor="plate">Placa</Label>
                            <FormField name="plate" control={control} placeholder="Placa" />
                            {errors.plate && <p className="text-red-600">{errors.plate.message}</p>}
                          </div>

                          <div className="w-full">
                            <Label htmlFor="make">Marca</Label>
                            <FormField name="make" control={control} placeholder="Marca" />
                            {errors.make && <p className="text-red-600">{errors.make.message}</p>}
                          </div>
                        </div>


                        <div className="flex justify-center gap-5">
                          <div className="w-full">
                            <Label htmlFor="model">Modelo</Label>
                            <FormField name="model" control={control} placeholder="Modelo" />
                            {errors.model && <p className="text-red-600">{errors.model.message}</p>}
                          </div>
                        </div>



                        <Button className="mt-5 bg-black" type="submit">Salvar</Button>
                      </form>
                    </DialogContent>
                    <DialogTrigger onClick={() => setEditId(vehicle.id)} className="px-4 py-1 bg-yellow-600 text-white rounded-lg"><UserPen /></DialogTrigger>
                  </Dialog>
                </div>
              </td>
            </tr>
          ))}
        </Table>
      </div>

      <PaginationBox currentPage={currentPage} setCurrentPage={setCurrentPage} totalPages={totalPages} size={5} />

    </section>
  )
}

export default Veiculos