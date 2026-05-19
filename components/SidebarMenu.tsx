"use client";
import { useApp } from "@/context/AppContext";
import { X, ChevronRight, Home, Shield, Info, Phone } from "lucide-react";

export default function SidebarMenu() {
  const { isMenuOpen, setIsMenuOpen } = useApp();

  if (!isMenuOpen) return null;

  const menuItems = [
    { name: "Início", icon: <Home size={18} /> },
    { name: "Políticas de Garantia", icon: <Shield size={18} /> },
    { name: "Sobre a Giselle Piercing", icon: <Info size={18} /> },
    { name: "Fale Conosco", icon: <Phone size={18} /> },
  ];

  return (
    <div className="fixed inset-0 z-50 flex max-w-md mx-auto">
      {/* Background escuro */}
      <div className="fixed inset-0 bg-black/40" onClick={() => setIsMenuOpen(false)} />
      
      {/* Painel do Menu */}
      <div className="relative w-72 max-w-xs h-full bg-white shadow-xl flex flex-col p-6 animate-in slide-in-from-left duration-200">
        <div className="flex justify-between items-center mb-8 border-b pb-4">
          <h2 className="font-serif text-lg tracking-wider text-[#1A1A1A]">Navegação</h2>
          <button onClick={() => setIsMenuOpen(false)} className="text-zinc-400">
            <X size={20} />
          </button>
        </div>

        <nav className="flex flex-col gap-5">
          {menuItems.map((item, index) => (
            <button
              key={index}
              onClick={() => setIsMenuOpen(false)}
              className="flex justify-between items-center text-sm font-medium text-zinc-700 hover:text-[#E8A3B3] py-2 transition-colors text-left"
            >
              <div className="flex items-center gap-3">
                {item.icon}
                <span>{item.name}</span>
              </div>
              <ChevronRight size={14} className="text-zinc-400" />
            </button>
          ))}
        </nav>
      </div>
    </div>
  );
}