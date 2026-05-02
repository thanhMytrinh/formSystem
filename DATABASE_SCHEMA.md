# Database Schema - Form System

## Tổng quan

Hệ thống Form sử dụng **MySQL** với 4 bảng chính:
- `forms`: Lưu trữ thông tin các form
- `fields`: Lưu trữ các field trong mỗi form
- `submissions`: Lưu trữ các submission từ user
- `submission_values`: Lưu trữ giá trị từng field trong mỗi submission

---

## Thiết kế bảng MySQL

### 1. Bảng `forms`

Lưu trữ thông tin các biểu mẫu

```sql
CREATE TABLE forms (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  display_order INT NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL, -- ACTIVE, DRAFT
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | BIGINT | Primary key, auto-increment |
| `title` | VARCHAR(255) | Tên form (bắt buộc) |
| `description` | TEXT | Mô tả chi tiết về form |
| `display_order` | INT | Thứ tự hiển thị |
| `status` | VARCHAR(20) | Trạng thái: `ACTIVE` (hiển thị) hoặc `DRAFT` (bản nháp) |
| `created_at` | TIMESTAMP | Thời gian tạo |
| `updated_at` | TIMESTAMP | Thời gian cập nhật cuối |

**Ví dụ dữ liệu:**
```
id=1, title="Khảo sát Nhân viên 2026", status="ACTIVE"
id=2, title="Đăng ký Team Building", status="DRAFT"
```

---

### 2. Bảng `fields`

Lưu trữ các field trong mỗi form

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
  CONSTRAINT fk_fields_form FOREIGN KEY (form_id) REFERENCES forms(id) ON DELETE CASCADE
);
```

| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | BIGINT | Primary key, auto-increment |
| `form_id` | BIGINT | Foreign key tới `forms.id` (bắt buộc) |
| `label` | VARCHAR(255) | Nhãn/tiêu đề của field |
| `type` | VARCHAR(50) | Loại field: `text`, `number`, `date`, `color`, `select` |
| `field_order` | INT | Thứ tự hiển thị field trong form |
| `required` | BOOLEAN | Trường bắt buộc hay không |
| `options` | TEXT | JSON array các tùy chọn (cho type=`select`) |
| `min_value` | INT | Giá trị tối thiểu (cho type=`number`) |
| `max_value` | INT | Giá trị tối đa (cho type=`number`) |
| `created_at` | TIMESTAMP | Thời gian tạo |
| `updated_at` | TIMESTAMP | Thời gian cập nhật cuối |

**Ví dụ dữ liệu:**
```
id=1, form_id=1, label="Họ tên đầy đủ", type="text", required=true
id=2, form_id=1, label="Điểm hài lòng", type="number", required=true, min_value=0, max_value=100
id=5, form_id=1, label="Vị trí hiện tại", type="select", options='["Intern", "Junior", "Senior", "Lead"]'
```

**Các loại field được hỗ trợ:**

| Type | Mô tả | Validation |
|------|-------|-----------|
| `text` | Nhập văn bản | Max length 200 ký tự |
| `number` | Nhập số | Trong khoảng min_value - max_value |
| `date` | Chọn ngày | Không được chọn ngày quá khứ |
| `color` | Chọn màu | Định dạng HEX (#RRGGBB) |
| `select` | Chọn từ danh sách | Phải chọn 1 trong `options` |

---

### 3. Bảng `submissions`

Lưu trữ các submission từ user

```sql
CREATE TABLE submissions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  form_id BIGINT NOT NULL,
  submitted_by VARCHAR(255),
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_submissions_form FOREIGN KEY (form_id) REFERENCES forms(id) ON DELETE CASCADE
);
```

| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | BIGINT | Primary key, auto-increment |
| `form_id` | BIGINT | Foreign key tới `forms.id` (bắt buộc) |
| `submitted_by` | VARCHAR(255) | Email hoặc tên người submit (nullable) |
| `submitted_at` | TIMESTAMP | Thời gian submit (mặc định: hiện tại) |

**Ví dụ dữ liệu:**
```
id=1, form_id=1, submitted_by="anh.nguyen@sw.com", submitted_at="2026-05-02 10:30:00"
id=2, form_id=1, submitted_by="bao.tran@sw.com", submitted_at="2026-05-02 11:00:00"
```

---

### 4. Bảng `submission_values`

Lưu trữ giá trị từng field trong mỗi submission

```sql
CREATE TABLE submission_values (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  submission_id BIGINT NOT NULL,
  field_id BIGINT NOT NULL,
  value TEXT,
  CONSTRAINT fk_values_submission FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
  CONSTRAINT fk_values_field FOREIGN KEY (field_id) REFERENCES fields(id) ON DELETE CASCADE
);
```

| Cột | Kiểu | Mô tả |
|-----|------|-------|
| `id` | BIGINT | Primary key, auto-increment |
| `submission_id` | BIGINT | Foreign key tới `submissions.id` (bắt buộc) |
| `field_id` | BIGINT | Foreign key tới `fields.id` (bắt buộc) |
| `value` | TEXT | Giá trị được submit |

**Ví dụ dữ liệu:**
```
id=1, submission_id=1, field_id=1, value="Nguyễn Văn Anh"
id=2, submission_id=1, field_id=2, value="95"
id=3, submission_id=1, field_id=3, value="2025-10-01"
id=4, submission_id=1, field_id=4, value="#4287f5"
id=5, submission_id=1, field_id=5, value="Junior"
```

---

## Mô hình dữ liệu liên kết (Entity-Relationship Diagram)

```
┌──────────────┐
│    forms     │
│  (id, title) │
└──────┬───────┘
       │ 1
       │
       ├─────────────── (n) ┌──────────────┐
       │                    │    fields    │
       │                    │ (id, label)  │
       │                    └──────┬───────┘
       │                           │ (n)
       ├─────────────── (n) ┌──────▼──────────────┐
       │                    │   submission_values │
       │                    │  (id, field_id)    │
       │                    └────────▲────────────┘
       │                             │
       ├─ (n) ┌──────────────┐       │
              │ submissions  │───────┘
              │  (id,        │ (1)
              │ form_id)     │
              └──────────────┘
```

### Các mối quan hệ:

| Quan hệ | Mô tả |
|---------|-------|
| `forms` 1 — (n) `fields` | Một form có nhiều field |
| `forms` 1 — (n) `submissions` | Một form có nhiều submission |
| `submissions` 1 — (n) `submission_values` | Một submission có nhiều giá trị |
| `fields` 1 — (n) `submission_values` | Một field có nhiều submission value từ các submission khác nhau |

**Cascade Delete:** Khi xóa form hoặc submission, tất cả child records sẽ bị xóa theo (ON DELETE CASCADE)

---

## Lưu trữ giá trị Field

### Trường `options` (cho type=`select`)

Lưu dữ liệu dưới dạng JSON array string:

```json
["Option A", "Option B", "Option C"]
```

**Ví dụ:**
```sql
INSERT INTO fields (form_id, label, type, options) VALUES
(1, 'Vị trí hiện tại', 'select', '["Intern", "Junior", "Senior", "Lead"]');
```

### Trường `value` (trong `submission_values`)

Lưu giá trị dưới dạng TEXT (có thể ép kiểu khi load):

```sql
INSERT INTO submission_values (submission_id, field_id, value) VALUES
(1, 1, 'Nguyễn Văn Anh'),          -- text
(1, 2, '95'),                      -- number (as string)
(1, 3, '2025-10-01'),              -- date (YYYY-MM-DD)
(1, 4, '#4287f5'),                 -- color (hex)
(1, 5, 'Junior');                  -- select (một option)
```

---

## Validation Rules

| Field Type | Validation Rules |
|-----------|------------------|
| `text` | - Bắt buộc (nếu required=true)<br>- Tối đa 200 ký tự<br>- Không để trống |
| `number` | - Bắt buộc (nếu required=true)<br>- Giá trị số nguyên<br>- Trong khoảng [min_value, max_value]<br>- Ví dụ: 0-100 cho satisfaction score |
| `date` | - Bắt buộc (nếu required=true)<br>- Định dạng YYYY-MM-DD<br>- Không được chọn ngày quá khứ<br>- Không được chọn ngày > hôm nay |
| `color` | - Bắt buộc (nếu required=true)<br>- Định dạng HEX: #RRGGBB<br>- Ví dụ: #FF0000, #4287f5 |
| `select` | - Bắt buộc (nếu required=true)<br>- Giá trị phải có trong `options` array<br>- Chỉ chọn 1 option |

---

## Indexes & Performance

### Recommended Indexes

```sql
-- Tìm kiếm form theo status
CREATE INDEX idx_forms_status ON forms(status);

-- Tìm kiếm field theo form
CREATE INDEX idx_fields_form_id ON fields(form_id);

-- Tìm kiếm submission theo form
CREATE INDEX idx_submissions_form_id ON submissions(form_id);

-- Tìm kiếm submission values theo submission
CREATE INDEX idx_submission_values_submission_id ON submission_values(submission_id);

-- Tìm kiếm submission values theo field
CREATE INDEX idx_submission_values_field_id ON submission_values(field_id);
```

---

## Dữ liệu mẫu (Sample Data)

### Khởi tạo dữ liệu

Xem file migration: [V1__Initial_Schema_And_Data.sql](formSystem/src/main/resources/db/migration/V1__Initial_Schema_And_Data.sql)

#### Forms
```
ID | Title | Status | Created_at
1  | Khảo sát Nhân viên 2026 | ACTIVE | 2026-05-02
2  | Đăng ký Team Building   | DRAFT  | 2026-05-02
```

#### Fields (Form 1)
```
ID | Form_ID | Label | Type | Required | Options/Range
1  | 1 | Họ tên đầy đủ | text | true | -
2  | 1 | Điểm hài lòng | number | true | 0-100
3  | 1 | Ngày gia nhập | date | true | -
4  | 1 | Mã màu phòng ban | color | false | -
5  | 1 | Vị trí hiện tại | select | true | ["Intern", "Junior", "Senior", "Lead"]
```

#### Submissions (Form 1)
```
ID | Form_ID | Submitted_By | Submitted_At
1  | 1 | anh.nguyen@sw.com | 2026-05-02 10:30:00
2  | 1 | bao.tran@sw.com | 2026-05-02 11:00:00
```

#### Submission Values
```
Submission 1 Data:
- Field 1 (Name): "Nguyễn Văn Anh"
- Field 2 (Score): "95"
- Field 3 (Join Date): "2025-10-01"
- Field 4 (Color): "#4287f5"
- Field 5 (Position): "Junior"

Submission 2 Data:
- Field 1 (Name): "Trần Thế Bảo"
- Field 2 (Score): "80"
- Field 3 (Join Date): "2026-01-15"
- Field 4 (Color): "#f54242"
- Field 5 (Position): "Intern"
```

---

## Các lệnh SQL thường dùng

### Lấy form và số lượng field của nó
```sql
SELECT f.id, f.title, COUNT(fi.id) as field_count
FROM forms f
LEFT JOIN fields fi ON f.id = fi.form_id
GROUP BY f.id
ORDER BY f.display_order;
```

### Lấy tất cả submission của một form
```sql
SELECT s.id, s.submitted_by, s.submitted_at, COUNT(sv.id) as value_count
FROM submissions s
LEFT JOIN submission_values sv ON s.id = sv.submission_id
WHERE s.form_id = ?
GROUP BY s.id
ORDER BY s.submitted_at DESC;
```

### Lấy chi tiết submission (form + field + value)
```sql
SELECT 
  s.id as submission_id,
  s.submitted_by,
  s.submitted_at,
  f.label as field_label,
  f.type as field_type,
  sv.value
FROM submissions s
JOIN submission_values sv ON s.id = sv.submission_id
JOIN fields f ON sv.field_id = f.id
WHERE s.id = ?
ORDER BY f.field_order;
```

### Tính thống kê submission
```sql
SELECT 
  COUNT(DISTINCT s.id) as total_submissions,
  COUNT(DISTINCT s.submitted_by) as unique_submitters,
  MIN(s.submitted_at) as first_submission,
  MAX(s.submitted_at) as last_submission
FROM submissions s
WHERE s.form_id = ?;
```

---

## Migration & Version Control

Database schema được quản lý bằng **Flyway** migrations.

File migration: `formSystem/src/main/resources/db/migration/V1__Initial_Schema_And_Data.sql`

**Naming convention:**
- `V1__Initial_Schema_And_Data.sql` - Initial version
- `V2__Add_new_column.sql` - Version 2
- Etc.

Khi thêm column hoặc bảng mới, tạo file migration mới và follow convention:
- `VX__` (X là version number)
- Mô tả thay đổi bằng snake_case

---

## Backup & Recovery

### Backup Database
```bash
mysqldump -u root -p form_system > form_system_backup.sql
```

### Restore Database
```bash
mysql -u root -p form_system < form_system_backup.sql
``
