"use client";
import { useApp } from "@/context/AppContext";
import { Menu, Search, ShoppingBag } from "lucide-react";

export default function Header() {
  const { setIsMenuOpen, setIsCartOpen, cart } = useApp();
  const totalItems = cart.reduce((acc, item) => acc + item.quantity, 0);

  return (
    <header className="w-full flex justify-between items-center px-4 py-4 bg-white/80 backdrop-blur-md sticky top-0 z-50 border-b border-zinc-100/50">
      <button onClick={() => setIsMenuOpen(true)} className="text-[#1A1A1A] p-1">
        <Menu size={24} strokeWidth={1.2} />
      </button>
      
      <h1 className="text-xl font-serif tracking-[0.1em] uppercase text-[#1A1A1A] pl-4">
        Giselle Piercing
      </h1>
      
      <div className="flex gap-3 items-center text-[#1A1A1A]">
        <Search size={22} strokeWidth={1.2} className="cursor-pointer" />
        <button onClick={() => setIsCartOpen(true)} className="relative p-1">
          <ShoppingBag size={22} strokeWidth={1.2} />
          {totalItems > 0 && (
            <span className="absolute -top-0.5 -right-0.5 bg-[#E8A3B3] text-white text-[9px] w-4 h-4 rounded-full flex items-center justify-center font-semibold">
              {totalItems}
            </span>
          )}
        </button>
      </div>
    </header>
  );
}