import { ApiError } from "../services/apiError";

export function getApiErrorMessage(error: unknown): string {
  if (error instanceof ApiError) {
    switch (error.status) {
      case 400:
        return "Dữ liệu không hợp lệ.";

      case 401:
        return "Phiên đăng nhập không hợp lệ.";

      case 403:
        return "Bạn không có quyền thực hiện thao tác này.";

      case 404:
        return "Không tìm thấy dữ liệu.";

      case 409:
        return "Dữ liệu đang bị xung đột.";

      case 500:
        return "Máy chủ đang gặp sự cố.";

      default:
        return "Đã xảy ra lỗi.";
    }
  }

  return "Không thể kết nối tới máy chủ.";
}
