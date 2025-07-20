package ru.hogwarts.school_.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school_.model.Faculty;
import ru.hogwarts.school_.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }

    @PutMapping("/{id}")
    public Faculty updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty){
        return facultyService.updateFaculty(id, faculty);
    }

    @DeleteMapping("/{id}")
    public Faculty deleteFaculty(@PathVariable Long id){
        return facultyService.deleteFaculty(id);
    }

    @GetMapping("/filter")
    public List<Faculty> getFacultiesByColor(@RequestParam String color){
        return facultyService.getFacultiesByColor(color);
    }
}
