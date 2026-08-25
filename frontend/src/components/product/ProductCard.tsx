import { Link } from "react-router-dom";
import type { Product } from "../../types/product";

interface ProductCardProps {
  product: Product;
}

export default function ProductCard({ product }: ProductCardProps) {
  return (
    <Link to={`/products/${product.productId}`} className="product-card">
      <div className="product-card-image">
        {product.imageUrl ? (
          <img src={product.imageUrl} alt={product.name} />
        ) : (
          <div className="product-card-placeholder">No image</div>
        )}
      </div>

      <div className="product-card-content">
        <span className="product-card-category">{product.category}</span>

        <h3>{product.name}</h3>

        <p>{product.description}</p>

        <div className="product-card-meta">
          <span>{product.material}</span>
          <span>{product.dimensions}</span>
        </div>
      </div>
    </Link>
  );
}
