import type { Product } from "../../types/product";

interface ProductInfoProps {
  product: Product;
}

export default function ProductInfo({ product }: ProductInfoProps) {
  return (
    <div className="product-info">
      <span className="product-info-category">{product.category}</span>

      <h1>{product.name}</h1>

      <p className="product-description">{product.description}</p>

      <div className="product-specifications">
        <div>
          <span>Chất liệu</span>
          <strong>{product.material}</strong>
        </div>

        <div>
          <span>Màu sắc</span>
          <strong>{product.color || "Tự nhiên"}</strong>
        </div>

        <div>
          <span>Kích thước</span>
          <strong>{product.dimensions}</strong>
        </div>
      </div>

      <button className="contact-button">Liên hệ báo giá</button>
    </div>
  );
}
