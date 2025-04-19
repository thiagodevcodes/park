'use client'

import { FC, ReactNode } from "react";

interface HeaderProps {
  children: ReactNode;
}

const Header: React.FC<HeaderProps> = ({ children }) => {
  return (
    <header className="header">
      {children}
    </header>
  );
}

export default Header;