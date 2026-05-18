"use client";
import { useApp } from "@/context/AppContext";
import { X, Trash2, ShoppingBag } from "lucide-react";

export default function CartDrawer() {
  const { cart, isCartOpen, setIsCartOpen, removeFromCart } = useApp();

  if (!isCartOpen) return null;

  const total = cart.reduce((acc, item) => acc + item.price * item.quantity, 0);

  return (
    <div className="fixed inset-0 z-50 flex max-w-md mx-auto justify-end">
      {/* Background escuro */}
      <div className="fixed inset-0 bg-black/40" onClick={() => setIsCartOpen(false)} />

      {/* Painel do Carrinho */}
      <div className="relative w-80 h-full bg-white shadow-xl flex flex-col p-5 animate-in slide-in-from-right duration-200">
        <div className="flex justify-between items-center mb-6 border-b pb-4">
          <div className="flex items-center gap-2 text-[#1A1A1A]">
            <ShoppingBag size={18} />
            <h2 className="font-serif text-lg tracking-wider">Sua Sacola</h2>
          </div>
          <button onClick={() => setIsCartOpen(false)} className="text-zinc-400">
            <X size={20} />
          </button>
        </div>

        {/* Lista de Itens */}
        <div className="flex-1 overflow-y-auto space-y-4 pr-1">
          {cart.length === 0 ? (
            <div className="text-center py-20 text-xs text-zinc-400">
              Sua sacola está vazia.
            </div>
          ) : (
            cart.map((item) => (
              <div key={item.id} className="flex gap-3 border-b border-zinc-100 pb-3 items-center">
                <img src={item.image} alt={item.title} className="w-14 h-14 object-cover rounded-md bg-zinc-50" />
                <div className="flex-1 min-w-0">
                  <h4 className="text-xs font-serif text-[#1A1A1A] truncate">{item.title}</h4>
                  <p className="text-[10px] text-zinc-400">{item.material}</p>
                  <p className="text-xs font-bold text-[#1A1A1A] mt-1">
                    {item.quantity}x {item.price.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
                  </p>
                </div>
                <button onClick={() => removeFromCart(item.id)} className="text-zinc-400 hover:text-red-500 p-1">
                  <Trash2 size={14} />
                </button>
              </div>
            ))
          )}
        </div>

        {/* Rodapé do Carrinho */}
        {cart.length > 0 && (
          <div className="border-t pt-4 mt-4">
            <div className="flex justify-between font-medium text-sm text-[#1A1A1A] mb-4">
              <span>Total:</span>
              <span className="font-bold">{total.toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}</span>
            </div>
            <button className="w-full bg-[#E8A3B3] text-white text-xs uppercase tracking-widest py-3 rounded-full font-semibold hover:opacity-95 transition-opacity">
              Finalizar Pedido
            </button>
          </div>
        )}
      </div>
    </div>
  );
}