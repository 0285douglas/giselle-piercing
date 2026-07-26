"use client";

import { useState } from "react";
import { useApp } from "@/context/AppContext";

export default function LoginView() {
  const { login, setActiveView } = useApp();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(null);
    setLoading(true);

    try {
      await login(email.trim(), password);
      setActiveView("HOME");
    } catch (err: any) {
      setError(err?.message || "Erro ao autenticar. Verifique suas credenciais.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-white p-8 flex flex-col justify-center items-center animate-in fade-in duration-500">
      <div className="w-full max-w-[300px] text-center">
        <h1 className="text-2xl font-serif uppercase tracking-widest text-[#1A1A1A] mb-2">Giselle Piercing</h1>
        <p className="text-[10px] text-zinc-400 uppercase tracking-[0.2em] mb-12">Sua conta exclusiva</p>
        
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <input
              type="email"
              placeholder="E-MAIL"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              className="w-full border-b border-zinc-200 py-3 text-xs outline-none focus:border-[#E8A3B3] transition-colors"
              required
            />
          </div>

          <div>
            <input
              type="password"
              placeholder="SENHA"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              className="w-full border-b border-zinc-200 py-3 text-xs outline-none focus:border-[#E8A3B3] transition-colors"
              required
            />
          </div>

          {error && (
            <p className="text-red-500 text-xs text-left">{error}</p>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-[#1A1A1A] text-white py-4 rounded-full text-xs font-bold uppercase tracking-widest mt-4 shadow-xl hover:bg-[#E8A3B3] transition-all disabled:opacity-50"
          >
            {loading ? "Entrando..." : "Entrar"}
          </button>

          <div className="flex justify-between mt-6 px-2">
            <button type="button" className="text-[10px] text-zinc-400 font-bold uppercase">
              Criar Conta
            </button>
            <button type="button" className="text-[10px] text-zinc-400 font-bold uppercase">
              Esqueci a Senha
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}