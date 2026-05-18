"use client";
import { createContext, useContext, useState, ReactNode, Dispatch, SetStateAction } from "react";

// Tipagem para suportar os filtros avançados
export type ViewType = 'HOME' | 'CATEGORIES' | 'ANATOMY' | 'ACCOUNT';

interface Product {
  id: number;
  title: string;
  material: string;
  price: number;
  image: string;
  bodyPart: string;
}

interface AppContextType {
  activeView: ViewType;
  setActiveView: Dispatch<SetStateAction<ViewType>>;
  selectedMaterial: string;
  setSelectedMaterial: Dispatch<SetStateAction<string>>;
  selectedBodyPart: string;
  setSelectedBodyPart: Dispatch<SetStateAction<string>>;
  cart: any[];
  addToCart: (p: any) => void;
  isMenuOpen: boolean;
  setIsMenuOpen: Dispatch<SetStateAction<boolean>>;
  isCartOpen: boolean;
  setIsCartOpen: Dispatch<SetStateAction<boolean>>;
  selectedCategory: string;                 // Prometido na interface
  setSelectedCategory: Dispatch<SetStateAction<string>>; // Ajustado para seguir o padrão Dispatch
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export function AppProvider({ children }: { children: ReactNode }) {
  const [activeView, setActiveView] = useState<ViewType>('HOME');
  const [selectedMaterial, setSelectedMaterial] = useState("Todos");
  const [selectedBodyPart, setSelectedBodyPart] = useState("Todos");
  const [cart, setCart] = useState<any[]>([]);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isCartOpen, setIsCartOpen] = useState(false);
  
  // CORREÇÃO: Criando os estados que estavam faltando no Provider
  const [selectedCategory, setSelectedCategory] = useState("Todos");

  const addToCart = (product: any) => {
    setCart(prev => [...prev, { ...product, quantity: 1 }]);
  };

  return (
    <AppContext.Provider value={{ 
      activeView, setActiveView, 
      selectedMaterial, setSelectedMaterial,
      selectedBodyPart, setSelectedBodyPart, 
      cart, addToCart,
      isMenuOpen, setIsMenuOpen, 
      isCartOpen, setIsCartOpen, 
      selectedCategory,    // CORREÇÃO: Passando o estado pro contexto
      setSelectedCategory  // CORREÇÃO: Passando a função de alteração pro contexto
    }}>
      {children}
    </AppContext.Provider>
  );
}

export const useApp = () => {
  const context = useContext(AppContext);
  if (!context) throw new Error("useApp must be used within AppProvider");
  return context;
};