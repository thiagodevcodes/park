import { GlobalContextProvider } from "@/contexts/GlobalContext";
import NextAuthSessionProvider from "@/providers/sessionProvider";
import type { Metadata } from "next";
import "../styles/globals.css";
import { Toaster } from "@/components/ui/toaster";

export const metadata: Metadata = {
  title: "SysPark",
  description: "Sistema para gestão de estacionamento",
};

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {


  return (
    <html lang="pt-br">
      <body>
        <NextAuthSessionProvider>
          <GlobalContextProvider>
            {children}
          </GlobalContextProvider>
        </NextAuthSessionProvider>
        <Toaster />
      </body>
    </html>
  );
}
