
-- 1. Возраст студента >= 16
ALTER TABLE student
    ADD CONSTRAINT chk_student_age CHECK (age >= 16);

-- 2. Имя студента NOT NULL и уникальное
ALTER TABLE student
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE student
    ADD CONSTRAINT uq_student_name UNIQUE (name);

-- 3. Значение по умолчанию для возраста = 20
ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;

-- 4. Уникальная комбинация (название факультета, цвет)
ALTER TABLE faculty
    ADD CONSTRAINT uq_faculty_name_color UNIQUE (name, color);
