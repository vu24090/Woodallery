import { Link } from "react-router-dom";

export default function NotFoundPage() {
  return (
    <main className="not-found">
      <span>404</span>

      <h1>Không tìm thấy trang</h1>

      <p>Trang bạn đang tìm kiếm không tồn tại.</p>

      <Link to="/">Quay về trang chủ</Link>
    </main>
  );
}
