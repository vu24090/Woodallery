import { useState } from "react";

import { updateProduct } from "../services/productService";

import type { Product, ProductRequest } from "../types/product";

export function useUpdateProduct() {
  const [isUpdating, setIsUpdating] = useState(false);

  const [error, setError] = useState<string | null>(null);

  async function execute(
    productId: string,
    data: ProductRequest
  ): Promise<Product> {
    try {
      setIsUpdating(true);
      setError(null);

      const product = await updateProduct(productId, data);

      return product;
    } catch (error) {
      console.error("Update product failed:", error);

      setError("Không thể cập nhật sản phẩm.");

      throw error;
    } finally {
      setIsUpdating(false);
    }
  }

  return {
    update: execute,
    isUpdating,
    error,
  };
}
