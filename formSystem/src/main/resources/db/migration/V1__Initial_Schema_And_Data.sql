DROP TABLE IF EXISTS submission_values;
DROP TABLE IF EXISTS submissions;
DROP TABLE IF EXISTS fields;
DROP TABLE IF EXISTS forms;

CREATE TABLE forms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    display_order INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL, -- ACTIVE, DRAFT
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

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

CREATE TABLE submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    form_id BIGINT NOT NULL,
    submitted_by VARCHAR(255),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_submissions_form FOREIGN KEY (form_id) REFERENCES forms(id) ON DELETE CASCADE
);

CREATE TABLE submission_values (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    submission_id BIGINT NOT NULL,
    field_id BIGINT NOT NULL,
    value TEXT,
    CONSTRAINT fk_values_submission FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
    CONSTRAINT fk_values_field FOREIGN KEY (field_id) REFERENCES fields(id) ON DELETE CASCADE
);


INSERT INTO forms (title, description, display_order, status) VALUES
('Khảo sát Nhân viên 2026', 'Thu thập phản hồi về môi trường làm việc tại SW.', 1, 'ACTIVE'),
('Đăng ký Team Building', 'Khảo sát địa điểm du lịch hè.', 2, 'DRAFT');

INSERT INTO fields (form_id, label, type, field_order, required, options, min_value, max_value) VALUES
(1, 'Họ tên đầy đủ', 'text', 1, TRUE, NULL, NULL, 200),
(1, 'Điểm hài lòng', 'number', 2, TRUE, NULL, 0, 100),
(1, 'Ngày gia nhập công ty', 'date', 3, TRUE, NULL, NULL, NULL),
(1, 'Mã màu nhận diện phòng ban', 'color', 4, FALSE, NULL, NULL, NULL),
(1, 'Vị trí hiện tại', 'select', 5, TRUE, '["Intern", "Junior", "Senior", "Lead"]', NULL, NULL);

INSERT INTO submissions (form_id, submitted_by) VALUES
(1, 'anh.nguyen@sw.com'),
(1, 'bao.tran@sw.com');

INSERT INTO submission_values (submission_id, field_id, value) VALUES
(1, 1, 'Nguyễn Văn Anh'),
(1, 2, '95'),
(1, 3, '2025-10-01'),
(1, 4, '#4287f5'),
(1, 5, 'Junior');

INSERT INTO submission_values (submission_id, field_id, value) VALUES
(2, 1, 'Trần Thế Bảo'),
(2, 2, '80'),
(2, 3, '2026-01-15'),
(2, 4, '#f54242'),
(2, 5, 'Intern');