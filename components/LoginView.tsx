"use client";
export default function LoginView() {
  return (
    <div className="min-h-screen bg-white p-8 flex flex-col justify-center items-center animate-in fade-in duration-500">
      <div className="w-full max-w-[300px] text-center">
        <h1 className="text-2xl font-serif uppercase tracking-widest text-[#1A1A1A] mb-2">Giselle Piercing</h1>
        <p className="text-[10px] text-zinc-400 uppercase tracking-[0.2em] mb-12">Sua conta exclusiva</p>
        
        <div className="space-y-4">
          <input type="email" placeholder="E-MAIL" className="w-full border-b border-zinc-200 py-3 text-xs outline-none focus:border-[#E8A3B3] transition-colors" />
          <input type="password" placeholder="SENHA" className="w-full border-b border-zinc-200 py-3 text-xs outline-none focus:border-[#E8A3B3] transition-colors" />
          
          <button className="w-full bg-[#1A1A1A] text-white py-4 rounded-full text-xs font-bold uppercase tracking-widest mt-8 shadow-xl hover:bg-[#E8A3B3] transition-all">
            Entrar
          </button>
          
          <div className="flex justify-between mt-6 px-2">
            <button className="text-[10px] text-zinc-400 font-bold uppercase">Criar Conta</button>
            <button className="text-[10px] text-zinc-400 font-bold uppercase">Esqueci a Senha</button>
          </div>
        </div>
      </div>
    </div>
  );
}