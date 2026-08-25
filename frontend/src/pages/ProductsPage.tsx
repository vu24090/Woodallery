import { useProducts } from "../hooks/useProducts";
import { useSearchParams } from "react-router-dom";
import { useDebounce } from "../hooks/useDebounce";
import { IoArrowBack } from "react-icons/io5";
import Header from "../components/layout/Header";
import Footer from "../components/layout/Footer";

import SearchInput from "../components/common/SearchInput";
import EmptyState from "../components/common/EmptyState";

import CategoryFilter from "../components/category/CategoryFilter";
import ProductGrid from "../components/product/ProductGrid";
import ProductGridSkeleton from "../components/common/ProductGridSkeleton";

export default function ProductsPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  const keyword = searchParams.get("keyword") ?? "";
  const category = searchParams.get("category") ?? "all";

  const debouncedKeyword = useDebounce(keyword, 400);

  const handleKeywordChange = (value: string) => {
    const params = new URLSearchParams(searchParams);

    if (value.trim()) {
      params.set("keyword", value);
    } else {
      params.delete("keyword");
    }

    setSearchParams(params);
  };

  const handleCategoryChange = (value: string) => {
    const params = new URLSearchParams(searchParams);

    if (value === "all") {
      params.delete("category");
    } else {
      params.set("category", value);
    }

    setSearchParams(params);
  };

  const { products, isLoading, error } = useProducts({
    category: category === "all" ? undefined : category,
    keyword: debouncedKeyword || undefined,
  });

  return (
    <>
      <Header />

      <main>
        <section className="products-page">
          <div className="products-page-container">
            <div className="products-page-heading">
              <div className="admin-back-wrapper">
                <button
                  className="back-icon-button"
                  onClick={() => window.history.back()}
                  aria-label="Back"
                >
                  <IoArrowBack size={26} />
                </button>
              </div>
              <span>SẢN PHẨM</span>
              <h1>Khám phá bộ sưu tập</h1>
              <p>
                Tìm kiếm những sản phẩm nội thất phù hợp với không gian của bạn.
              </p>
            </div>

            <div className="products-filter">
              <SearchInput value={keyword} onChange={handleKeywordChange} />
              <CategoryFilter
                selectedCategory={category}
                onChange={handleCategoryChange}
              />
            </div>

            <div className="products-result">
              {isLoading ? (
                <ProductGridSkeleton />
              ) : error ? (
                <EmptyState
                  title="Không thể tải sản phẩm"
                  description={error}
                />
              ) : products.length === 0 ? (
                <EmptyState title="Không có sản phẩm" />
              ) : (
                <>
                  <span>{products.length} sản phẩm</span>
                  <ProductGrid products={products} />
                </>
              )}
            </div>
          </div>
        </section>
      </main>

      <Footer />
    </>
  );
}
