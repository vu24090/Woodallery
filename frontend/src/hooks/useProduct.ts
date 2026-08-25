import { useEffect, useState } from "react";

import { getProductById } from "../services/productService";

import type { Product } from "../types/product";

import { getApiErrorMessage } from "../utils/apiError";

export function useProduct(id?: string) {
  const [product, setProduct] = useState<Product | null>(null);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) {
      setProduct(null);
      setLoading(false);
      setError("Product ID không hợp lệ");
      return;
    }

    let cancelled = false;

    async function loadProduct() {
      try {
        setLoading(true);
        setError(null);

        const data = await getProductById(id);

        if (!cancelled) {
          setProduct(data);
        }
      } catch (error) {
        if (!cancelled) {
          setError(getApiErrorMessage(error));
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    loadProduct();

    return () => {
      cancelled = true;
    };
  }, [id]);

  return {
    product,
    loading,
    error,
  };
}
