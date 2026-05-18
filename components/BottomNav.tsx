"use client";
import { Home, LayoutGrid, Sparkles, User } from "lucide-react";
import { useApp, ViewType } from "@/context/AppContext";

export default function BottomNav() {
  const { activeView, setActiveView } = useApp();

  const navItems: { id: ViewType; label: string; icon: any }[] = [
    { id: 'HOME', label: 'Início', icon: Home },
    { id: 'CATEGORIES', label: 'Categorias', icon: LayoutGrid },
    { id: 'ANATOMY', label: 'Anatomia', icon: Sparkles },
    { id: 'ACCOUNT', label: 'Conta', icon: User },
  ];

  return (
    <div className="fixed bottom-0 left-0 right-0 bg-white/95 backdrop-blur-md border-t border-zinc-100 py-3 px-6 flex justify-between items-center z-50 max-w-md mx-auto shadow-[0_-4px_10px_rgba(0,0,0,0.03)]">
      {navItems.map((item) => (
        <button
          key={item.id}
          onClick={() => setActiveView(item.id)}
          className={`flex flex-col items-center gap-1 transition-all ${
            activeView === item.id ? "text-[#E8A3B3]" : "text-zinc-300"
          }`}
        >
          <item.icon size={22} strokeWidth={activeView === item.id ? 2.5 : 1.5} />
          <span className="text-[10px] font-bold uppercase tracking-tighter">{item.label}</span>
        </button>
      ))}
    </div>
  );
}