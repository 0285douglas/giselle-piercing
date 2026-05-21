"use client";

import { useState, useEffect } from "react";
import { useApp } from "@/context/AppContext";
import { ShoppingCart } from "lucide-react";

interface Product {
  id: number;
  name: string;
  description: string;
  material: string;
  category: string;
  price: number;
  imageUrl: string;
}

const hotspots = [
  {
    name: "Hélix",
    top: "28%",
    left: "22%",
  },
  {
    name: "Tragus",
    top: "49%",
    left: "22%",
  },
  {
    name: "Mamilo",
    top: "70%",
    left: "22%",
  },
  {
    name: "Septo",
    top: "28%",
    left: "79%",
  },
  {
    name: "Nostril",
    top: "49%",
    left: "79%",
  },
  {
    name: "Língua",
    top: "70%",
    left: "79%",
  },
];

const materials = [
  "Ouro 18k",
  "Titânio",
  "Aço Inox",
];

export default function AnatomyView() {

  const { addToCart } = useApp();

  const [selectedPart, setSelectedPart] =
    useState<string | null>(null);

  const [activeMaterial, setActiveMaterial] =
    useState("Ouro 18k");

  const [products, setProducts] =
    useState<Product[]>([]);

  const [loading, setLoading] =
    useState(false);

  useEffect(() => {

    if (!selectedPart) {
      setProducts([]);
      return;
    }

    setLoading(true);

    const params = new URLSearchParams();

    params.append(
      "category",
      selectedPart
    );

    params.append(
      "material",
      activeMaterial
    );

    fetch(
      `http://localhost:8080/api/products?${params.toString()}`
    )
      .then((response) => {

        if (!response.ok) {
          throw new Error(
            "Erro ao buscar produtos"
          );
        }

        return response.json();
      })
      .then((data: Product[]) => {
        setProducts(data);
      })
      .catch((error) => {

        console.error(
          "Erro ao carregar anatomia:",
          error
        );
      })
      .finally(() => {
        setLoading(false);
      });

  }, [
    selectedPart,
    activeMaterial,
  ]);

  return (

    <div className="flex flex-col bg-white min-h-screen pb-20 animate-in fade-in">

      <div className="p-6 flex flex-col items-center">

        <h2 className="font-serif uppercase tracking-[0.2em] text-sm mb-6 text-[#1A1A1A]">
          Mapa Anatômico
        </h2>

        <div className="relative w-full max-w-[390px] aspect-square mb-8 bg-zinc-50 rounded-2xl flex items-center justify-center">

          <img
            src="/images/anatomia_corpo.png"
            alt="Escolha o local que deseja"
            className="w-full h-full object-contain p-4"
          />

          {hotspots.map((spot) => (

            <div
              key={spot.name}
              className="absolute z-10"
              style={{
                top: spot.top,
                left: spot.left,
              }}
            >

              <button
                onClick={() => {

                  const sameSpot =
                    selectedPart === spot.name;

                  setSelectedPart(
                    sameSpot ? null : spot.name
                  );
                }}
                className={`
                  absolute
                  -translate-x-1/2
                  -translate-y-1/2
                  w-4
                  h-4
                  rounded-full
                  border-2
                  border-white/80
                  shadow-md
                  transition-all
                  duration-200
                  backdrop-blur-sm
                  ${
                    selectedPart === spot.name
                      ? "bg-[#E8A3B3]/80 scale-125 ring-4 ring-[#E8A3B3]/20"
                      : "bg-[#E8A3B3]/60 hover:scale-110"
                  }
                `}
              />

              {selectedPart === spot.name && (

                <div className="absolute z-20 left-6 top-1/2 -translate-y-1/2 bg-white/95 backdrop-blur-md shadow-2xl rounded-lg p-2 border border-zinc-100 flex flex-col gap-1 min-w-[110px]">

                  <p className="text-[9px] font-bold text-zinc-400 uppercase px-1">
                    {spot.name}
                  </p>

                  {materials.map((material) => (

                    <button
                      key={material}
                      onClick={() =>
                        setActiveMaterial(material)
                      }
                      className={`text-[10px] text-left px-2 py-1.5 rounded transition-colors ${
                        activeMaterial === material
                          ? "bg-[#E8A3B3]/10 text-[#E8A3B3] font-bold"
                          : "text-zinc-600"
                      }`}
                    >

                      {material}

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

              <h3 className="text-sm font-serif uppercase tracking-wider text-[#1A1A1A]">

                {selectedPart}

                <span className="text-zinc-400 text-xs ml-1">
                  ({activeMaterial})
                </span>

              </h3>

              <span className="text-[10px] text-zinc-400 uppercase font-bold">

                {products.length} Joias

              </span>

            </div>

            {loading ? (

              <p className="text-xs text-zinc-400 text-center py-6">
                Carregando joias...
              </p>

            ) : products.length === 0 ? (

              <p className="text-xs text-zinc-400 text-center py-6">
                Nenhuma joia encontrada.
              </p>

            ) : (

              <div className="grid grid-cols-2 gap-3">

                {products.map((product) => (

                  <div
                    key={product.id}
                    className="bg-zinc-50/50 border border-zinc-100 rounded-xl p-3 relative flex flex-col justify-between"
                  >

                    <div>

                      <img
                        src={product.imageUrl}
                        alt={product.name}
                        className="w-full h-28 object-cover rounded-lg mb-2"
                      />

                      <h4 className="text-xs font-serif text-[#1A1A1A] truncate">
                        {product.name}
                      </h4>

                    </div>

                    <div className="flex justify-between items-center mt-2">

                      <p className="text-xs font-bold text-zinc-900">

                        {product.price.toLocaleString(
                          "pt-BR",
                          {
                            style: "currency",
                            currency: "BRL",
                          }
                        )}

                      </p>

                      <button
                        onClick={() => addToCart(product)}
                        className="bg-white p-1.5 rounded-full shadow-sm border border-zinc-100"
                      >

                        <ShoppingCart
                          size={12}
                          className="text-[#E8A3B3]"
                        />

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