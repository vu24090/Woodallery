import { useState } from "react";

import { createProduct } from "../services/productService";

import { ApiError } from "../services/apiError";

import type { Product, ProductRequest } from "../types/product";

export function useCreateProduct() {
  const [isCreating, setIsCreating] = useState(false);

  const [error, setError] = useState<string | null>(null);

  async function execute(data: ProductRequest): Promise<Product> {
    try {
      setIsCreating(true);
      setError(null);

      const product = await createProduct(data);

      return product;
    } catch (error) {
      if (error instanceof ApiError) {
        if (error.status === 401) {
          setError("Phiên đăng nhập không hợp lệ.");
        } else if (error.status === 403) {
          setError("Bạn không có quyền tạo sản phẩm.");
        } else if (error.status === 400) {
          setError("Dữ liệu sản phẩm không hợp lệ.");
        } else {
          setError("Không thể tạo sản phẩm.");
        }
      } else {
        setError("Có lỗi xảy ra.");
      }

      throw error;
    } finally {
      setIsCreating(false);
    }
  }

  return {
    create: execute,
    isCreating,
    error,
  };
}
