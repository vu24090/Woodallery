import { useState } from "react";
import type { Product } from "../../types/product";

export interface ProductFormData {
  category: string;
  name: string;
  slug: string;
  description: string;
  material: string;
  color: string;
  dimensions: string;
  imageUrl: string;
}

interface ProductFormProps {
  initialProduct?: Product | null;
  isSubmitting?: boolean;
  error?: string | null;
  onSubmit: (data: ProductFormData) => void | Promise<void>;
  onCancel: () => void;
}

export default function ProductForm({
  initialProduct,
  isSubmitting = false,
  error = null,
  onSubmit,
  onCancel,
}: ProductFormProps) {
  const [form, setForm] = useState<ProductFormData>({
    category: initialProduct?.category ?? "",
    name: initialProduct?.name ?? "",
    slug: initialProduct?.slug ?? "",
    description: initialProduct?.description ?? "",
    material: initialProduct?.material ?? "",
    color: initialProduct?.color ?? "",
    dimensions: initialProduct?.dimensions ?? "",
    imageUrl: initialProduct?.imageUrl ?? "",
  });

  function handleChange(
    event: React.ChangeEvent<
      HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
    >
  ) {
    const { name, value } = event.target;

    setForm((previous) => {
      const updated = { ...previous, [name]: value };

      if (name === "name") {
        updated.slug = generateSlug(value);
      }

      return updated;
    });
  }

  function validateProduct(data: ProductFormData) {
    const errors: Record<string, string> = {};

    if (!data.category) errors.category = "Category is required.";
    if (!data.name.trim()) errors.name = "Product name is required.";
    if (!data.slug.trim()) errors.slug = "Slug is required.";

    return errors;
  }

  function generateSlug(name: string) {
    return name
      .toLowerCase()
      .trim()
      .replace(/[\s\/]+/g, "-")
      .replace(/[^a-z0-9-]/g, "")
      .replace(/-+/g, "-");
  }

  function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const errors = validateProduct(form);
    if (Object.keys(errors).length > 0) {
      console.log("Validation errors:", errors);
      return;
    }

    onSubmit(form);
  }

  return (
    <form className="product-form" onSubmit={handleSubmit}>
      <div className="form-group">
        <label htmlFor="category">Category</label>
        <select
          id="category"
          name="category"
          value={form.category}
          onChange={handleChange}
          required
        >
          <option value="">Select category</option>
          <option value="table">Table</option>
          <option value="chair">Chair</option>
          <option value="bed">Bed</option>
          <option value="closet">Closet</option>
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="name">Product name</label>
        <input
          id="name"
          name="name"
          value={form.name}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="slug">Slug</label>
        <input
          id="slug"
          name="slug"
          value={form.slug}
          onChange={handleChange}
          disabled
        />
      </div>

      <div className="form-group">
        <label htmlFor="description">Description</label>
        <textarea
          id="description"
          name="description"
          value={form.description}
          onChange={handleChange}
          rows={5}
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="material">Material</label>
          <input
            id="material"
            name="material"
            value={form.material}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label htmlFor="color">Color</label>
          <input
            id="color"
            name="color"
            value={form.color}
            onChange={handleChange}
          />
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="dimensions">Dimensions</label>
        <input
          id="dimensions"
          name="dimensions"
          value={form.dimensions}
          onChange={handleChange}
          placeholder="120x60x75"
        />
      </div>

      <div className="form-group">
        <label htmlFor="imageUrl">Image URL</label>
        <input
          id="imageUrl"
          name="imageUrl"
          value={form.imageUrl}
          onChange={handleChange}
        />
      </div>

      <div className="form-actions">
        <button type="button" onClick={onCancel}>
          Cancel
        </button>

        <button
          type="submit"
          className="primary-button"
          disabled={isSubmitting}
        >
          {isSubmitting
            ? "Saving..."
            : initialProduct
            ? "Update Product"
            : "Create Product"}
        </button>
      </div>
      {error && <p className="form-error">{error}</p>}
    </form>
  );
}
