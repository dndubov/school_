package ru.hogwarts.school_.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school_.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findByColorIgnoreCase(String color);

    List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
}
