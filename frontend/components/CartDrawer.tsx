"use client";

import { useApp } from "@/context/AppContext";
import api from "@/lib/api";
import { X, Trash2, ShoppingBag } from "lucide-react";

export default function CartDrawer() {
  const { cart, isCartOpen, setIsCartOpen, removeFromCart } = useApp();

  if (!isCartOpen) {
    return null;
  }

  const total = cart.reduce(
    (accumulator, item) => accumulator + Number(item.price) * item.quantity,
    0
  );

  const handleCheckout = async () => {
    try {
      const token =
        typeof window !== "undefined"
          ? localStorage.getItem("jwt_token")
          : null;

      if (!token) {
        alert("Faça login para finalizar o pedido.");
        return;
      }

      // Envia pedido ao backend usando PIX como método padrão
      const payment = await api.checkout({ paymentMethod: "PIX" });

      console.log("Checkout response:", payment);
      alert("Pedido gerado com sucesso! Verifique o console para os detalhes.");
      setIsCartOpen(false);
    } catch (e: any) {
      console.error("Erro no checkout", e);
      alert(e?.body?.message || "Erro ao finalizar pedido.");
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex justify-end bg-black/40 backdrop-blur-xs animate-in fade-in">
      {/* Overlay para fechar ao clicar fora */}
      <div
        className="absolute inset-0"
        onClick={() => setIsCartOpen(false)}
      />

      {/* Painel do Drawer */}
      <div className="relative w-full max-w-md bg-white h-full shadow-xl flex flex-col p-6 z-10 animate-in slide-in-from-right">
        {/* Cabeçalho */}
        <div className="flex items-center justify-between border-b border-zinc-100 pb-4 mb-4">
          <div className="flex items-center gap-2">
            <ShoppingBag size={18} className="text-[#E8A3B3]" />
            <h3 className="font-serif uppercase tracking-wider text-sm text-[#1A1A1A]">
              Seu Carrinho
            </h3>
          </div>
          <button
            onClick={() => setIsCartOpen(false)}
            className="p-1 text-zinc-400 hover:text-zinc-600 rounded-full"
          >
            <X size={18} />
          </button>
        </div>

        {/* Lista de Itens */}
        <div className="flex-1 overflow-y-auto space-y-4">
          {cart.length === 0 ? (
            <div className="flex flex-col items-center justify-center h-full text-zinc-400 gap-2">
              <ShoppingBag size={32} strokeWidth={1.5} />
              <p className="text-xs">Seu carrinho está vazio.</p>
            </div>
          ) : (
            cart.map((item: any) => {
              // Suporta tanto o formato mapeado do backend quanto do estado local
              const id = item.itemId || item.id;
              const name = item.productName || item.name;
              const image = item.imageUrl;

              return (
                <div
                  key={id}
                  className="flex gap-3 border-b border-zinc-100 pb-3 items-center"
                >
                  <img
                    src={image}
                    alt={name}
                    className="w-14 h-14 object-cover rounded-md bg-zinc-50"
                  />

                  <div className="flex-1 min-w-0">
                    <h4 className="text-xs font-serif text-[#1A1A1A] truncate">
                      {name}
                    </h4>

                    {item.material && (
                      <p className="text-[10px] text-zinc-400">
                        {item.material}
                      </p>
                    )}

                    <p className="text-xs font-bold text-[#1A1A1A] mt-1">
                      {item.quantity}x{" "}
                      {Number(item.price).toLocaleString("pt-BR", {
                        style: "currency",
                        currency: "BRL",
                      })}
                    </p>
                  </div>

                  <button
                    onClick={() => removeFromCart(id)}
                    className="text-zinc-400 hover:text-red-500 p-1"
                  >
                    <Trash2 size={14} />
                  </button>
                </div>
              );
            })
          )}
        </div>

        {/* Rodapé / Total e Checkout */}
        {cart.length > 0 && (
          <div className="border-t border-zinc-100 pt-4 mt-4">
            <div className="flex justify-between font-medium text-sm text-[#1A1A1A] mb-4">
              <span>Total:</span>
              <span className="font-bold">
                {total.toLocaleString("pt-BR", {
                  style: "currency",
                  currency: "BRL",
                })}
              </span>
            </div>

            <button
              onClick={handleCheckout}
              className="w-full bg-[#E8A3B3] text-white text-xs uppercase tracking-widest py-3 rounded-full font-semibold hover:opacity-95 transition-opacity"
            >
              Finalizar Pedido
            </button>
          </div>
        )}
      </div>
    </div>
  );
}