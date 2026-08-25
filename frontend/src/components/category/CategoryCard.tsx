import type { Category } from "../../data/categories";

interface CategoryCardProps {
  category: Category;
}

export default function CategoryCard({ category }: CategoryCardProps) {
  return (
    <article className="category-card">
      <div className="category-card-image">
        <img src={category.imageUrl} alt={category.name} />
      </div>

      <div className="category-card-overlay">
        <span>{category.name}</span>

        <p>{category.description}</p>
      </div>
    </article>
  );
}
