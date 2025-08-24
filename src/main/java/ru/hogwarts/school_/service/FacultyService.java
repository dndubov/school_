package ru.hogwarts.school_.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school_.model.Faculty;
import ru.hogwarts.school_.model.Student;
import ru.hogwarts.school_.repository.FacultyRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
@Transactional
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public Faculty createFaculty(Faculty faculty) {
        log.info("Was invoked method for createFaculty: {}", faculty);
        return facultyRepository.save(faculty);
    }

    @Transactional(readOnly = true)
    public Faculty getFaculty(Long id) {
        log.info("Was invoked method for getFaculty with id={}", id);
        return facultyRepository.findById(id)
                .orElseGet(() -> {
                    log.warn("Faculty not found with id={}", id);
                    return null;
                });
    }

    @Transactional(readOnly = true)
    public Faculty getFacultyById(Long id) {
        log.debug("Was invoked method for getFacultyById with id={}", id);
        return getFaculty(id);
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        log.info("Was invoked method for updateFaculty with id={}: {}", id, faculty);
        if (!facultyRepository.existsById(id)) {
            log.warn("Trying to update non-existing faculty with id={}", id);
        }
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        log.info("Was invoked method for deleteFaculty with id={}", id);
        if (!facultyRepository.existsById(id)) {
            log.warn("Trying to delete non-existing faculty with id={}", id);
        }
        facultyRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Faculty> getFacultiesByColor(String color) {
        log.info("Was invoked method for getFacultiesByColor with color={}", color);
        return facultyRepository.findByColorIgnoreCase(color);
    }

    @Transactional(readOnly = true)
    public List<Faculty> findByNameOrColor(String query) {
        log.info("Was invoked method for findByNameOrColor with query={}", query);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(query, query);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByFacultyId(Long facultyId) {
        log.info("Was invoked method for getStudentsByFacultyId with id={}", facultyId);
        if (facultyId == null) {
            log.warn("FacultyId is null, returning empty list");
            return Collections.emptyList();
        }
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElseGet(() -> {
                    log.warn("Faculty not found with id={}, returning empty list", facultyId);
                    return Collections.emptyList();
                });
    }

    @Transactional(readOnly = true)
    public Optional<Faculty> findByName(String name) {
        log.info("Was invoked method for findByName with name={}", name);
        return facultyRepository.findByName(name);
    }
}
