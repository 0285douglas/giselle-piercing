"use client";
import { useApp } from "@/context/AppContext";
import Header from "@/components/Header";
import Hero from "@/components/Hero";
import Categories from "@/components/Categories";
import ProductGrid from "@/components/ProductGrid";
import Features from "@/components/Features";
import BottomNav from "@/components/BottomNav";
import SidebarMenu from "@/components/SidebarMenu";
import CartDrawer from "@/components/CartDrawer";
import AnatomyView from "@/components/AnatomyView";
import CategoryView from "@/components/CategoryView";
import LoginView from "@/components/LoginView";

export default function Home() {
  const { activeView } = useApp();

  return (
    <main className="min-h-screen bg-[#FDFBFB] max-w-md mx-auto shadow-xl border-x border-zinc-100 relative overflow-x-hidden">
      
      {/* HEADER GLOBAL: Agora ele fica fixo no topo de todas as telas */}
      <Header />

      {/* HOME VIEW */}
      {activeView === 'HOME' && (
        <div className="animate-in fade-in duration-500 pb-24">
          <Hero />
          <Categories />
          <ProductGrid />
          <Features />
        </div>
      )}

      {/* CATEGORIES VIEW */}
      {activeView === 'CATEGORIES' && (
        <div className="pb-24">
          <CategoryView />
        </div>
      )}

      {/* ANATOMY VIEW */}
      {activeView === 'ANATOMY' && (
        <div className="pb-24">
          <AnatomyView />
        </div>
      )}

      {/* ACCOUNT VIEW */}
      {activeView === 'ACCOUNT' && (
        <div className="pb-24">
          <LoginView />
        </div>
      )}

      {/* Componentes de Navegação e Modais Globais */}
      <BottomNav />
      <SidebarMenu />
      <CartDrawer />
    </main>
  );
}