"use client";
import { useState, useEffect } from "react";
import { useApp } from "@/context/AppContext";
import { ShoppingCart } from "lucide-react";

interface Product {
  id: number;
  title: string;
  material: string;
  price: number;
  image: string;
  bodyPart: string;
}

const hotspots = [
  { name: "Hélix", top: "12%", left: "18%" },
  { name: "Tragus", top: "28%", left: "18%" },
  { name: "Mamilo", top: "68%", left: "18%" },
  { name: "Septo", top: "12%", left: "82%" },
  { name: "Nostril", top: "28%", left: "82%" },
];

const materials = ["Ouro 18k", "Titânio", "Aço Inox"];

export default function AnatomyView() {
  const { addToCart } = useApp();
  const [selectedPart, setSelectedPart] = useState<string | null>(null);
  const [activeMaterial, setActiveMaterial] = useState("Ouro 18k");
  const [products, setProducts] = useState<Product[]>([]);

  // Sempre que a parte do corpo ou o material mudar, consulta o Backend em Java
  useEffect(() => {
    if (!selectedPart) return;

    fetch(`http://localhost:8080/api/products/bodypart/${selectedPart}`)
      .then((res) => res.json())
      .then((data) => {
        // Filtra os produtos daquela região anatômica pelo material selecionado
        const filtered = data.filter((p: Product) => 
          p.material.toLowerCase() === activeMaterial.toLowerCase()
        );
        setProducts(filtered);
      })
      .catch((err) => console.error("Erro ao carregar anatomia:", err));
  }, [selectedPart, activeMaterial]);

  return (
    <div className="flex flex-col bg-white min-h-screen pb-20 animate-in fade-in">
      <div className="p-6 flex flex-col items-center">
        <h2 className="font-serif uppercase tracking-[0.2em] text-sm mb-6 text-[#1A1A1A]">Mapa Anatômico</h2>
        
        <div className="relative w-full max-w-[390px] aspect-square mb-8 bg-zinc-50 rounded-2xl flex items-center justify-center">
          <img src="images/anatomia_corpo.png" alt="Corpo" className="w-full h-full object-contain p-4" />
          
          {hotspots.map((spot) => (
            <div key={spot.name} className="absolute" style={{ top: spot.top, left: spot.left }}>
              <button 
                onClick={() => {
                  setSelectedPart(selectedPart === spot.name ? null : spot.name);
                  if (selectedPart !== spot.name) setProducts([]); // limpa lista antiga ao trocar de ponto
                }}
                className={`w-4 h-4 rounded-full border-2 border-white shadow-lg transition-transform ${
                  selectedPart === spot.name ? "bg-[#E8A3B3] scale-125" : "bg-zinc-400"
                }`}
              />
              
              {selectedPart === spot.name && (
                <div className="absolute z-20 left-6 top-1/2 -translate-y-1/2 bg-white shadow-2xl rounded-lg p-2 border border-zinc-100 flex flex-col gap-1 min-w-[110px]">
                  <p className="text-[9px] font-bold text-zinc-400 uppercase px-1">{spot.name}</p>
                  {materials.map(m => (
                    <button 
                      key={m} 
                      onClick={() => setActiveMaterial(m)}
                      className={`text-[10px] text-left px-2 py-1.5 rounded transition-colors ${
                        activeMaterial === m ? "bg-[#E8A3B3]/10 text-[#E8A3B3] font-bold" : "text-zinc-600"
                      }`}
                    >
                      {m}
                    </button>
                  ))}
                </div>
              )}
            </div>
          ))}
        </div>

        {selectedPart && (
          <div className="w-full mt-2 animate-in slide-in-from-bottom-4">
            <div className="flex justify-between items-end mb-4 border-b border-zinc-100 pb-2">
              <h3 className="text-sm font-serif uppercase tracking-wider text-[#1A1A1A]">{selectedPart} <span className="text-zinc-400 text-xs">({activeMaterial})</span></h3>
              <span className="text-[10px] text-zinc-400 uppercase font-bold">{products.length} Joias</span>
            </div>
            
            {products.length === 0 ? (
              <p className="text-xs text-zinc-400 text-center py-6">Nenhuma joia em {activeMaterial} cadastrada para esta região.</p>
            ) : (
              <div className="grid grid-cols-2 gap-3">
                {products.map(product => (
                  <div key={product.id} className="bg-zinc-50/50 border border-zinc-100 rounded-xl p-3 relative flex flex-col justify-between">
                    <div>
                      <img src={product.image} className="w-full h-28 object-cover rounded-lg mb-2" />
                      <h4 className="text-xs font-serif text-[#1A1A1A] truncate">{product.title}</h4>
                    </div>
                    <div className="flex justify-between items-center mt-2">
                      <p className="text-xs font-bold text-zinc-900">R$ {product.price},00</p>
                      <button onClick={() => addToCart(product)} className="bg-white p-1.5 rounded-full shadow-sm border border-zinc-100">
                        <ShoppingCart size={12} className="text-[#E8A3B3]" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}