"use client";

import {
  createContext,
  useContext,
  useState,
  ReactNode,
  Dispatch,
  SetStateAction,
} from "react";

export type ViewType =
  | "HOME"
  | "CATEGORIES"
  | "ANATOMY"
  | "ACCOUNT";

export interface Product {
  id: number;
  name: string;
  description: string;
  material: string;
  category: string;
  price: number;
  imageUrl: string;
}

interface CartItem extends Product {
  quantity: number;
}

interface AppContextType {

  activeView: ViewType;

  setActiveView:
    Dispatch<SetStateAction<ViewType>>;

  selectedMaterial: string;

  setSelectedMaterial:
    Dispatch<SetStateAction<string>>;

  selectedCategory: string;

  setSelectedCategory:
    Dispatch<SetStateAction<string>>;

  cart: CartItem[];

  addToCart: (product: Product) => void;

  removeFromCart: (id: number) => void;

  isMenuOpen: boolean;

  setIsMenuOpen:
    Dispatch<SetStateAction<boolean>>;

  isCartOpen: boolean;

  setIsCartOpen:
    Dispatch<SetStateAction<boolean>>;
}

const AppContext =
  createContext<AppContextType | undefined>(
    undefined
  );

export function AppProvider({
  children,
}: {
  children: ReactNode;
}) {

  const [activeView, setActiveView] =
    useState<ViewType>("HOME");

  const [selectedMaterial, setSelectedMaterial] =
    useState("Todos");

  const [selectedCategory, setSelectedCategory] =
    useState("Todos");

  const [cart, setCart] =
    useState<CartItem[]>([]);

  const [isMenuOpen, setIsMenuOpen] =
    useState(false);

  const [isCartOpen, setIsCartOpen] =
    useState(false);

  const addToCart = (product: Product) => {

    setCart((previousCart) => {

      const existingItem =
        previousCart.find(
          (item) => item.id === product.id
        );

      if (existingItem) {

        return previousCart.map((item) =>

          item.id === product.id
            ? {
                ...item,
                quantity: item.quantity + 1,
              }
            : item
        );
      }

      return [
        ...previousCart,
        {
          ...product,
          quantity: 1,
        },
      ];
    });
  };

  const removeFromCart = (id: number) => {

    setCart((previousCart) =>
      previousCart.filter(
        (item) => item.id !== id
      )
    );
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
      }}
    >

      {children}

    </AppContext.Provider>
  );
}

export const useApp = () => {

  const context = useContext(AppContext);

  if (!context) {

    throw new Error(
      "useApp must be used within AppProvider"
    );
  }

  return context;
};