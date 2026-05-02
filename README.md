# Form System

## Tổng quan

- **Admin** quản lý form (tạo, sửa, xóa)
- **Admin** quản lý field trong mỗi form
- **Nhân viên SW** xem danh sách form active và submit dữ liệu
- **Validate** dữ liệu theo loại field và quy tắc cấu hình

---

## Yêu cầu hệ thống

- **Java**: JDK 11 hoặc cao hơn
- **Maven**: 3.6.0 hoặc cao hơn (hoặc sử dụng Maven wrapper `mvnw`)
- **MySQL**: 5.7 hoặc cao hơn
- **Docker** (tùy chọn): Để chạy MySQL trong container

---

## Cài đặt và thiết lập

### 1. Clone dự án

```bash
git clone <repository-url>
cd formSystem
```

### 2. Cấu hình Database

#### Option A: Sử dụng Docker Compose (Khuyến nghị)

```bash
docker-compose up -d
```

File `docker-compose.yml` sẽ tạo container MySQL với:
- **Host**: `localhost`
- **Port**: `3307` (trên máy host)
- **Database**: `form_system`
- **User**: `root`
- **Password**: `1234`

> Lưu ý: ứng dụng Spring Boot trong Docker kết nối tới MySQL bằng `jdbc:mysql://db:3306/form_system`.

#### Option B: Cài đặt MySQL theo cách thủ công

1. Tạo database:
```sql
CREATE DATABASE form_system;
```

2. Cập nhật `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/form_system
spring.datasource.username=root
spring.datasource.password=<your-password>
```

### 3. Cấu hình ứng dụng

Chỉnh sửa file `formSystem/src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/form_system
spring.datasource.username=root
spring.datasource.password=<your-password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Flyway (Migration)
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

---

## Chạy ứng dụng

### Sử dụng Maven Wrapper (Windows)

```bash
cd formSystem
mvnw.cmd spring-boot:run
```

### Sử dụng Maven Wrapper (Linux/Mac)

```bash
cd formSystem
./mvnw spring-boot:run
```

### Sử dụng Maven (nếu đã cài đặt toàn cục)

```bash
mvn spring-boot:run
```

### Sử dụng JAR đã build

```bash
mvn clean package
java -jar formSystem/target/formSystem-0.0.1-SNAPSHOT.jar
```

Ứng dụng sẽ khởi động tại: **http://localhost:8080**

---

## Hướng dẫn nộp bài và chạy project trên máy khác

1. **Clone repository**

```bash
git clone <repository-url>
cd formSystem
```

2. **Chuẩn bị database**

- Nếu dùng Docker Compose:
  ```bash
docker-compose up -d
```
- Nếu dùng MySQL thủ công:
  - Tạo database `form_system`
  - Cập nhật `formSystem/src/main/resources/application.properties` với user/password của bạn

3. **Chuyển vào thư mục ứng dụng**

```bash
cd formSystem
```

4. **Build và chạy project**

```bash
./mvnw.cmd spring-boot:run
```

hoặc trên Linux/Mac:

```bash
./mvnw spring-boot:run
```

5. **Truy cập API**

- `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

6. **Test nhanh bằng Postman**

- Import file: `postman_collection.json`
- Thiết lập biến `baseUrl` thành `http://localhost:8080`

7. **Gửi bài**

- Kiểm tra project đã build được bằng `.
\mvnw.cmd clean package -DskipTests` trong thư mục `formSystem`
- Đảm bảo file `README.md`, `DATABASE_SCHEMA.md`, `postman_collection.json` nằm trong repository
- Push code lên GitHub/GitLab và gửi link repo cho người chấm

---

## Accessing APIs

### Swagger API Documentation

Sau khi ứng dụng chạy, truy cập:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

### Postman Collection

File Postman collection mẫu đã có sẵn trong repo:
- `postman_collection.json`

Nó chứa các request cơ bản cho:
- `GET /api/forms` (pagination)
- `GET /api/forms/{id}`
- `POST /api/forms`
- `PUT /api/forms/{id}`
- `DELETE /api/forms/{id}`
- `POST /api/forms/{formId}/fields`
- `PUT /api/forms/{formId}/fields/{fid}`
- `DELETE /api/forms/{formId}/fields/{fid}`
- `PUT /api/forms/{formId}/fields/reorder`
- `GET /api/forms/active`
- `POST /api/forms/{id}/submit`
- `GET /api/submissions`

---

## Cấu trúc dự án

```
formSystem/                                          # Project root
├── pom.xml                                          # Maven configuration
├── docker-compose.yml                               # Docker setup for MySQL
├── Dockerfile                                       # Docker image for app
├── README.md                                        # Tài liệu này
├── DATABASE_SCHEMA.md                               # Thiết kế database
├── HELP.md                                          # Spring Boot help
├── mvnw / mvnw.cmd                                  # Maven Wrapper
│
└── formSystem/                                      # Main Spring Boot project
    ├── src/
    │   ├── main/
    │   │   ├── java/com/formSystem/formSystem/
    │   │   │   ├── FormSystemApplication.java       # Main application entry
    │   │   │   │
    │   │   │   ├── config/
    │   │   │   │   └── SwaggerConfig.java           # Swagger/OpenAPI configuration
    │   │   │   │
    │   │   │   ├── model/                           # Entity classes
    │   │   │   │   ├── Form.java                    # Form entity
    │   │   │   │   ├── Field.java                   # Form field entity
    │   │   │   │   ├── Submission.java              # Form submission entity
    │   │   │   │   └── SubmissionValue.java         # Submission value entity
    │   │   │   │
    │   │   │   ├── repository/                      # JPA Repositories
    │   │   │   │   ├── FormsRepository.java
    │   │   │   │   ├── FieldsRepository.java
    │   │   │   │   ├── SubmissionsRepository.java
    │   │   │   │   └── SubmissionValueRepository.java
    │   │   │   │
    │   │   │   ├── service/                         # Business logic
    │   │   │   │   ├── FormsService.java            # Form management
    │   │   │   │   ├── FieldsService.java           # Field management
    │   │   │   │   ├── SubmissionsService.java      # Submission handling
    │   │   │   │   └── ValidationService.java       # Data validation logic
    │   │   │   │
    │   │   │   ├── controller/                      # REST Controllers
    │   │   │   │   ├── FormController.java          # Form endpoints
    │   │   │   │   ├── FieldsController.java        # Field endpoints
    │   │   │   │   └── SubmissionsController.java   # Submission endpoints
    │   │   │   │
    │   │   │   ├── dto/                             # Data Transfer Objects
    │   │   │   │   ├── FormRequest.java
    │   │   │   │   ├── FieldRequest.java
    │   │   │   │   ├── FieldReorderRequest.java
    │   │   │   │   ├── SubmissionRequest.java
    │   │   │   │   └── ErrorResponse.java
    │   │   │   │
    │   │   │   ├── exception/                       # Custom exceptions
    │   │   │   │   ├── FormValidationException.java
    │   │   │   │   ├── ValidationException.java
    │   │   │   │   └── GlobalExceptionHandler.java  # Global exception handling
    │   │   │   │
    │   │   │   ├── util/                            # Utility classes
    │   │   │   │   └── ValidationUtil.java          # Validation utilities
    │   │   │   │
    │   │   │   └── constants/                       # Constants
    │   │   │       └── ValidationRules.java
    │   │   │
    │   │   └── resources/
    │   │       ├── application.properties           # Application configuration
    │   │       ├── db/migration/
    │   │       │   └── V1__Initial_Schema_And_Data.sql  # Database migration (Flyway)
    │   │       ├── static/                          # Static files (CSS, JS)
    │   │       └── templates/                       # Thymeleaf templates
    │   │
    │   └── test/
    │       └── java/com/formSystem/formSystem/      # Unit tests
    │           ├── service/
    │           ├── util/
    │           └── FormSystemApplicationTests.java
    │
    └── target/                                      # Build output (generated)
        └── formSystem-0.0.1-SNAPSHOT.jar            # Compiled JAR
```

### Ý nghĩa các thư mục chính

| Thư mục | Mục đích |
|---------|---------|
| `model/` | Entity classes ánh xạ với database tables |
| `repository/` | JPA interfaces để truy vấn database |
| `service/` | Business logic và validation rules |
| `controller/` | REST API endpoints |
| `dto/` | Các lớp để transfer data giữa client và server |
| `exception/` | Custom exceptions và global exception handler |
| `util/` | Utility functions cho validation, conversion, etc. |
| `config/` | Configuration classes (Swagger, Security, etc.) |
| `constants/` | Constants và enum values |
| `resources/db/migration/` | Database migration scripts (Flyway) |

---

## Kiến trúc ứng dụng

### Layered Architecture

```
┌─────────────────────────────────┐
│      REST Controllers           │  (Tiếp nhận requests)
├─────────────────────────────────┤
│      Services Layer             │  (Business logic + Validation)
├─────────────────────────────────┤
│      Repository Layer           │  (Data access)
├─────────────────────────────────┤
│      Database (MySQL)           │  (Data persistence)
└─────────────────────────────────┘
```

### Data Flow

1. **Request** từ client → **Controller**
2. **Controller** gọi **Service**
3. **Service** validate dữ liệu
4. **Service** gọi **Repository**
5. **Repository** truy vấn **Database**
6. **Response** được trả về client

---

## Mô hình dữ liệu

Xem chi tiết tại: [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md)

### Các Entity chính

- **Form**: Đại diện cho một biểu mẫu
- **Field**: Các trường trong form
- **Submission**: Một submission của form
- **SubmissionValue**: Giá trị từng field trong submission

### Mối quan hệ

```
Form (1) ──── (n) Field
  │
  └──── (n) Submission ──── (n) SubmissionValue
```

---

## Technologies Used

- **Framework**: Spring Boot 3.x
- **Build Tool**: Maven
- **Database**: MySQL 5.7+
- **ORM**: JPA/Hibernate
- **Migration**: Flyway
- **API Documentation**: Springdoc OpenAPI (Swagger)
- **Logging**: SLF4J
- **Utilities**: Lombok
- **Testing**: JUnit 5

---

## API Endpoints Overview

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| **GET** | `/api/forms` | Lấy danh sách forms, hỗ trợ pagination với `page` và `size` |
| **GET** | `/api/forms/{id}` | Lấy chi tiết form |
| **POST** | `/api/forms` | Tạo form mới |
| **PUT** | `/api/forms/{id}` | Cập nhật form |
| **DELETE** | `/api/forms/{id}` | Xóa form |
| **GET** | `/api/forms/{formId}/fields` | Lấy fields của form |
| **POST** | `/api/forms/{formId}/fields` | Thêm field vào form |
| **PUT** | `/api/forms/{formId}/fields/{fid}` | Cập nhật field |
| **DELETE** | `/api/forms/{formId}/fields/{fid}` | Xóa field |
| **PUT** | `/api/forms/{formId}/fields/reorder` | Sắp xếp lại thứ tự field |
| **GET** | `/api/forms/active` | Lấy các form active |
| **POST** | `/api/forms/{id}/submit` | Submit form |
| **GET** | `/api/submissions` | Lấy danh sách submissions |

---

## Build & Deploy

### Build Project

```bash
mvn clean package
```

### Run JAR

```bash
java -jar formSystem/target/formSystem-0.0.1-SNAPSHOT.jar
```

### Docker Build & Run

```bash
docker build -t form-system:1.0 .
docker run -d -p 8080:8080 --name form-system form-system:1.0
```

---

## Troubleshooting

### Lỗi: `Access denied for user 'root'@'localhost'`
- Kiểm tra username, password và database URL trong `application.properties`
- Đảm bảo MySQL service đang chạy

### Lỗi: `Communications link failure`
- MySQL container chưa start hoặc port 3306 bị chiếm dụng
- Kiểm tra: `docker ps` (nếu dùng Docker)

### Lỗi: `Column not found`
- Chạy database migration: `mvn flyway:migrate`
- Kiểm tra file `V1__Initial_Schema_And_Data.sql`

---

## File quan trọng

- `README.md` - Hướng dẫn cài đặt và chạy project
- `DATABASE_SCHEMA.md` - Thiết kế schema chi tiết
- `formSystem/src/main/resources/db/migration/V1__Initial_Schema_And_Data.sql` - Migration script ban đầu
- `postman_collection.json` - Postman collection mẫu để test API

![Database Diagram](formSystem/formSystem/img/database-diagram.png)

### Bảng `forms`

- `id` (BIGINT) - khóa chính
- `title` (VARCHAR) - tên form
- `description` (TEXT) - mô tả ngắn
- `display_order` (INT) - thứ tự hiển thị
- `status` (VARCHAR) - `active` hoặc `draft`
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Bảng `fields`

- `id` (BIGINT) - khóa chính
- `form_id` (BIGINT) - tham chiếu tới `forms.id`
- `label` (VARCHAR) - tên hiển thị field
- `type` (VARCHAR) - kiểu field (`text`, `number`, `date`, `color`, `select`)
- `field_order` (INT) - thứ tự trong form
- `required` (BOOLEAN) - bắt buộc hay không
- `options` (TEXT) - danh sách option cho `select` (lưu JSON hoặc CSV)
- `min_value` (INT) - giá trị nhỏ nhất cho number (tùy chọn)
- `max_value` (INT) - giá trị lớn nhất cho number (tùy chọn)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Bảng `submissions`

- `id` (BIGINT) - khóa chính
- `form_id` (BIGINT) - tham chiếu tới `forms.id`
- `submitted_at` (TIMESTAMP)
- `submitted_by` (VARCHAR) - tên người dùng hoặc mã nhân viên (tùy chọn)

### Bảng `submission_values`

- `id` (BIGINT) - khóa chính
- `submission_id` (BIGINT) - tham chiếu tới `submissions.id`
- `field_id` (BIGINT) - tham chiếu tới `fields.id`
- `value` (TEXT) - giá trị nhập vào

## Luồng API dự kiến

### Quản lý Form (Admin)

- `GET /api/forms` - lấy danh sách tất cả form
- `POST /api/forms` - tạo form mới
- `GET /api/forms/{id}` - lấy chi tiết form kèm field
- `PUT /api/forms/{id}` - cập nhật thông tin form
- `DELETE /api/forms/{id}` - xóa form

### Quản lý Field

- `GET /api/forms/{id}/fields` - lấy field của form
- `POST /api/forms/{id}/fields` - thêm field
- `PUT /api/forms/{id}/fields/{fid}` - cập nhật field
- `DELETE /api/forms/{id}/fields/{fid}` - xóa field

### Submission (Nhân viên SW)

- `GET /api/forms/active` - lấy danh sách form active theo thứ tự
- `POST /api/forms/{id}/submit` - submit dữ liệu form
- `GET /api/submissions` - xem lại danh sách submission

## Database schema đề xuất

Đây là thiết kế SQL cơ bản cho MySQL.

- `forms` liên kết 1-n với `fields`
- `forms` liên kết 1-n với `submissions`
- `submissions` liên kết 1-n với `submission_values`

## Chạy dự án

### Yêu cầu

- Java 17
- Maven
- MySQL

### Các bước

1. Tạo database MySQL, ví dụ `form_system`
2. Cập nhật `src/main/resources/application.properties` với datasource:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/form_system?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. Chạy ứng dụng:

```bash
cd formSystem/formSystem
./mvnw spring-boot:run
```

4. Mở API tại:

- `http://localhost:8080/api/forms`
- `http://localhost:8080/swagger-ui.html` (khi Swagger UI đã sẵn sàng)

## Chạy bằng Docker Compose

Trong thư mục gốc `formSystem`, chạy:

```bash
docker compose up --build
```

Dịch vụ sẽ bao gồm:

- `app`: Spring Boot application chạy trên `http://localhost:8080`
- `db`: MySQL 8.0 với database `form_system`

Cấu hình MySQL được thiết lập sẵn bằng biến môi trường:

- `SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/form_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
- `SPRING_DATASOURCE_USERNAME=root`
- `SPRING_DATASOURCE_PASSWORD=1234`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`

> Trên máy host, nếu bạn cần kết nối trực tiếp tới MySQL từ bên ngoài Docker, hãy dùng `localhost:3307`.

Nếu cần dừng dịch vụ:

```bash
docker compose down
```
