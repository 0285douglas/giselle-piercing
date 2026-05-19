"use client";
import { useState } from "react";
import { ChevronRight } from "lucide-react";

const catalog = [
  { part: "Orelha", sub: ["Hélix", "Tragus", "Conch", "Lóbulo"] },
  { part: "Nariz", sub: ["Septo", "Nostril"] },
  { part: "Boca", sub: ["Língua", "Labret"] },
  { part: "Corporais", sub: ["Mamilo", "Umbigo", "Microdermal"] },
];

export default function CategoryView() {
  const [expanded, setExpanded] = useState<string | null>(null);

  return (
    <div className="p-6 bg-white min-h-screen pb-24">
      <h2 className="font-serif uppercase tracking-[0.2em] text-sm mb-8 text-center">Catálogo de Joias</h2>
      
      <div className="space-y-4">
        {catalog.map((item) => (
          <div key={item.part} className="border-b border-zinc-50 pb-4">
            <button 
              onClick={() => setExpanded(expanded === item.part ? null : item.part)}
              className="w-full flex justify-between items-center py-2"
            >
              <span className="text-sm font-medium text-[#1A1A1A] uppercase tracking-widest">{item.part}</span>
              <ChevronRight size={18} className={`transition-transform ${expanded === item.part ? "rotate-90 text-[#E8A3B3]" : "text-zinc-300"}`} />
            </button>
            
            {expanded === item.part && (
              <div className="grid grid-cols-2 gap-2 mt-4 animate-in slide-in-from-top-2">
                {item.sub.map(sub => (
                  <div key={sub} className="bg-zinc-50 p-4 rounded-xl group active:scale-95 transition-all">
                    <p className="text-xs font-bold text-zinc-500 uppercase mb-4">{sub}</p>
                    <div className="flex flex-col gap-2">
                      {["Ouro", "Titânio", "Aço"].map(mat => (
                        <button key={mat} className="text-[10px] text-left hover:text-[#E8A3B3] font-medium border-l border-zinc-200 pl-2">
                          Ver em {mat}
                        </button>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}