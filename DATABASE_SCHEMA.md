# Database Schema

## Thiết kế bảng MySQL cho hệ thống Form

### Bảng `forms`

```sql
CREATE TABLE forms (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  display_order INT NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Bảng `fields`

```sql
CREATE TABLE fields (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  form_id BIGINT NOT NULL,
  label VARCHAR(255) NOT NULL,
  type VARCHAR(50) NOT NULL,
  field_order INT NOT NULL DEFAULT 0,
  required BOOLEAN NOT NULL DEFAULT FALSE,
  options TEXT,
  min_value INT,
  max_value INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (form_id) REFERENCES forms(id) ON DELETE CASCADE
);
```

### Bảng `submissions`

```sql
CREATE TABLE submissions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  form_id BIGINT NOT NULL,
  submitted_by VARCHAR(255),
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (form_id) REFERENCES forms(id) ON DELETE CASCADE
);
```

### Bảng `submission_values`

```sql
CREATE TABLE submission_values (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  submission_id BIGINT NOT NULL,
  field_id BIGINT NOT NULL,
  value TEXT,
  FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
  FOREIGN KEY (field_id) REFERENCES fields(id) ON DELETE CASCADE
);
```

## Mô hình dữ liệu liên kết

- `forms` 1-n `fields`
- `forms` 1-n `submissions`
- `submissions` 1-n `submission_values`
- `fields` 1-n `submission_values`

## Gợi ý lưu trữ giá trị field

- Với `type = select`, trường `options` có thể lưu chuỗi JSON, ví dụ:

```json
["Option A", "Option B", "Option C"]
```

- Với các trường number/date/color/text, giá trị user nhập có thể lưu trong `submission_values.value`.

## Validation logic

Các quy tắc validate nên được tách ra thành module riêng:

- text: required, max length 200
- number: required, giá trị trong khoảng 0-100
- date: required, không được chọn ngày quá khứ
- color: required, phải là mã HEX `#RRGGBB`
- select: required, phải chọn 1 giá trị trong `options`
