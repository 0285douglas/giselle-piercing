import Header from "@/components/Header";
import Hero from "@/components/Hero";
import Categories from "@/components/Categories";
import ProductGrid from "@/components/ProductGrid";
import Features from "@/components/Features";
import BottomNav from "@/components/BottomNav";
import SidebarMenu from "@/components/SidebarMenu";
import CartDrawer from "@/components/CartDrawer";

export default function Home() {
  return (
    // Removido o conflito de overflow e mantido apenas a centralização estilo mobile
    <main className="min-h-screen bg-[#FDFBFB] pb-24 max-w-md mx-auto shadow-xl border-x border-zinc-100">
      <Header />
      <Hero />
      <Categories />
      <ProductGrid />
      <Features />
      <BottomNav />
      <SidebarMenu />
      <CartDrawer />
    </main>
  );
}