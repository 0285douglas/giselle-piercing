"use client";
import { ArrowRight } from "lucide-react";
import { useRef, useState, MouseEvent } from "react";

export default function Hero() {
  const scrollRef = useRef<HTMLDivElement>(null);
  const [currentSlide, setCurrentSlide] = useState(0);

  const [isDown, setIsDown] = useState(false);
  const [startX, setStartX] = useState(0);
  const [scrollLeft, setScrollLeft] = useState(0);

  // Array de slides para facilitar a manutenção e o teste
  const slides = [
    {
      id: 1,
      tag: "Nova Coleção",
      title: "Ouro & Titânio Premium",
      desc: "Designs exclusivos para elevar sua essência.",
      img: "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=800&q=80",
      btn: "Descubra Agora"
    },
    {
      id: 2,
      tag: "Coleção Minimal",
      title: "Linhas Finas & Aço",
      desc: "A beleza no que é essencial e discreto.",
      img: "https://images.unsplash.com/photo-1601121141461-9d6647bca1ed?w=800&q=80",
      btn: "Ver Modelos"
    },
    {
      id: 3,
      tag: "Segurança & Arte",
      title: "Joias Hipoalergênicas",
      desc: "Materiais certificados para uma cicatrização perfeita.",
      img: "https://images.unsplash.com/photo-1596944214906-db7637218328?w=800&q=80",
      btn: "Saiba Mais"
    }
  ];

  const handleScrollEnd = () => {
    if (scrollRef.current) {
      const width = scrollRef.current.offsetWidth;
      const index = Math.round(scrollRef.current.scrollLeft / width);
      if (index !== currentSlide) {
        setCurrentSlide(index);
      }
    }
  };

  const handleMouseDown = (e: MouseEvent<HTMLDivElement>) => {
    if (!scrollRef.current) return;
    setIsDown(true);
    setStartX(e.pageX - scrollRef.current.offsetLeft);
    setScrollLeft(scrollRef.current.scrollLeft);
  };

  const handleMouseLeave = () => setIsDown(false);
  const handleMouseUp = () => setIsDown(false);

  const handleMouseMove = (e: MouseEvent<HTMLDivElement>) => {
    if (!isDown || !scrollRef.current) return;
    e.preventDefault();
    const x = e.pageX - scrollRef.current.offsetLeft;
    const walk = (x - startX) * 2;
    scrollRef.current.scrollLeft = scrollLeft - walk;
  };

  return (
    <div className="w-full relative overflow-hidden touch-pan-x">
      <div 
        ref={scrollRef}
        onScroll={handleScrollEnd}
        onMouseDown={handleMouseDown}
        onMouseLeave={handleMouseLeave}
        onMouseUp={handleMouseUp}
        onMouseMove={handleMouseMove}
        className="w-full flex overflow-x-auto snap-x snap-mandatory scrollbar-none pointer-events-auto relative z-10 select-none cursor-grab active:cursor-grabbing scroll-smooth"
        style={{ WebkitOverflowScrolling: "touch", scrollBehavior: "smooth" }}
      >
        {slides.map((slide) => (
          <div 
            key={slide.id}
            className="min-w-full h-[380px] bg-cover bg-center flex items-center px-6 snap-start flex-shrink-0 relative"
            style={{ backgroundImage: `url('${slide.img}')` }}
          >
            {/* OVERLAY PARA LEITURA: Um degradê escuro da esquerda para a direita */}
            <div className="absolute inset-0 bg-gradient-to-r from-black/70 via-black/30 to-transparent z-0" />

            <div className="max-w-[260px] relative z-10">
              <span className="text-[#E8A3B3] text-[10px] font-bold uppercase tracking-[0.2em]">
                {slide.tag}
              </span>
              {/* Texto em Branco para contraste máximo sobre o overlay */}
              <h2 className="text-3xl font-serif text-white mt-2 leading-tight drop-shadow-sm">
                {slide.title}
              </h2>
              <p className="text-xs text-zinc-200 mt-3 leading-relaxed">
                {slide.desc}
              </p>
              <button className="mt-6 bg-[#E8A3B3] text-white text-[10px] font-bold px-6 py-3 rounded-full flex items-center gap-2 uppercase tracking-[0.1em] shadow-lg active:scale-95 transition-transform">
                {slide.btn} <ArrowRight size={14} />
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Indicadores Dinâmicos (ajustado para 3 slides) */}
      <div className="absolute bottom-6 left-1/2 -translate-x-1/2 flex gap-2 z-20 pointer-events-none">
        {slides.map((_, idx) => (
          <span 
            key={idx}
            className={`h-1.5 rounded-full transition-all duration-500 ${
              currentSlide === idx ? "w-6 bg-[#E8A3B3]" : "w-1.5 bg-white/40"
            }`} 
          />
        ))}
      </div>
    </div>
  );
}