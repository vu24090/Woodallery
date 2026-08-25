import {
  createContext,
  useContext,
  useCallback,
  useEffect,
  useState,
  type ReactNode,
} from "react";
import { Hub } from "aws-amplify/utils";
import { fetchAuthSession, getCurrentUser } from "aws-amplify/auth";

interface AuthUser {
  userId: string;
  username: string;
}

interface AuthContextValue {
  user: AuthUser | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  groups: string[];
  isAdmin: boolean;
  refreshAuth: () => Promise<void>;
}

const AuthContext = createContext<AuthContextValue | null>(null);

interface AuthProviderProps {
  children: ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<AuthUser | null>(null);

  const [groups, setGroups] = useState<string[]>([]);

  const [isLoading, setIsLoading] = useState(true);

  const refreshAuth = useCallback(async () => {
    try {
      setIsLoading(true);

      const currentUser = await getCurrentUser();
      console.log("currentUser:", currentUser);

      const session = await fetchAuthSession();
      console.log("is authenticated session:", session);

      const idToken = session.tokens?.idToken;

      const claims = idToken?.payload;
      console.log("claims:", claims);

      const cognitoGroups = claims?.["cognito:groups"];
      console.log("cognito groups:", cognitoGroups);

      setUser({
        userId: currentUser.userId,

        username: currentUser.username,
      });

      setGroups(Array.isArray(cognitoGroups) ? cognitoGroups.map(String) : []);
    } catch (error) {
      console.log("No authenticated user");

      setUser(null);
      setGroups([]);
    } finally {
      setIsLoading(false);
    }
    console.log("refreshAuth called");
  }, []);

  useEffect(() => {
    refreshAuth();

    const unsubscribe = Hub.listen("auth", ({ payload }) => {
      console.log("Auth event:", payload.event);

      if (payload.event === "signedIn" || payload.event === "signedOut") {
        refreshAuth();
      }
    });

    return unsubscribe;
  }, [refreshAuth]);

  const value = {
    user,
    isAuthenticated: user !== null,

    isLoading,

    groups,

    isAdmin: groups.includes("ADMIN"),

    refreshAuth,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }

  return context;
}
