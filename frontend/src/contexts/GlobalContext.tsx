'use client'

import { DefaultSession } from 'next-auth';
import { useSession } from 'next-auth/react';
import React, { createContext, useState, useEffect, ReactNode } from 'react';


interface CustomSession extends DefaultSession {
  user: {
    accessToken: string;
    role: string;
    name: string;
    address: string;
  };
}

interface MobileContextType {
  screenWidth: number;
  mobileActive: boolean;
  sessionInfo: CustomSession["user"] | undefined; // Adjust to match the user type
  setMobileActive: (active: boolean) => void;
  status: string; // Include status in context type
}

interface MyContextProviderProps {
  children: ReactNode;
}

const defaultContextValue: MobileContextType = {
  screenWidth: 9999,
  mobileActive: false,
  sessionInfo: undefined,
  setMobileActive: () => {},
  status: 'unauthenticated', 
};

export const GlobalContext = createContext<MobileContextType>(defaultContextValue);

export const GlobalContextProvider: React.FC<MyContextProviderProps> = ({ children }) => {
  const [screenWidth, setScreenWidth] = useState<number>(9999);
  const [mobileActive, setMobileActive] = useState<boolean>(false);
  const { data: session, status } = useSession();

  const sessionInfo = (session as CustomSession)?.user;
  
  useEffect(() => {
    const handleResize = () => {
      setScreenWidth(window.innerWidth);
    };

    window.addEventListener('resize', handleResize);
    setScreenWidth(window.innerWidth);

    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  if(status == "loading") {
    return
  }

  return (
    <GlobalContext.Provider value={{ screenWidth, mobileActive, setMobileActive, sessionInfo, status }}>
      {children}
    </GlobalContext.Provider>
  );
};