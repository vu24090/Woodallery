import { fetchAuthSession } from "aws-amplify/auth";
import { ApiError } from "./apiError";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

interface RequestOptions extends RequestInit {
  query?: Record<string, string | undefined>;

  requiresAuth?: boolean;
}

async function parseResponseBody(response: Response) {
  const contentType = response.headers.get("content-type");

  if (contentType?.includes("application/json")) {
    return response.json();
  }

  return response.text();
}

async function getAccessToken() {
  const session = await fetchAuthSession();

  return session.tokens?.accessToken?.toString();
}

async function request<T>(
  path: string,
  options: RequestOptions = {}
): Promise<T> {
  const { query, headers, requiresAuth = false, ...fetchOptions } = options;

  const url = new URL(`${API_BASE_URL}${path}`);

  if (query) {
    Object.entries(query).forEach(([key, value]) => {
      if (value !== undefined && value !== "") {
        url.searchParams.set(key, value);
      }
    });
  }

  const requestHeaders = new Headers(headers);

  requestHeaders.set("Content-Type", "application/json");

  if (requiresAuth) {
    const accessToken = await getAccessToken();

    if (!accessToken) {
      throw new ApiError(401, "Authentication required");
    }

    requestHeaders.set("Authorization", `Bearer ${accessToken}`);
  }

  const response = await fetch(url.toString(), {
    ...fetchOptions,
    headers: requestHeaders,
  });

  const data = await parseResponseBody(response);

  if (!response.ok) {
    const message =
      typeof data === "object" && data !== null && "message" in data
        ? String(
            (
              data as {
                message: unknown;
              }
            ).message
          )
        : `Request failed with status ${response.status}`;

    throw new ApiError(response.status, message, data);
  }

  return data as T;
}

export const apiClient = {
  get<T>(path: string, query?: Record<string, string | undefined>) {
    return request<T>(path, {
      method: "GET",
      query,
    });
  },

  post<T>(path: string, body: unknown) {
    return request<T>(path, {
      method: "POST",
      body: JSON.stringify(body),
      requiresAuth: true,
    });
  },

  put<T>(path: string, body: unknown) {
    return request<T>(path, {
      method: "PUT",
      body: JSON.stringify(body),
      requiresAuth: true,
    });
  },

  async delete<T>(path: string): Promise<T> {
    return request<T>(path, {
      method: "DELETE",
      requiresAuth: true,
    });
  },
};
