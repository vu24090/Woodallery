export interface ProductImage {
  url: string;
  order: number;
}

export interface Product {
  productId: string;
  category: string;
  name: string;
  slug: string;
  description: string;
  material: string;
  color: string;
  dimensions: string;
  createAt: string;
  imageUrl: string | null;
}

export interface ProductRequest {
  category: string;
  name: string;
  slug: string;
  description: string;
  material: string;
  color: string;
  dimensions: string;
  imageUrl?: string | null;
}
