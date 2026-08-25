interface EmptyStateProps {
  title?: string;
  description?: string;
}

export default function EmptyState({
  title = "Không tìm thấy sản phẩm",
  description = "Hãy thử thay đổi từ khóa hoặc danh mục.",
}: EmptyStateProps) {
  return (
    <div className="empty-state">
      <h3>{title}</h3>

      <p>{description}</p>
    </div>
  );
}
