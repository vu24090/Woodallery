import { createContext } from "react";

interface AuthUser {
  userId: string;
  username: string;
}

export interface AuthContextValue {
  user: AuthUser | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  groups: string[];
  isAdmin: boolean;
  refreshAuth: () => Promise<void>;
}

export const AuthContext = createContext<AuthContextValue | null>(null);
