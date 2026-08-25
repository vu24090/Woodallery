import type { Product } from "../../types/product";

interface ProductTableProps {
  products: Product[];

  onEdit: (product: Product) => void;

  onDelete: (product: Product) => void;
}

export default function ProductTable({
  products,
  onEdit,
  onDelete,
}: ProductTableProps) {
  return (
    <div className="product-table-wrapper">
      <table className="product-table">
        <thead>
          <tr>
            <th>Product</th>
            <th>Category</th>
            <th>Material</th>
            <th>Color</th>
            <th>Dimensions</th>
            <th>Created</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {products.map((product) => (
            <tr key={product.productId}>
              <td>
                <div className="product-table-name">{product.name}</div>

                <div className="product-table-slug">{product.slug}</div>
              </td>

              <td>
                <span className="category-badge">{product.category}</span>
              </td>

              <td>{product.material || "-"}</td>

              <td>{product.color || "-"}</td>

              <td>{product.dimensions || "-"}</td>

              <td>{new Date(product.createAt).toLocaleDateString("vi-VN")}</td>

              <td>
                <div className="product-actions">
                  <button
                    type="button"
                    className="edit-btn"
                    onClick={() => onEdit(product)}
                  >
                    Edit
                  </button>

                  <button
                    type="button"
                    className="delete-btn"
                    onClick={() => onDelete(product)}
                  >
                    Delete
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {products.length === 0 && (
        <div className="empty-state">Không tìm thấy sản phẩm.</div>
      )}
    </div>
  );
}
