import { useState } from "react";
import ProductTable from "../components/product/ProductTable";
import ProductForm, {
  type ProductFormData,
} from "../components/product/ProductForm";
import Modal from "../components/ui/Modal";
import { useProducts } from "../hooks/useProducts";
import type { Product } from "../types/product";
import { useDebounce } from "../hooks/useDebounce";
import { useCreateProduct } from "../hooks/useCreateProduct";
import { useUpdateProduct } from "../hooks/useUpdateProduct";
import { useDeleteProduct } from "../hooks/useDeleteProduct";
import { useNavigate } from "react-router-dom";
import { IoArrowBack } from "react-icons/io5";

export default function AdminPage() {
  const navigate = useNavigate();

  const [keyword, setKeyword] = useState("");
  const debouncedKeyword = useDebounce(keyword, 400);

  const [category, setCategory] = useState("");

  const { products, isLoading, error, reload } = useProducts({
    category,
    keyword: debouncedKeyword,
  });

  const { create, isCreating, error: createError } = useCreateProduct();
  const { update, isUpdating, error: updateError } = useUpdateProduct();
  const { deleteProduct, isDeleting, error: deleteError } = useDeleteProduct();

  const [productToDelete, setProductToDelete] = useState<Product | null>(null);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);

  const [toastMessage, setToastMessage] = useState("");

  function handleCreate() {
    setSelectedProduct(null);
    setIsFormOpen(true);
  }

  function handleEdit(product: Product) {
    setSelectedProduct(product);
    setIsFormOpen(true);
  }

  function handleDelete(product: Product) {
    setProductToDelete(product);
  }

  async function handleConfirmDelete() {
    if (!productToDelete) return;

    try {
      await deleteProduct(productToDelete.productId);

      setToastMessage("Xoá sản phẩm thành công");
      setTimeout(() => setToastMessage(""), 3000);

      setProductToDelete(null);
      await reload();
    } catch {
      // giữ modal mở nếu lỗi
    }
  }

  function handleCloseForm() {
    setIsFormOpen(false);
    setSelectedProduct(null);
  }

  async function handleSubmit(data: ProductFormData) {
    try {
      if (selectedProduct) {
        await update(selectedProduct.productId, data);
        setToastMessage("Sửa thành công");
      } else {
        await create(data);
        setToastMessage("Tạo sản phẩm thành công");
      }

      handleCloseForm();
      await reload();

      setTimeout(() => setToastMessage(""), 3000);
    } catch (error) {
      console.error("Create failed:", error);
    }
  }

  return (
    <main className="admin-page">
      <section className="admin-container">
        <div className="admin-back-wrapper">
          <button
            className="back-icon-button"
            onClick={() => navigate("/")}
            aria-label="Back to home"
          >
            <IoArrowBack size={26} />
          </button>
        </div>

        <header className="admin-page-header">
          <div>
            <p className="admin-eyebrow">ADMIN</p>
            <h1>Product Management</h1>
            <p>Manage products in your showroom.</p>
          </div>

          <button
            className="primary-button"
            type="button"
            onClick={handleCreate}
          >
            + Add Product
          </button>
        </header>

        <section className="admin-toolbar">
          <input
            type="search"
            placeholder="Search product..."
            value={keyword}
            onChange={(event) => setKeyword(event.target.value)}
          />

          <select
            value={category}
            onChange={(event) => setCategory(event.target.value)}
          >
            <option value="">All categories</option>
            <option value="table">Table</option>
            <option value="chair">Chair</option>
            <option value="bed">Bed</option>
            <option value="closet">Closet</option>
          </select>
        </section>

        {isLoading && <div className="loading-state">Loading products...</div>}

        {error && (
          <div className="error-state">
            <p>{error}</p>
            <button type="button" onClick={reload}>
              Try again
            </button>
          </div>
        )}

        {!isLoading && !error && (
          <ProductTable
            products={products}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        )}
      </section>

      {/* FORM MODAL */}
      <Modal
        open={isFormOpen}
        title={selectedProduct ? "Edit Product" : "Create Product"}
        onClose={handleCloseForm}
      >
        <ProductForm
          initialProduct={selectedProduct}
          isSubmitting={isCreating || isUpdating}
          error={createError || updateError}
          onSubmit={handleSubmit}
          onCancel={handleCloseForm}
        />
      </Modal>

      {/* DELETE CONFIRM MODAL */}
      <Modal
        open={productToDelete !== null}
        title="Delete Product"
        onClose={() => setProductToDelete(null)}
      >
        <div className="delete-confirmation">
          <p>Are you sure you want to delete this product?</p>

          <strong>{productToDelete?.name}</strong>

          {deleteError && <div className="form-error">{deleteError}</div>}

          <div className="form-actions">
            <button
              type="button"
              disabled={isDeleting}
              onClick={() => setProductToDelete(null)}
            >
              Cancel
            </button>

            <button
              type="button"
              className="danger-button"
              disabled={isDeleting}
              onClick={handleConfirmDelete}
            >
              {isDeleting ? "Deleting..." : "Delete"}
            </button>
          </div>
        </div>
      </Modal>

      {/* TOAST */}
      {toastMessage && <div className="toast-notification">{toastMessage}</div>}
    </main>
  );
}
