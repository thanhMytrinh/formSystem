-- V2__Insert_fake_data.sql

-- Insert fake users
INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@example.com', '$2a$10$examplehashedpassword', 'ADMIN'),
('user1', 'user1@example.com', '$2a$10$examplehashedpassword', 'USER'),
('user2', 'user2@example.com', '$2a$10$examplehashedpassword', 'USER');

-- Insert fake forms
INSERT INTO forms (title, description, created_by, is_active) VALUES
('Contact Form', 'A simple contact form', 1, TRUE),
('Survey Form', 'Customer satisfaction survey', 1, TRUE);

-- Insert fake form fields for Contact Form (id=1)
INSERT INTO form_fields (form_id, field_name, field_type, label, placeholder, is_required, field_order) VALUES
(1, 'name', 'text', 'Full Name', 'Enter your full name', TRUE, 1),
(1, 'email', 'email', 'Email Address', 'Enter your email', TRUE, 2),
(1, 'message', 'textarea', 'Message', 'Enter your message', TRUE, 3);

-- Insert fake form fields for Survey Form (id=2)
INSERT INTO form_fields (form_id, field_name, field_type, label, placeholder, is_required, options, field_order) VALUES
(2, 'age', 'number', 'Age', 'Enter your age', TRUE, NULL, 1),
(2, 'satisfaction', 'select', 'Satisfaction Level', 'Select your satisfaction', TRUE, '["Very Satisfied", "Satisfied", "Neutral", "Dissatisfied", "Very Dissatisfied"]', 2),
(2, 'comments', 'textarea', 'Comments', 'Any additional comments', FALSE, NULL, 3);

-- Insert fake submissions
INSERT INTO submissions (form_id, submitted_by) VALUES
(1, 2),
(2, 3);

-- Insert fake submission data
INSERT INTO submission_data (submission_id, field_id, field_value) VALUES
(1, 1, 'John Doe'),
(1, 2, 'john@example.com'),
(1, 3, 'Hello, this is a test message.'),
(2, 4, '25'),
(2, 5, 'Satisfied'),
(2, 6, 'Great service!');