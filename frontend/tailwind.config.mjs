/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        luxury: {
          pink: "#E8A3B3", // Cor dos botões e destaques rosa
          bg: "#FDFBFB",   // Fundo off-white sutil
          text: "#1A1A1A", // Texto escuro principal
          muted: "#71717A" // Texto cinza secundário
        }
      },
    },
  },
  plugins: [],
};