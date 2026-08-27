import { useState } from "react";
import { Menu, X } from "lucide-react";
import { Link } from "react-router-dom";
import { login, logout } from "../../services/authService";
import { useAuth } from "../auth/AuthContext";

export default function Header() {
  const { isAuthenticated, isAdmin, isLoading, user } = useAuth();
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <>
      <header className="header">
        <div className="header-container">
          <Link className="logo" to="/">
            TRƯỜNG PHÁT
          </Link>

          <nav className="desktop-nav">
            <Link to="/">Trang chủ</Link>
            <Link to="/products">Sản phẩm</Link>
            <Link to="/">Liên hệ</Link>
            <Link to="/">Hỗ trợ</Link>
          </nav>

          <div className="header-actions">
            {isLoading ? (
              <span>Đang tải...</span>
            ) : isAuthenticated ? (
              <>
                <span className="welcome-text">
                  Xin chào, {user?.username}!
                </span>

                {isAdmin && (
                  <Link className="admin-link" to="/admin">
                    Quản lý sản phẩm
                  </Link>
                )}

                <button className="login-button" onClick={logout}>
                  Đăng xuất
                </button>
              </>
            ) : (
              <button className="login-button" onClick={login}>
                Đăng nhập
              </button>
            )}

            {/* Nút mở menu */}
            <button
              className="icon-button mobile-menu-button"
              aria-label="Menu"
              onClick={() => setMenuOpen(true)}
            >
              <Menu size={22} />
            </button>
          </div>
        </div>
      </header>

      {/* MENU SLIDE */}
      <div className={`mobile-menu-overlay ${menuOpen ? "open" : ""}`}>
        <div className="mobile-menu">
          <button className="close-menu-btn" onClick={() => setMenuOpen(false)}>
            <X size={24} />
          </button>

          <Link to="/" onClick={() => setMenuOpen(false)}>
            Trang chủ
          </Link>
          <Link to="/products" onClick={() => setMenuOpen(false)}>
            Sản phẩm
          </Link>
          <Link to="/" onClick={() => setMenuOpen(false)}>
            Liên hệ
          </Link>
          <Link to="/" onClick={() => setMenuOpen(false)}>
            Hỗ trợ
          </Link>

          {isAdmin && (
            <Link to="/admin" onClick={() => setMenuOpen(false)}>
              Quản lý sản phẩm
            </Link>
          )}

          {isAuthenticated ? (
            <button className="logout-btn" onClick={logout}>
              Đăng xuất
            </button>
          ) : (
            <button className="logout-btn" onClick={login}>
              Đăng nhập
            </button>
          )}
        </div>
      </div>
    </>
  );
}
