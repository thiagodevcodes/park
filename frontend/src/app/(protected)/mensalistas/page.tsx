"use client";

import React, { useContext, useEffect, useState } from "react";
import Table from "@/components/Table";
import { Trash, UserPen, Calendar, CarFront } from "lucide-react";
import { GlobalContext } from "@/contexts/GlobalContext";
import { handleDelete, fetchData, fetchDataAll, handleCreate, handleUpdate } from "@/services/axios";
import { useForm } from "react-hook-form";
import { z } from 'zod';
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import FormField from "@/components/FormField";
import PaginationBox from "@/components/Pagination";
import { validateCPF } from "@/utils/utils";
import { DialogClose } from "@/components/ui/dialog";
import { Label } from "@/components/ui/label"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { useToast } from "@/hooks/use-toast";
import Link from "next/link";

interface Customer {
  id: number;
  paymentDay: number
  person: Person;
  role: number;
}

interface Person {
  name: string;
  email: string;
  phone: string;
  cpf: string;
}

const preprocessNumber = (val: any) => {
  if (val === '' || val === null || val === undefined) return 0;
  return val;
};

const formSchema = z.object({
  paymentDay: z.preprocess(preprocessNumber,
    z.number()
      .min(1, "O dia de pagamento deve ser no mínimo 1")
      .max(31, "O dia de pagamento deve ser no máximo 31")
  ),
  idCustomerType: z.preprocess(preprocessNumber,
    z.number()
      .min(1, "O dia de pagamento deve ser no mínimo 1")
      .max(2, "O dia de pagamento deve ser no máximo 31")
  ),
  person: z.object({
    name: z.string().min(1, "Campo é obrigatório"),
    email: z.string().email("Email inválido"),
    phone: z.string().min(1, "Telefone é obrigatório"),
    cpf: z.string().refine(validateCPF, "CPF inválido")
  })
});

type UsersSchema = z.infer<typeof formSchema>;

const Mensalistas: React.FC = () => {
  const [message, setMessage] = useState<string>("");
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [size, setSize] = useState<number>(5);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [editId, setEditId] = useState<number | null>(null);
  const { toast } = useToast()

  const { handleSubmit, control, reset, formState: { errors } } = useForm<UsersSchema>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      idCustomerType: 1,
      paymentDay: 0,
      person: {
        name: "",
        email: "",
        phone: "",
        cpf: ""
      }
    }
  });

  const { sessionInfo } = useContext(GlobalContext);
  const token = sessionInfo?.accessToken;

  useEffect(() => {
    fetchDataAll(token, currentPage, "customers").then((res: any) => {
      setCustomers(res.content);
      setTotalPages(res.totalPages);
    });
  }, [token, currentPage, size]);

  useEffect(() => {
    if (editId !== null) {
      fetchData(token, editId, "customers").then((customer: Customer) => {
        console.log(customer)
        reset({
          paymentDay: customer.paymentDay,
          idCustomerType: 1,
          person: {
            name: customer.person.name ? customer.person.name : "",
            email: customer.person.email ? customer.person.email : "",
            phone: customer.person.phone ? customer.person.phone : "",
            cpf: customer.person.cpf ? customer.person.cpf : ""
          }
        });
      });
    } else {
      reset({
        paymentDay: 0,
        idCustomerType: 1,
        person: {
          name: "",
          email: "",
          phone: "",
          cpf: ""
        }
      })
    }
  }, [editId, token, reset]);

  const createData = async (data: UsersSchema) => {
    const result = await handleCreate(data, token, "customers");
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
        fetchDataAll(token, currentPage, "customers").then((res: any) => {
          setCustomers(res.content);
          setTotalPages(res.totalPages);
        });
      }
    }
  };

  const updateData = async (data: UsersSchema) => {
    if (editId !== null) {
      const result = await handleUpdate(data, token, "customers", editId);

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
          fetchDataAll(token, currentPage, "customers").then((res: any) => {
            setCustomers(res.content);
            setTotalPages(res.totalPages);
          });
        }
      }
    }
  };

  const deleteData = async (id: number) => {
    const result = await handleDelete(token, id, "customers");

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
          fetchDataAll(token, currentPage, "customers").then((res: any) => {
            setCustomers(res.content);
            setTotalPages(res.totalPages);
          });
        }
      }
    }
  };

  return (
    <section className="mx-10 px-3 py-1 mb-4">
      <div className="flex justify-center gap-96 my-5 items-center">
        <div className="flex flex-col">
          <div className="flex items-center gap-1">
            <Calendar />
            <h1 className="font-bold text-3xl">Clientes Mensalistas</h1>
          </div>
          <span>Lista de todos os usuários do sistema</span>
        </div>

        <Dialog>
          <DialogContent>
            <DialogHeader>
              <DialogTitle className="text-3xl font-bold">Adicionar Usuário</DialogTitle>
              <DialogDescription>
                Preencha os Dados abaixo para cadastrar um usuário
              </DialogDescription>
            </DialogHeader>

            <form onSubmit={handleSubmit(createData)} className="flex flex-col gap-2">
              <div className="flex justify-center gap-5">
                <div className="w-full">
                  <Label htmlFor="person.name">Nome</Label>
                  <FormField name="person.name" control={control} placeholder="Nome" />
                  {errors.person?.name && <p className="text-red-600">{errors.person.name.message}</p>}
                </div>

                <div className="w-full">
                  <Label htmlFor="person.cpf">CPF</Label>
                  <FormField name="person.cpf" control={control} placeholder="CPF" />
                  {errors.person?.cpf && <p className="text-red-600">{errors.person.cpf.message}</p>}
                </div>
              </div>


              <div className="flex justify-center gap-5">

                <div className="w-full">
                  <Label htmlFor="paymentDay">Dia do Pagamento</Label>
                  <FormField type="number" name="paymentDay" control={control} placeholder="Dia de Pagamento" />
                  {errors.paymentDay && <p className="text-red-600">{errors.paymentDay.message}</p>}
                </div>
                <div className="w-full">
                  <Label htmlFor="person.phone">Telefone</Label>
                  <FormField name="person.phone" control={control} placeholder="Telefone" />
                  {errors.person?.phone && <p className="text-red-600">{errors.person.phone.message}</p>}
                </div>
              </div>

              <div className="flex justify-center gap-5">
                <div className="w-full">
                  <Label htmlFor="person.email">E-mail</Label>
                  <FormField name="person.email" control={control} placeholder="Email" />
                  <FormField type="hidden" name="idCustomerType" control={control} placeholder="Email" value={1} />
                  {errors.person?.email && <p className="text-red-600">{errors.person.email.message}</p>}
                </div>
              </div>
              
              <Button className="mt-5 bg-black" type="submit">Salvar</Button>
            </form>
          </DialogContent>
          <DialogTrigger onClick={() => setEditId(null)} className=" px-4 py-1 bg-black text-white rounded-lg h-full">Adicionar Usuário</DialogTrigger>
        </Dialog>
      </div>

      <div className="overflow-auto flex justify-center">
        <Table columns={["Nome", "CPF", "Email", "Telefone", "Dia do Pagamento"]}>
          {customers.map((customer) => (
            <tr key={customer.id}>
              <td className="p-3 text-center">{customer.person.name}</td>
              <td className="p-3 text-center">{customer.person.cpf}</td>
              <td className="p-3 text-center">{customer.person.email}</td>
              <td className="p-3 text-center">{customer.person.phone}</td>
              <td className="p-3 text-center">{customer.paymentDay}</td>
              <td className="p-3 text-center">
                <div className="flex gap-2 items-center justify-center">
                  <Dialog>
                    <DialogContent>
                      <DialogHeader>
                        <DialogTitle className="text-3xl font-bold">Deletar Usuário</DialogTitle>
                        <DialogDescription>
                          Tem certeza que deseja deletar este usuário? Está ação não poderá ser revertida!
                        </DialogDescription>
                      </DialogHeader>
                      <div className="flex justify-end items-center gap-2" >
                        <DialogClose className="bg-black w-32 text-white rounded-lg py-2" onClick={() => deleteData(customer.id)}>Sim</DialogClose>
                        <DialogClose className="bg-black w-32 text-white rounded-lg py-2">Não</DialogClose>
                      </div>
                    </DialogContent>
                    <DialogTrigger className=" px-4 py-1 bg-red-600 text-white rounded-lg"><Trash /></DialogTrigger>
                  </Dialog>

                  <Dialog>
                    <DialogContent>
                      <DialogHeader>
                        <DialogTitle className="text-3xl font-bold">Editar Usuário</DialogTitle>
                        <DialogDescription>
                          Preencha os Dados abaixo para atualizar o usuário
                        </DialogDescription>
                      </DialogHeader>

                      <form onSubmit={handleSubmit(updateData)} className="flex flex-col gap-2">
                        <div className="flex justify-center gap-5">
                          <div className="w-full">
                            <Label htmlFor="person.name">Nome</Label>
                            <FormField name="person.name" control={control} placeholder="Nome" />
                            {errors.person?.name && <p className="text-red-600">{errors.person.name.message}</p>}
                          </div>

                          <div className="w-full">
                            <Label htmlFor="person.cpf">CPF</Label>
                            <FormField name="person.cpf" control={control} placeholder="CPF" />
                            {errors.person?.cpf && <p className="text-red-600">{errors.person.cpf.message}</p>}
                          </div>
                        </div>


                        <div className="flex justify-center gap-5">
                          <div className="w-full">
                            <Label htmlFor="paymentDay">Dia do Pagamento</Label>
                            <FormField type="number" name="paymentDay" control={control} placeholder="Dia de Pagamento" />
                            {errors.paymentDay && <p className="text-red-600">{errors.paymentDay.message}</p>}
                          </div>
                          <div className="w-full">
                            <Label htmlFor="person.phone">Telefone</Label>
                            <FormField name="person.phone" control={control} placeholder="Telefone" />
                            {errors.person?.phone && <p className="text-red-600">{errors.person.phone.message}</p>}
                          </div>
                        </div>

                        <div className="flex justify-center gap-5">
                          <div className="w-full">
                            <Label htmlFor="person.email">E-mail</Label>
                            <FormField name="person.email" control={control} placeholder="Email" />
                            {errors.person?.email && <p className="text-red-600">{errors.person.email.message}</p>}
                          </div>
                        </div>

                        <Button className="mt-5 bg-black" type="submit">Salvar</Button>
                      </form>
                    </DialogContent>
                    <DialogTrigger onClick={() => setEditId(customer.id)} className="px-4 py-1 bg-yellow-600 text-white rounded-lg"><UserPen /></DialogTrigger>
                  </Dialog>

                  <Link href={`/mensalistas/veiculos/${customer.id}`} className=" px-4 py-1 bg-blue-600 text-white rounded-lg">
                    <CarFront />
                  </Link>
                </div>
              </td>
            </tr>
          ))}
        </Table>
      </div>

      <PaginationBox currentPage={currentPage} setCurrentPage={setCurrentPage} totalPages={totalPages} size={5} />
    </section>
  );
};

export default Mensalistas;
