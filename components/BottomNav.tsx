import { Home, Grid, Heart, User } from "lucide-react";

export default function BottomNav() {
  return (
    <div className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-100 py-2.5 px-6 flex justify-between items-center z-50 max-w-md mx-auto">
      <button className="flex flex-col items-center text-luxury-pink gap-0.5">
        <Home size={20} strokeWidth={1.8} />
        <span className="text-[9px] uppercase font-bold tracking-wider">Início</span>
      </button>
      <button className="flex flex-col items-center text-luxury-muted hover:text-luxury-text gap-0.5">
        <Grid size={20} strokeWidth={1.5} />
        <span className="text-[9px] uppercase tracking-wider">Categorias</span>
      </button>
      <button className="flex flex-col items-center text-luxury-muted hover:text-luxury-text gap-0.5">
        <Heart size={20} strokeWidth={1.5} />
        <span className="text-[9px] uppercase tracking-wider">Favoritos</span>
      </button>
      <button className="flex flex-col items-center text-luxury-muted hover:text-luxury-text gap-0.5">
        <User size={20} strokeWidth={1.5} />
        <span className="text-[9px] uppercase tracking-wider">Conta</span>
      </button>
    </div>
  );
}