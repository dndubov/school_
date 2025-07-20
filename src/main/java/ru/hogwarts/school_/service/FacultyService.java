package ru.hogwarts.school_.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school_.model.Faculty;
import ru.hogwarts.school_.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        if (facultyRepository.existsById(id)) {
            faculty.setId(id);
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public Faculty deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty != null) {
            facultyRepository.deleteById(id);
        }
        return faculty;
    }

    public List<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .toList();
    }
}
