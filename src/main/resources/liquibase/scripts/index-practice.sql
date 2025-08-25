-- liquibase formatted sql

-- changeset dndubov:1
CREATE INDEX idx_student_name ON student(name);

-- changeset dndubov:2
CREATE INDEX idx_faculty_name_color ON faculty(name, color);
