package ru.hogwarts.school_.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school_.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
