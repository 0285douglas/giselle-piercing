"use client";
import { useEffect, useState } from "react";
import { Heart, ShoppingCart, Award } from "lucide-react";
import { useApp } from "@/context/AppContext";

interface Product {
  id: number;
  title: string;
  material: string;
  price: number;
  image: string;
  bodyPart: string;
}

export default function ProductGrid() {
  const { addToCart, selectedCategory } = useApp();
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    
    // Define qual material buscar baseado na categoria selecionada na Home
    // Se selectedCategory for "Todos" ou vazio, busca a lista completa
    const categoria = selectedCategory && selectedCategory !== "Todos" ? selectedCategory : "";
    const url = categoria 
      ? `http://localhost:8080/api/products/material/${categoria}`
      : "http://localhost:8080/api/products";

    const timer = setTimeout(() => {
      fetch(url)
        .then((res) => res.ok ? res.json() : Promise.reject())
        .then((data) => setProducts(data))
        .catch((err) => console.error("Erro ao conectar com a API Java:", err))
        .finally(() => setLoading(false));
    }, 400);

    return () => clearTimeout(timer);
  }, [selectedCategory]);

  if (loading) {
    return (
      <div className="px-4 py-4 grid grid-cols-2 gap-3">
        {[1, 2, 3, 4].map((i) => (
          <div key={i} className="bg-white rounded-lg border border-zinc-100 p-3 flex flex-col gap-3 animate-pulse">
            <div className="w-full h-40 bg-zinc-100 rounded-md" />
            <div className="h-3 bg-zinc-100 rounded w-3/4" />
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className="px-4 py-3 bg-[#FDFBFB]">
      <div className="mb-4">
        <h3 className="text-sm font-serif tracking-widest uppercase text-[#1A1A1A]">
          Destaques {selectedCategory || "Gerais"}
        </h3>
      </div>

      <div className="grid grid-cols-2 gap-3">
        {products.map((product) => {
          const isTitanium = product.material?.toLowerCase() === "titânio";
          return (
            <div key={product.id} className="bg-white rounded-lg overflow-hidden border border-zinc-100 relative flex flex-col justify-between p-3">
              <div className="relative">
                {isTitanium && (
                  <div className="absolute top-2 left-2 z-10 flex items-center gap-1 bg-white/95 text-[#1A1A1A] font-semibold text-[8px] px-2 py-0.5 rounded shadow-xs uppercase">
                    <Award size={10} className="text-[#E8A3B3]" />
                    <span>ASTM F-136</span>
                  </div>
                )}
                <div className="w-full h-36 bg-zinc-50 flex items-center justify-center overflow-hidden rounded-md mb-2">
                  <img src={product.image} alt={product.title} className="w-full h-full object-cover" />
                </div>
                <h4 className="text-xs font-serif text-[#1A1A1A] truncate">{product.title}</h4>
                <p className="text-[10px] text-zinc-400 mt-0.5">{product.material}</p>
              </div>

              <div className="flex justify-between items-center mt-3">
                <span className="text-xs font-bold text-[#1A1A1A]">
                  {product.price?.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}
                </span>
                <button onClick={() => addToCart(product)} className="p-1.5 bg-[#E8A3B3] text-white rounded-full">
                  <ShoppingCart size={11} />
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}