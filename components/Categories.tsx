"use client";

import { useApp } from "@/context/AppContext";
import { useEffect } from "react";

const materials = [
  {
    id: 1,
    name: "Ouro 18k",
    image:
      "https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=150&q=80",
  },
  {
    id: 2,
    name: "Titânio",
    image:
      "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=150&q=80",
  },
  {
    id: 3,
    name: "Aço Inox",
    image:
      "https://images.unsplash.com/photo-1630019852942-f89202989a59?w=150&q=80",
  },
];

export default function Categories() {

  const {
    selectedMaterial,
    setSelectedMaterial,
  } = useApp();

  useEffect(() => {

    if (
      selectedMaterial === "Todos"
    ) {

      setSelectedMaterial(
        "Ouro 18k"
      );
    }

  }, [
    selectedMaterial,
    setSelectedMaterial,
  ]);

  return (

    <div className="bg-white py-5 border-b border-zinc-100/60">

      <div className="flex justify-center gap-8">

        {materials.map((material) => {

          const isActive =
            selectedMaterial === material.name;

          return (

            <div
              key={material.id}
              onClick={() =>
                setSelectedMaterial(
                  material.name
                )
              }
              className="flex flex-col items-center gap-2 cursor-pointer group"
            >

              <div
                className={`w-16 h-16 rounded-full border p-0.5 flex items-center justify-center bg-[#FDFBFB] overflow-hidden transition-all duration-300 ${
                  isActive
                    ? "border-[#E8A3B3] scale-105 shadow-sm"
                    : "border-zinc-100"
                }`}
              >

                <img
                  src={material.image}
                  alt={material.name}
                  className="w-full h-full object-cover rounded-full mix-blend-multiply"
                />

              </div>

              <span
                className={`text-[10px] font-medium uppercase tracking-widest transition-colors ${
                  isActive
                    ? "text-[#E8A3B3] font-semibold"
                    : "text-[#1A1A1A]"
                }`}
              >

                {material.name}

              </span>

              <div
                className={`h-[1.5px] bg-[#E8A3B3] transition-all duration-300 ${
                  isActive
                    ? "w-4 mt-0.5"
                    : "w-0"
                }`}
              />

            </div>
          );
        })}

      </div>

    </div>
  );
}