"use client";
import { ArrowRight } from "lucide-react";
import { useRef, useState } from "react";

export default function Hero() {
  const scrollRef = useRef<HTMLDivElement>(null);
  const [currentSlide, setCurrentSlide] = useState(0);

  const handleScroll = () => {
    if (scrollRef.current) {
      const width = scrollRef.current.offsetWidth;
      const index = Math.round(scrollRef.current.scrollLeft / width);
      setCurrentSlide(index);
    }
  };

  return (
    <div className="w-full relative">
      <div 
        ref={scrollRef}
        onScroll={handleScroll}
        className="w-full flex overflow-x-auto snap-x snap-mandatory scrollbar-none"
      >
        {/* Slide 1 */}
        <div className="min-w-full h-[360px] bg-[#EAE3DC] bg-cover bg-center flex items-center px-6 snap-start"
             style={{ backgroundImage: "url('https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=600&q=80')" }}>
          <div className="max-w-[240px]">
            <span className="text-[#E8A3B3] text-xs font-bold uppercase tracking-widest">Nova Coleção</span>
            <h2 className="text-2xl font-serif text-[#1A1A1A] mt-1 leading-tight">Ouro & Titânio Premium</h2>
            <p className="text-xs text-zinc-500 mt-2">Designs exclusivos para elevar sua essência.</p>
            <button className="mt-4 bg-[#E8A3B3] text-white text-[11px] font-semibold px-5 py-2 rounded-full flex items-center gap-2 uppercase tracking-wider">
              Descubra Agora <ArrowRight size={12} />
            </button>
          </div>
        </div>

        {/* Slide 2 */}
        <div className="min-w-full h-[360px] bg-[#E5E5E5] bg-cover bg-center flex items-center px-6 snap-start"
             style={{ backgroundImage: "url('https://images.unsplash.com/photo-1601121141461-9d6647bca1ed?w=600&q=80')" }}>
          <div className="max-w-[240px]">
            <span className="text-[#E8A3B3] text-xs font-bold uppercase tracking-widest">Coleção Minimal</span>
            <h2 className="text-2xl font-serif text-[#1A1A1A] mt-1 leading-tight">Linhas Finas & Aço</h2>
            <p className="text-xs text-zinc-500 mt-2">A beleza no que é essencial e discreto.</p>
            <button className="mt-4 bg-[#E8A3B3] text-white text-[11px] font-semibold px-5 py-2 rounded-full flex items-center gap-2 uppercase tracking-wider">
              Ver Modelos <ArrowRight size={12} />
            </button>
          </div>
        </div>
      </div>

      {/* Indicadores Dinâmicos de Bolinha */}
      <div className="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-2 z-10 pointer-events-none">
        <span className={`h-1.5 rounded-full transition-all duration-300 ${currentSlide === 0 ? "w-4 bg-[#E8A3B3]" : "w-1.5 bg-zinc-300"}`} />
        <span className={`h-1.5 rounded-full transition-all duration-300 ${currentSlide === 1 ? "w-4 bg-[#E8A3B3]" : "w-1.5 bg-zinc-300"}`} />
      </div>
    </div>
  );
}