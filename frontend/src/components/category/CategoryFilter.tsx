interface CategoryFilterProps {
  selectedCategory: string;
  onChange: (category: string) => void;
}

const categoryOptions = [
  {
    label: "Tất cả",
    value: "all",
  },
  {
    label: "Bàn",
    value: "table",
  },
  {
    label: "Ghế",
    value: "chair",
  },
  {
    label: "Giường",
    value: "bed",
  },
  {
    label: "Tủ",
    value: "cabinet",
  },
];

export default function CategoryFilter({
  selectedCategory,
  onChange,
}: CategoryFilterProps) {
  return (
    <div className="category-filter">
      {categoryOptions.map((category) => {
        const isActive = selectedCategory === category.value;

        return (
          <button
            key={category.value}
            type="button"
            className={
              isActive
                ? "category-filter-button active"
                : "category-filter-button"
            }
            onClick={() => onChange(category.value)}
          >
            {category.label}
          </button>
        );
      })}
    </div>
  );
}
