"use client";

import {
  createContext,
  useContext,
  useState,
  ReactNode,
  Dispatch,
  SetStateAction,
  useEffect,
} from "react";

import api from "@/lib/api";

export type ViewType =
  | "HOME"
  | "CATEGORIES"
  | "ANATOMY"
  | "ACCOUNT";

export interface Product {
  id: number;
  name: string;
  description?: string;
  material?: string;
  category?: string;
  price: number;
  imageUrl?: string;
  stockQuantity?: number;
  minimumStock?: number;
}

export interface CartItem {
  itemId: number;
  productId: number;
  productName: string;
  imageUrl?: string;
  price: number;
  quantity: number;
  subtotal?: number;
}

interface AppContextType {
  activeView: ViewType;
  setActiveView: Dispatch<SetStateAction<ViewType>>;

  selectedMaterial: string;
  setSelectedMaterial: Dispatch<SetStateAction<string>>;

  selectedCategory: string;
  setSelectedCategory: Dispatch<SetStateAction<string>>;

  cart: CartItem[];

  addToCart: (product: Product) => Promise<void>;
  removeFromCart: (itemId: number) => Promise<void>;

  isMenuOpen: boolean;
  setIsMenuOpen: Dispatch<SetStateAction<boolean>>;

  isCartOpen: boolean;
  setIsCartOpen: Dispatch<SetStateAction<boolean>>;

  login: (email: string, password: string) => Promise<void>;
  register: (payload: { firstName: string; lastName: string; email: string; password: string; cpf: string }) => Promise<void>;
  logout: () => void;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export function AppProvider({ children }: { children: ReactNode }) {
  const [activeView, setActiveView] = useState<ViewType>("HOME");

  const [selectedMaterial, setSelectedMaterial] = useState("Todos");
  const [selectedCategory, setSelectedCategory] = useState("Todos");

  const [cart, setCart] = useState<CartItem[]>([]);

  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isCartOpen, setIsCartOpen] = useState(false);

  async function refreshCart() {
    try {
      const data = await api.getCart();
      // Map backend shape to frontend CartItem[]
      const items: CartItem[] = (data.items || []).map((it: any) => ({
        itemId: it.itemId,
        productId: it.productId,
        productName: it.productName,
        imageUrl: it.imageUrl,
        price: Number(it.price),
        quantity: it.quantity,
        subtotal: it.subtotal ? Number(it.subtotal) : undefined,
      }));

      setCart(items);
    } catch (e) {
      setCart([]);
    }
  }

  useEffect(() => {
    // On mount, if token exists, try to refresh cart
    try {
      if (typeof window !== "undefined") {
        const token = localStorage.getItem("jwt_token");
        if (token) {
          refreshCart();
        }
      }
    } catch (e) {}
  }, []);

  const addToCart = async (product: Product) => {
    try {
      await api.addCartItem(product.id, 1);
      await refreshCart();
    } catch (e) {
      console.error("Erro ao adicionar ao carrinho", e);
      throw e;
    }
  };

  const removeFromCart = async (itemId: number) => {
    try {
      await api.removeCartItem(itemId);
      await refreshCart();
    } catch (e) {
      console.error("Erro ao remover item do carrinho", e);
      throw e;
    }
  };

  const login = async (email: string, password: string) => {
    await api.login(email, password);
    await refreshCart();
  };

  const register = async (payload: { firstName: string; lastName: string; email: string; password: string; cpf: string }) => {
    await api.register(payload);
  };

  const logout = () => {
    api.logout();
    setCart([]);
  };

  return (
    <AppContext.Provider
      value={{
        activeView,
        setActiveView,

        selectedMaterial,
        setSelectedMaterial,

        selectedCategory,
        setSelectedCategory,

        cart,

        addToCart,
        removeFromCart,

        isMenuOpen,
        setIsMenuOpen,

        isCartOpen,
        setIsCartOpen,

        login,
        register,
        logout,
      }}
    >
      {children}
    </AppContext.Provider>
  );
}

export const useApp = () => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error("useApp must be used within AppProvider");
  }
  return context;
};