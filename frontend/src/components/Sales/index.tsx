import React from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../ui/card'
import { CarFront, ArrowRightLeft } from 'lucide-react'
import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'

const Sales = () => {
  return (
    <Card className='flex-1'>
        <CardHeader>
            <div className='flex items-center justify-center'>
                <CardTitle className='text-lg sm:text-xl text-gray-800'>
                    Últimas Movimentações
                </CardTitle>
                <ArrowRightLeft className="ml-auto w-4 h-4"/>
            </div>
            <CardDescription>
                Últimas movimentações registradas
            </CardDescription>
        </CardHeader>

        <CardContent>
            <article className='flex items-center gap-2 py-2 border-b'>
                <Avatar>  
                    <AvatarFallback><CarFront/></AvatarFallback>
                </Avatar>
                <div className='flex items-start flex-col'>
                    <p className='text-sm sm:text-base font-semibold'>Ford Fiesta - ACS8744</p>
                    <div className='flex gap-2'>
                        <span className='text-[12px] sm:text-sm text-gray-400'>Cliente: Thiago</span>
                        <span className='text-[12px] sm:text-sm text-gray-400'>Entrada: 15:38</span>
                        <span className='text-[12px] sm:text-sm text-gray-400'>Saida: 16:05</span>
                    </div>
                </div>
            </article>
        </CardContent>
    </Card>
  )
}

export default Sales;