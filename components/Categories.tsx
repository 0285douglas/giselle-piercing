"use client";
import { useApp } from "@/context/AppContext";
import { useEffect } from "react";

const categories = [
  { id: 1, name: "Ouro 18k", image: "https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=150&q=80" },
  { id: 2, name: "Titânio", image: "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=150&q=80" },
  { id: 3, name: "Aço Inox", image: "https://images.unsplash.com/photo-1630019852942-f89202989a59?w=150&q=80" },
];

export default function Categories() {
  const { selectedCategory, setSelectedCategory } = useApp();

  useEffect(() => {
    if (selectedCategory === "Todos") {
      setSelectedCategory("Ouro 18k");
    }
  }, [selectedCategory, setSelectedCategory]);

  return (
    <div className="bg-white py-5 border-b border-zinc-100/60">
      <div className="flex justify-center gap-8">
        {categories.map((cat) => {
          const isActive = selectedCategory === cat.name;
          return (
            <div 
              key={cat.id} 
              onClick={() => setSelectedCategory(cat.name)} 
              className="flex flex-col items-center gap-2 cursor-pointer group"
            >
              {/* Círculo minimalista com efeito soft */}
              <div className={`w-16 h-16 rounded-full border p-0.5 flex items-center justify-center bg-[#FDFBFB] overflow-hidden transition-all duration-300 ${
                isActive ? "border-[#E8A3B3] scale-105 shadow-sm" : "border-zinc-100"
              }`}>
                <img 
                  src={cat.image} 
                  alt={cat.name} 
                  className="w-full h-full object-cover rounded-full mix-blend-multiply" 
                />
              </div>
              <span className={`text-[10px] font-medium uppercase tracking-widest transition-colors ${
                isActive ? "text-[#E8A3B3] font-semibold" : "text-[#1A1A1A]"
              }`}>
                {cat.name}
              </span>
              {/* Linha indicadora fina */}
              <div className={`h-[1.5px] bg-[#E8A3B3] transition-all duration-300 ${isActive ? "w-4 mt-0.5" : "w-0"}`} />
            </div>
          );
        })}
      </div>
    </div>
  );
}