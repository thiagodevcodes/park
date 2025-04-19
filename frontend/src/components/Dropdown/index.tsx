'use client'

import { useState, ReactNode, FC } from "react";
import styles from "./dropdown.module.css";
import { ChevronDown, ChevronUp } from "lucide-react";

interface DropdownProps {
  children: ReactNode;
  title: string;
}

const Dropdown: React.FC<DropdownProps> = ({ children, title }) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div className={styles.dropdown}>
      <button className={styles.dropdown_toggle + " "} onClick={toggleDropdown}>
        <p className="text-black">{title}</p>
        { isOpen ? <ChevronUp color="black"/> : <ChevronDown color="black"/> }
      </button>
      {isOpen && (
        <div className="flex justify-center">
          <div className={styles.dropdown_menu}>
            {children}
          </div>
        </div>
      )}
    </div>
  );
};

export default Dropdown;
