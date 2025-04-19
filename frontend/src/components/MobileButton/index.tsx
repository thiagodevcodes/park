import { useContext } from "react";
import { GlobalContext } from "@/contexts/GlobalContext";

interface MobileButtonProps {
  color: string;
}

const MobileButton: React.FC<MobileButtonProps> = ({ color }) => {
  const { mobileActive, setMobileActive } = useContext(GlobalContext);  
  
  const handleClick = () => {
    setMobileActive(!mobileActive);
  };

  return (
    <button className="md:hidden bg-none border-none cursor-pointer" onClick={handleClick}>
      <div className={`w-8 h-1 ${color} m-2 transition-all rounded ${mobileActive ? '-rotate-45 translate-x-[0px] translate-y-[12px]' : ''}`}></div>
      <div className={`w-8 h-1 ${color} m-2 transition-all rounded ${mobileActive ? 'opacity-0' : ''}`}></div>
      <div className={`w-8 h-1 ${color} m-2 transition-all rounded ${mobileActive ? 'rotate-45 translate-x-[0px] translate-y-[-11px]' : ''}`}></div>
    </button>   
  );
}

export default MobileButton;