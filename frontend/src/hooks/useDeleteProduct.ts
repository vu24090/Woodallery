import { useState } from "react";

import { deleteProduct } from "../services/productService";

export function useDeleteProduct() {
  const [isDeleting, setIsDeleting] = useState(false);

  const [error, setError] = useState<string | null>(null);

  async function execute(productId: string) {
    try {
      setIsDeleting(true);
      setError(null);

      await deleteProduct(productId);
    } catch (error) {
      console.error("Delete product failed:", error);

      setError("Không thể xóa sản phẩm.");

      throw error;
    } finally {
      setIsDeleting(false);
    }
  }

  return {
    deleteProduct: execute,
    isDeleting,
    error,
  };
}
