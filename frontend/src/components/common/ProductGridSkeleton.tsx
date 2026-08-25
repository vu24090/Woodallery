export default function ProductGridSkeleton() {
  return (
    <div className="product-grid">
      {Array.from({ length: 8 }).map((_, index) => (
        <div className="product-skeleton" key={index}>
          <div className="skeleton-image" />

          <div className="skeleton-line" />

          <div className="skeleton-line short" />
        </div>
      ))}
    </div>
  );
}
