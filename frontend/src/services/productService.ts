import { apiClient } from "./apiClient";
import type { Product, ProductRequest } from "../types/product";

export interface GetProductsParams {
  category?: string;
  keyword?: string;
}

export function getProducts(params: GetProductsParams = {}) {
  const queryParams = Object.fromEntries(
    Object.entries(params).filter(([_, value]) => value !== undefined)
  ) as Record<string, string>;
  return apiClient.get<Product[]>("/products", queryParams);
}

export function getProductById(productId: string) {
  return apiClient.get<Product>(`/products/${productId}`);
}

export function createProduct(product: ProductRequest) {
  return apiClient.post<Product>("/products", product);
}

export function updateProduct(productId: string, product: ProductRequest) {
  return apiClient.put<Product>(`/products/${productId}`, product);
}

export async function deleteProduct(
  productId: string
): Promise<{ message: string }> {
  return apiClient.delete(`/products/${productId}`);
}
