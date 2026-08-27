import { Navigate, Outlet } from "react-router-dom";

import { useAuth } from "./AuthContext";

export default function AdminRoute() {
  const { isAuthenticated, isLoading, isAdmin } = useAuth();

  if (isLoading) {
    return (
      <main>
        <p>Đang kiểm tra quyền truy cập...</p>
      </main>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  if (!isAdmin) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
}
