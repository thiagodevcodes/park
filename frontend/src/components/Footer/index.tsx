import Image from "next/image";
import Link from "next/link";

const Footer: React.FC = () => {
  return (
    <footer className="footer">
      <div className="flex justify-center gap-20 flex-wrap bg-zinc-900 p-6">
        <Image src="/img/ParkingWhite.svg" width={80} height={80} alt="Logo do Systems On Solutions" />
        
        <ul className="flex flex-col text-center gap-3">
          <li className="text-zinc-400">
            <Link href="#">
              Home
            </Link>
          </li>
          <li className="text-zinc-400">
            <Link href="#">
              Institucional
            </Link>
          </li>
          <li className="text-zinc-400">
            <Link href="#">
              Projetos
            </Link>
          </li>          
          <li className="text-zinc-400">
            <Link href="#">
              Equipe
            </Link>
          </li>
        </ul>

        <div className="flex justify-between flex-col">
          <div>
            <h3 className="text-white font-bold">Nossas Redes Sociais</h3>
            <div className="flex items-center justify-center gap-2">
              <Link className="bg-zinc-800 rounded-md px-2 py-2" href="#">
                <Image width={18} height={18} src="/icons/instagram.svg" alt="Logo do Instagram" />
              </Link>
              <Link className="bg-zinc-800 rounded-md px-2 py-2" href="#">
                <Image width={18} height={18} src="/icons/whatsapp.svg" alt="Logo do Whatsapp" />
              </Link> 
            </div>
          </div>

          <div className="flex justify-center flex-col items-center">
            <h3 className="text-white font-bold">Telefone para contato</h3>
            <p className="text-white">(79) 99898-9898</p>
          </div>
        </div>
      </div>

      <div className="h-14 bg-black flex items-center justify-center">
        <p className="text-white text-xs">Todos os direitos reservados &copy; Systems On Solutions, 2024</p>
      </div>
    </footer>
  );
}

export default Footer;
