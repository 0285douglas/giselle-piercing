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
}

const fallbackProducts: Product[] = [
  // 4 de Ouro 18k
  { id: 1, title: "Argola Hélix Clicker", material: "Ouro 18k", price: 329.00, image: "https://images.unsplash.com/photo-1630019852942-f89202989a59?w=400&q=80" },
  { id: 2, title: "Nostril Ponto de Luz", material: "Ouro 18k", price: 189.00, image: "https://images.unsplash.com/photo-1617038260897-41a1f14a8ca0?w=400&q=80" },
  { id: 3, title: "Tragus Coração Slim", material: "Ouro 18k", price: 249.00, image: "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400&q=80" },
  { id: 4, title: "Luxury Gold Septo", material: "Ouro 18k", price: 449.00, image: "https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&q=80" },
  
  // 4 de Titânio
  { id: 5, title: "Labret Trinity", material: "Titânio", price: 219.00, image: "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&q=80" },
  { id: 6, title: "Segmentado Articulado", material: "Titânio", price: 159.00, image: "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400&q=80" },
  { id: 7, title: "Cluster Florescido Hélix", material: "Titânio", price: 289.00, image: "https://images.unsplash.com/photo-1630019852942-f89202989a59?w=400&q=80" },
  { id: 8, title: "Ferradura Clássica Septo", material: "Titânio", price: 139.00, image: "https://images.unsplash.com/photo-1617038260897-41a1f14a8ca0?w=400&q=80" },

  // 4 de Aço Inox
  { id: 9, title: "Industrial Piercing Barbell", material: "Aço Inox", price: 99.00, image: "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400&q=80" },
  { id: 10, title: "Banana Bell Umbigo", material: "Aço Inox", price: 119.00, image: "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&q=80" },
  { id: 11, title: "Captive Ball Ring", material: "Aço Inox", price: 79.00, image: "https://images.unsplash.com/photo-1630019852942-f89202989a59?w=400&q=80" },
  { id: 12, title: "Mamilo Cravejado Par", material: "Aço Inox", price: 169.00, image: "https://images.unsplash.com/photo-1617038260897-41a1f14a8ca0?w=400&q=80" }
];

export default function ProductGrid() {
  const { addToCart, selectedCategory } = useApp();
  const [products, setProducts] = useState<Product[]>(fallbackProducts);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    const timer = setTimeout(() => {
      fetch("http://localhost:8080/api/products")
        .then((res) => res.ok ? res.json() : Promise.reject())
        .then((data) => { if (data?.length > 0) setProducts(data); })
        .catch(() => console.log("Usando banco expandido de joias."))
        .finally(() => setLoading(false));
    }, 800); // 800ms de animação de esqueleto ao trocar de aba

    return () => clearTimeout(timer);
  }, [selectedCategory]);

  const filteredProducts = products.filter(p => p.material.toLowerCase() === selectedCategory.toLowerCase());

  if (loading) {
    return (
      <div className="px-4 py-4 grid grid-cols-2 gap-3">
        {[1, 2, 3, 4].map((i) => (
          <div key={i} className="bg-white rounded-lg border border-zinc-100 p-3 flex flex-col gap-3 animate-pulse">
            <div className="w-full h-40 bg-zinc-100 rounded-md" />
            <div className="h-3 bg-zinc-100 rounded w-3/4" />
            <div className="h-2 bg-zinc-100 rounded w-1/2" />
            <div className="flex justify-between items-center mt-2">
              <div className="h-4 bg-zinc-100 rounded w-1/3" />
              <div className="w-7 h-7 bg-zinc-100 rounded-full" />
            </div>
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className="px-4 py-3 bg-[#FDFBFB]">
      <div className="flex justify-between items-center mb-4">
        <h3 className="text-sm font-serif tracking-widest uppercase text-[#1A1A1A]">
          Destaques {selectedCategory}
        </h3>
      </div>

      <div className="grid grid-cols-2 gap-3">
        {filteredProducts.map((product) => {
          const isTitanium = product.material.toLowerCase() === "titânio";
          return (
            <div key={product.id} className="bg-white rounded-lg overflow-hidden border border-zinc-100 relative shadow-xs flex flex-col justify-between">
              <div>
                <button className="absolute top-2 right-2 z-10 p-1.5 bg-white/80 backdrop-blur-sm rounded-full text-zinc-400 hover:text-red-500 transition-colors">
                  <Heart size={14} />
                </button>
                
                {isTitanium && (
                  <div className="absolute top-2 left-2 z-10 flex items-center gap-1 bg-white/95 backdrop-blur-sm border border-zinc-200/60 text-[#1A1A1A] font-semibold text-[8px] px-2 py-0.5 rounded shadow-xs uppercase tracking-wider">
                    <Award size={10} className="text-[#E8A3B3]" />
                    <span>ASTM F-136</span>
                  </div>
                )}

                <div className="w-full h-40 bg-zinc-50 flex items-center justify-center overflow-hidden">
                  <img src={product.image} alt={product.title} className="w-full h-full object-cover" />
                </div>
                
                <div className="p-3 pb-1">
                  <h4 className="text-xs font-serif text-[#1A1A1A] truncate">{product.title}</h4>
                  <p className="text-[10px] text-zinc-400 mt-0.5">{product.material}</p>
                </div>
              </div>

              <div className="p-3 pt-0 flex justify-between items-center mt-2">
                <span className="text-xs font-bold text-[#1A1A1A]">
                  {product.price.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}
                </span>
                <button 
                  onClick={() => addToCart(product)} 
                  className="p-1.5 bg-[#E8A3B3] text-white rounded-full hover:bg-[#df92a3] active:scale-95 transition-all"
                >
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