import { useCallback, useEffect, useState, type ReactNode } from "react";
import { Hub } from "aws-amplify/utils";
import { fetchAuthSession, getCurrentUser } from "aws-amplify/auth";
import { AuthContext, type AuthContextValue } from "./AuthContext";

interface AuthProviderProps {
  children: ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<AuthContextValue["user"]>(null);
  const [groups, setGroups] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const refreshAuth = useCallback(async () => {
    try {
      setIsLoading(true);

      const currentUser = await getCurrentUser();
      const session = await fetchAuthSession();

      const idToken = session.tokens?.idToken;
      const claims = idToken?.payload;
      const cognitoGroups = claims?.["cognito:groups"];

      setUser({
        userId: currentUser.userId,
        username: currentUser.username,
      });

      setGroups(Array.isArray(cognitoGroups) ? cognitoGroups.map(String) : []);
    } catch (error) {
      console.log("No authenticated user", error);
      setUser(null);
      setGroups([]);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    (async () => {
      await refreshAuth();
    })();

    const unsubscribe = Hub.listen("auth", ({ payload }) => {
      if (payload.event === "signedIn" || payload.event === "signedOut") {
        refreshAuth();
      }
    });

    return unsubscribe;
  }, [refreshAuth]);

  const value: AuthContextValue = {
    user,
    isAuthenticated: user !== null,
    isLoading,
    groups,
    isAdmin: groups.includes("ADMIN"),
    refreshAuth,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
