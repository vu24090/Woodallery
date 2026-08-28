import { useParams } from "react-router-dom";

import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";

import ProductGallery from "../components/product/ProductGallery";
import ProductInfo from "../components/product/ProductInfo";

import ProductGridSkeleton from "../components/common/ProductGridSkeleton";

import EmptyState from "../components/common/EmptyState";

import { useProduct } from "../hooks/useProduct";

export default function ProductDetailPage() {
  const { id } = useParams();

  const { product, loading, error } = useProduct(id);

  return (
    <>
      <Header />

      <main>
        {loading ? (
          <section className="product-detail">
            <ProductGridSkeleton />
          </section>
        ) : error || !product ? (
          <EmptyState
            title="Không tìm thấy sản phẩm"
            description={error ?? "Sản phẩm không tồn tại."}
          />
        ) : (
          <section className="product-detail">
            <div className="product-detail-container">
              <ProductGallery
                imageUrl={product.imageUrl}
                productName={product.name}
              />

              <ProductInfo product={product} />
            </div>
          </section>
        )}
      </main>

      <Footer />
    </>
  );
}
