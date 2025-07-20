package ru.hogwarts.school_.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school_.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
