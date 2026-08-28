import { useEffect } from "react";

import { useNavigate } from "react-router-dom";

import { fetchAuthSession } from "aws-amplify/auth";

export default function AuthCallbackPage() {
  const navigate = useNavigate();

  useEffect(() => {
    async function handleCallback() {
      try {
        const session = await fetchAuthSession();

        console.log("Authenticated session:", session);

        navigate("/", {
          replace: true,
        });
      } catch (error) {
        console.error("Authentication callback failed:", error);

        navigate("/", {
          replace: true,
        });
      }
    }

    handleCallback();
  }, [navigate]);

  return (
    <main className="auth-callback">
      <div>
        <h1>Đang đăng nhập...</h1>

        <p>Vui lòng chờ trong giây lát.</p>
      </div>
    </main>
  );
}
