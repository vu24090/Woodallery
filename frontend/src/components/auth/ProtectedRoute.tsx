import { Navigate, Outlet } from "react-router-dom";

import { useAuth } from "../../auth/AuthContext";

export default function ProtectedRoute() {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <main>
        <p>Đang kiểm tra đăng nhập...</p>
      </main>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
}
