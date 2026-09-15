# TRIỂN KHAI VÀ VẬN HÀNH HỆ THỐNG WEBSITE THEO KIẾN TRÚC SERVERLESS

## 1. Giới thiệu

Đây là mã nguồn đồ án ngành với đề tài **“Triển khai và vận hành hệ thống Website theo kiến trúc Serverless”**.

Hệ thống được xây dựng theo kiến trúc Serverless trên nền tảng AWS, sử dụng React cho Frontend và các dịch vụ AWS như **API Gateway, Lambda, DynamoDB, Cognito, S3 và CloudFront**. Quy trình triển khai được hỗ trợ bởi **GitHub Actions** nhằm tự động hóa build và deployment.

Repository gồm hai thành phần chính:

```text
frontend/              # Mã nguồn giao diện Website
backend/showroom-backend/  # Mã nguồn REST API
```

## 2. Truy cập Website

Website đã được triển khai trên AWS và có thể truy cập tại:

**https://ds8embvnehnu8.cloudfront.net**

Người dùng có thể truy cập trực tiếp bằng trình duyệt để sử dụng các chức năng của hệ thống.

## 3. Chạy Website trên Local

### Frontend

Yêu cầu: **Node.js và npm**

```bash
cd frontend
npm install
npm run dev
```

Sau đó truy cập:

```text
http://localhost:5173
```

### Backend

Yêu cầu: **Java 21, Maven và AWS SAM CLI**

```bash
cd backend/showroom-backend
sam build
sam local start-api
```

API local mặc định chạy tại:

```text
http://127.0.0.1:3000
```

> Khi chạy Frontend local, cần cấu hình URL API tương ứng trong file `.env` của Frontend.
