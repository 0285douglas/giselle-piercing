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
    <main className="min-h-screen bg-[#FDFBFB] max-w-md mx-auto shadow-xl border-x border-zinc-100 relative">
      
      {activeView !== 'ACCOUNT' && <Header />}

      {activeView === 'HOME' && (
        <div className="animate-in fade-in duration-500 pt-16 pb-24 overflow-x-hidden">
          <Hero />
          <Categories />
          <ProductGrid />
          <Features />
        </div>
      )}

      {activeView === 'CATEGORIES' && (
        <div className="pt-16 pb-24 overflow-x-hidden">
          <CategoryView />
        </div>
      )}

      {activeView === 'ANATOMY' && (
        <div className="pt-16 pb-24 overflow-x-hidden">
          <AnatomyView />
        </div>
      )}

      {activeView === 'ACCOUNT' && (
        <div className="pb-24">
          <LoginView />
        </div>
      )}

      <BottomNav />
      <SidebarMenu />
      <CartDrawer />
    </main>
  );
}