"use client";

import React, { useContext, useEffect, useState } from "react";
import Table from "@/components/Table";
import { Users, Trash, UserPen } from "lucide-react";
import { GlobalContext } from "@/contexts/GlobalContext";
import { fetchData, fetchDataAll, handleCreate, handleDelete, handleUpdate } from "@/services/axios";
import { useForm, Controller } from "react-hook-form";
import { z } from 'zod';
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "next/navigation";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import FormField from "@/components/FormField";
import PaginationBox from "@/components/Pagination";
import { validateCPF } from "@/utils/utils";
import { DialogClose } from "@/components/ui/dialog";
import { Label } from "@/components/ui/label"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { useToast } from "@/hooks/use-toast";

interface User {
  id: number;
  username: string;
  password: string;
  person: Person;
  role: number;
}

interface Person {
  name: string;
  email: string;
  phone: string;
  cpf: string;
}

const formSchema = z.object({
  username: z.string().min(1, "Campo é obrigatório"),
  password: z.nullable(z.string().min(1, "Mínimo 4 Caracteres")),
  role: z.number().min(1, "Campo é obrigatório"),
  person: z.object({
    name: z.string().min(1, "Campo é obrigatório"),
    email: z.string().email("Email inválido"),
    phone: z.string().min(1, "Telefone é obrigatório"),
    cpf: z.string().refine(validateCPF, "CPF inválido")
  })
});

type UsersSchema = z.infer<typeof formSchema>;

const AdminUsers: React.FC = () => {
  const [message, setMessage] = useState<string>("");
  const [currentPage, setCurrentPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [size, setSize] = useState<number>(5);
  const [users, setUsers] = useState<User[]>([]);
  const [editId, setEditId] = useState<number | null>(null);
  const { toast } = useToast()

  const { handleSubmit, control, reset, formState: { errors } } = useForm<UsersSchema>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: "",
      password: "",
      role: 2,
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
    fetchDataAll(token, currentPage, "users").then((res: any) => {
      setUsers(res.content);
      setTotalPages(res.totalPages);
    });
  }, [token, currentPage, size]);

  useEffect(() => {
    if (editId !== null) {
      fetchData(token, editId, "users").then((user: User) => {
        reset({
          username: user.username,
          password: null,
          role: user.role,
          person: {
            name: user.person.name ? user.person.name : "",
            email: user.person.email ? user.person.email : "",
            phone: user.person.phone ? user.person.phone : "",
            cpf: user.person.cpf ? user.person.cpf : ""
          }
        });
      });
    } else {
      reset({
        username: "",
        password: "",
        role: 2,
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
    const result = await handleCreate(data, token, "users");

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
        fetchDataAll(token, currentPage, "users").then((res: any) => {
          setUsers(res.content);
          setTotalPages(res.totalPages);
        });
      }
    }
  };

  const updateData = async (data: UsersSchema) => {
    if (editId !== null) {
      const result = await handleUpdate(data, token, "users", editId);

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
          fetchDataAll(token, currentPage, "users").then((res: any) => {
            setUsers(res.content);
            setTotalPages(res.totalPages);
          });
        }
      }
    }
  };

  const deleteData = async (id: number) => {
    const result = await handleDelete(token, id, "users");

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
          fetchDataAll(token, currentPage, "users").then((res: any) => {
            setUsers(res.content);
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
            <Users />
            <h1 className="font-bold text-3xl">Usuários</h1>
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
                  <Label htmlFor="username">Username</Label>
                  <FormField name="username" control={control} placeholder="Nome de Usuário" />
                  {errors.username && <p className="text-red-600">{errors.username.message}</p>}
                </div>
                <div className="w-full">
                  <Label htmlFor="password">Senha</Label>
                  <FormField name="password" control={control} placeholder="Senha" type="password" />
                  {errors.password && <p className="text-red-600">{errors.password.message}</p>}
                </div>
              </div>

              <div className="flex justify-center gap-5">
                <div className="w-full">
                  <Label htmlFor="person.email">E-mail</Label>
                  <FormField name="person.email" control={control} placeholder="Email" />
                  {errors.person?.email && <p className="text-red-600">{errors.person.email.message}</p>}
                </div>
                <div className="w-full">
                  <Label htmlFor="person.phone">Telefone</Label>
                  <FormField name="person.phone" control={control} placeholder="Telefone" />
                  {errors.person?.phone && <p className="text-red-600">{errors.person.phone.message}</p>}
                </div>
              </div>

              <Label htmlFor="role">Permissão</Label>
              <Controller
                name="role"
                control={control}
                render={({ field }) => (
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value.toString()}
                  >
                    <SelectTrigger className="w-full">
                      <SelectValue placeholder="Permissão" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="2">Colaborador</SelectItem>
                      <SelectItem value="1">Administrador</SelectItem>
                    </SelectContent>
                  </Select>
                )}
              />
              {errors.role && <p className="text-red-600">{errors.role.message}</p>}

              <Button className="mt-5 bg-black" type="submit">Salvar</Button>
            </form>
          </DialogContent>
          <DialogTrigger onClick={() => setEditId(null)} className=" px-4 py-1 bg-black text-white rounded-lg h-full">Adicionar Usuário</DialogTrigger>
        </Dialog>
      </div>

      <div className="overflow-auto flex justify-center">
        <Table columns={["Nome", "Username", "CPF", "Email", "Telefone", "Permissões"]}>
          {users.map((user) => (
            <tr key={user.id}>
              <td className="p-3 text-center">{user.person.name}</td>
              <td className="p-3 text-center">{user.username}</td>
              <td className="p-3 text-center">{user.person.cpf}</td>
              <td className="p-3 text-center">{user.person.email}</td>
              <td className="p-3 text-center">{user.person.phone}</td>
              <td className="p-3 text-center">
                {user.role === 1 ? "Administrador" : "Colaborador"}
              </td>
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
                        <DialogClose className="bg-black w-32 text-white rounded-lg py-2" onClick={() => deleteData(user.id)}>Sim</DialogClose>
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
                            <Label htmlFor="username">Username</Label>
                            <FormField name="username" control={control} placeholder="Nome de Usuário" />
                            {errors.username && <p className="text-red-600">{errors.username.message}</p>}
                          </div>
                          <div className="w-full">
                            <Label htmlFor="person.email">E-mail</Label>
                            <FormField name="person.email" control={control} placeholder="Email" />
                            {errors.person?.email && <p className="text-red-600">{errors.person.email.message}</p>}
                          </div>
                        </div>

                        <div className="flex justify-center gap-5">
                          <div className="w-full">
                            <Label htmlFor="person.phone">Telefone</Label>
                            <FormField name="person.phone" control={control} placeholder="Telefone" />
                            {errors.person?.phone && <p className="text-red-600">{errors.person.phone.message}</p>}
                          </div>

                          <div className="w-full">
                            <Label htmlFor="role">Permissão</Label>
                            <Controller
                              name="role"
                              control={control}
                              render={({ field }) => (
                                <Select
                                  onValueChange={(value) => field.onChange(Number(value))}
                                  value={field.value.toString()}
                                >
                                  <SelectTrigger className="w-full">
                                    <SelectValue placeholder="Permissão" />
                                  </SelectTrigger>
                                  <SelectContent>
                                    <SelectItem value="2">Colaborador</SelectItem>
                                    <SelectItem value="1">Administrador</SelectItem>
                                  </SelectContent>
                                </Select>
                              )}
                            />
                            {errors.role && <p className="text-red-600">{errors.role.message}</p>}
                          </div>
                        </div>

                        <Button className="mt-5 bg-black" type="submit">Salvar</Button>
                      </form>
                    </DialogContent>
                    <DialogTrigger onClick={() => setEditId(user.id)} className="px-4 py-1 bg-yellow-600 text-white rounded-lg"><UserPen /></DialogTrigger>
                  </Dialog>
       
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

export default AdminUsers;
