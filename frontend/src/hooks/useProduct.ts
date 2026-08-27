import { useEffect, useState } from "react";
import { getProductById } from "../services/productService";
import type { Product } from "../types/product";
import { getApiErrorMessage } from "../utils/apiError";

export function useProduct(id?: string) {
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    (async () => {
      if (!id) {
        if (!cancelled) {
          setError("Product ID không hợp lệ");
          setLoading(false);
        }
        return;
      }

      try {
        setLoading(true);
        setError(null);

        const data = await getProductById(id);

        if (!cancelled) {
          setProduct(data);
        }
      } catch (err) {
        if (!cancelled) {
          setError(getApiErrorMessage(err));
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    })();

    return () => {
      cancelled = true;
    };
  }, [id]);

  return { product, loading, error };
}
