"use client"

import FormField from "@/components/FormField";
import { GlobalContext } from "@/contexts/GlobalContext";
import axios from "axios";
import React, { useContext, useEffect, useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { z } from 'zod'
import { zodResolver } from "@hookform/resolvers/zod"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { handleCreate, handleUpdate } from "@/services/axios";
import { Button } from "@/components/ui/button";

interface User {
  id: number,
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
  username: z.string().min(1, "Nome de usuário é obrigatório"),
  role: z.number().min(1, "Permissão é obrigatório"),
  person: z.object({
    name: z.string().min(1, "Nome é obrigatório"),
    email: z.string().email("Email inválido"),
    phone: z.string().min(1, "Telefone é obrigatório"),
    cpf: z.string().min(1, "CPF é obrigatório"),
  })
});

type UsersSchema = z.infer<typeof formSchema>

const AdminUsersEdit = ({ params }: { params: { id: number } }) => {
  const [message, setMessage] = useState("");
  const { sessionInfo } = useContext(GlobalContext)
  const token = sessionInfo?.accessToken;
  const id = params.id

  const { register, handleSubmit, control, setValue, reset, formState: { errors } } = useForm<UsersSchema>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: "",
      role: 2,
      person: {
        name: "",
        email: "",
        phone: "",
        cpf: ""
      }
    }
  });

  useEffect(() => {
    if (id !== undefined) {
      fetchUser(token, id, "users").then((user: User) => {

        reset({
          username: user.username,
          role: user.role,
          person: {
            name: user.person.name,
            email: user.person.email,
            phone: user.person.phone,
            cpf: user.person.cpf
          }
        });
      });
    }
  }, [id, token, reset]);


  const createData = async (data: any) => {
    console.log(data)
    const result = await handleUpdate(data, token, "users", id);
    
    if (result) {
      console.log(result)
      setMessage(result.message)
    }
  }

  return (
    <div className="flex flex-col gap-2 justify-center items-center h-screen">
      <h1>Edição de Usuário</h1>

      <form onSubmit={handleSubmit(createData)} className="flex flex-col gap-2">
        <div className="flex gap-4">
          <FormField name="person.name" control={control} placeholder="Nome" />
          {errors.person?.name && <p className="text-red-600">{errors.person.name.message}</p>}
          <FormField name="username" control={control} placeholder="Nome de Usuário" />
          {errors.username && <p className="text-red-600">{errors.username.message}</p>}
        </div>

        <div className="flex gap-4">
          <FormField name="person.cpf" control={control} placeholder="CPF" />
          {errors.person?.cpf && <p className="text-red-600">{errors.person.cpf.message}</p>}
          <FormField name="person.email" control={control} placeholder="Email" />
          {errors.person?.email && <p className="text-red-600">{errors.person.email.message}</p>}
        </div>

        <div className="flex gap-4">
          <FormField name="person.phone" control={control} placeholder="Telefone" />
          {errors.person?.phone && <p className="text-red-600">{errors.person.phone.message}</p>}
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
          {errors.role && <p className="text-red-600" >{errors.role.message}</p>}
        </div>




        <div className="text-center">
          {message ? message : null}
        </div>
        <Button className="bg-black" type="submit">Salvar</Button>
      </form>
    </div>
  );
}

export default AdminUsersEdit;