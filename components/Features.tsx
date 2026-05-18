import { Gem, ShieldCheck, Box, Lock } from "lucide-react";

const features = [
  { icon: <Gem size={22} strokeWidth={1.2} />, title: "Joias Autênticas" },
  { icon: <ShieldCheck size={22} strokeWidth={1.2} />, title: "Garantia Vitalícia" },
  { icon: <Box size={22} strokeWidth={1.2} />, title: "Embalagem Premium" },
  { icon: <Lock size={22} strokeWidth={1.2} />, title: "Compra Segura" },
];

export default function Features() {
  return (
    <div className="grid grid-cols-4 bg-[#FBF6F6] py-5 px-1 border-t border-b border-gray-100 text-center my-4">
      {features.map((f, i) => (
        <div key={i} className="flex flex-col items-center justify-center text-[#1A1A1A] gap-1">
          <div className="text-[#1A1A1A]">{f.icon}</div>
          <span className="text-[9px] uppercase tracking-wider font-medium leading-tight max-w-[75px]">
            {f.title}
          </span>
        </div>
      ))}
    </div>
  );
}