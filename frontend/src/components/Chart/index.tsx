"use client"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../ui/card'
import { DollarSign } from 'lucide-react'
import { ChartConfig, ChartContainer } from '../ui/chart'
import { Bar, CartesianGrid, XAxis, BarChart, YAxis } from 'recharts'

const ChartOverview = () => {
    const chartData = [
        { month: "Janeiro", desktop: 186, mobile: 80, total: 250 },
        { month: "Feveiro", desktop: 305, mobile: 200, total
            :100 },
        { month: "Março", desktop: 237, mobile: 120, total: 340 },
        { month: "Abril", desktop: 73, mobile: 190, total: 150 },
        { month: "Maio", desktop: 209, mobile: 130, total: 450 },
        { month: "Junho", desktop: 214, mobile: 140, total: 50 },
    ]

    const chartConfig = {
        desktop: {
          label: "Desktop",
          color: "#2563eb",
        },
        mobile: {
          label: "Mobile",
          color: "#60a5fa",
        },
    } satisfies ChartConfig

    return (
        <Card className='w-full md:w-1/2 md:max-w-[600px]'>
            <CardHeader>
                <div className='flex items-center justify-between'>
                    <div className='flex justify-center flex-col'>
                        <CardTitle className='text-lg sm:text-xl text-gray-800'>
                            Fluxo de Movimentações
                        </CardTitle>
                        <CardDescription>
                            Fluxo anual de movimentações
                        </CardDescription>
                    </div>
                    <DollarSign className=' h-4 w-4'/>
                </div>
            </CardHeader>

            <CardContent>
                <ChartContainer config={chartConfig} className='min-h-[200px] w-full'>
                    <BarChart data={chartData}>
                        <CartesianGrid vertical={false} />
                        <XAxis 
                            dataKey="month" 
                            tickLine={false} 
                            tickMargin={10} 
                            axisLine={false}
                            tickFormatter={(value) => value.slice(0,3)}
                        />
                        <YAxis
                            dataKey="total" 
                            tickLine={false} 
                            tickMargin={10} 
                            axisLine={false}             
                        />
                        <Bar dataKey="desktop" fill='var(--color-desktop)' radius={4}/>
                        <Bar dataKey="mobile" fill='var(--color-mobile' radius={4}/>
                    </BarChart>
                </ChartContainer>
            </CardContent>
        </Card>
    )
}

export default ChartOverview