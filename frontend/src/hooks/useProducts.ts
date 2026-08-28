import { useCallback, useEffect, useState } from "react";

import { getProducts } from "../services/productService";

import type { Product } from "../types/product";

interface UseProductsParams {
  category?: string;
  keyword?: string;
}

export function useProducts({
  category = "",
  keyword = "",
}: UseProductsParams = {}) {
  const [products, setProducts] = useState<Product[]>([]);

  const [isLoading, setIsLoading] = useState(true);

  const [error, setError] = useState<string | null>(null);

  const loadProducts = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);

      const data = await getProducts({
        category: category || undefined,

        keyword: keyword || undefined,
      });

      setProducts(data);
    } catch (error) {
      console.error("Failed to load products:", error);

      setError("Không thể tải sản phẩm.");
    } finally {
      setIsLoading(false);
    }
  }, [category, keyword]);

  useEffect(() => {
    (async () => {
      await loadProducts();
    })();
  }, [loadProducts]);

  return {
    products,
    isLoading,
    error,
    reload: loadProducts,
  };
}
