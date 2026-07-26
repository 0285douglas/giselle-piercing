const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

const getToken = (): string | null => {
  if (typeof window === "undefined") return null;
  try {
    return localStorage.getItem("jwt_token");
  } catch {
    return null;
  }
};

async function request(path: string, options: RequestInit = {}) {
  const headers: Record<string, string> = {
    "Accept": "application/json",
    "Content-Type": "application/json",
    ...(options.headers as Record<string, string>),
  };

  const token = getToken();
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const res = await fetch(`${API_URL}${path}`, {
    ...options,
    headers,
  });

  const text = await res.text();
  const contentType = res.headers.get("content-type") || "";

  let body: any = text;
  if (contentType.includes("application/json") && text) {
    try {
      body = JSON.parse(text);
    } catch {
      body = text;
    }
  }

  if (!res.ok) {
    const message = body?.message || body?.error || res.statusText || "Erro na requisição";
    const error: any = new Error(message);
    error.status = res.status;
    error.body = body;
    throw error;
  }

  if (contentType.includes("application/json")) {
    return body;
  }

  return body;
}

export interface LoginResponse {
  token: string;
}

export interface RegisterPayload {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  cpf: string;
}

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  category: string;
  material: string;
  stockQuantity: number;
  minimumStock: number;
}

export interface CartItem {
  itemId: number;
  productId: number;
  productName: string;
  imageUrl: string;
  price: number;
  quantity: number;
  subtotal: number;
}

export interface CartResponse {
  cartId: number;
  userId: number;
  items: CartItem[];
  total: number;
}

export interface CheckoutPayload {
  paymentMethod: "PIX" | "CREDIT_CARD" | "BOLETO";
  token?: string;
  installments?: number;
  paymentMethodId?: string;
}

export async function login(email: string, password: string): Promise<LoginResponse> {
  const data = await request("/api/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });

  if (data?.token) {
    try {
      localStorage.setItem("jwt_token", data.token);
    } catch {}
  }

  return data;
}

export async function register(payload: RegisterPayload) {
  return request("/api/auth/register", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function getProducts(params?: Record<string, string | number | boolean>) {
  const search = params
    ? "?" + new URLSearchParams(Object.entries(params).map(([k, v]) => [k, String(v)])).toString()
    : "";

  return request(`/api/products${search}`) as Promise<Product[]>;
}

export async function getProductById(id: number | string) {
  return request(`/api/products/${id}`) as Promise<Product>;
}

export async function getCart() {
  return request(`/api/cart`) as Promise<CartResponse>;
}

export async function addCartItem(productId: number | string, quantity = 1) {
  return request(`/api/cart/items?productId=${productId}&quantity=${quantity}`, {
    method: "POST",
  });
}

export async function removeCartItem(itemId: number | string) {
  return request(`/api/cart/items/${itemId}`, {
    method: "DELETE",
  });
}

export async function checkout(payload: CheckoutPayload) {
  return request(`/api/checkout`, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function logout() {
  try {
    localStorage.removeItem("jwt_token");
  } catch {}
}

export default {
  login,
  register,
  getProducts,
  getProductById,
  getCart,
  addCartItem,
  removeCartItem,
  checkout,
  logout,
};
