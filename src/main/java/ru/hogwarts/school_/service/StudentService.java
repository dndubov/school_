package ru.hogwarts.school_.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school_.model.Faculty;
import ru.hogwarts.school_.model.Student;
import ru.hogwarts.school_.repository.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyService facultyService;

    public StudentService(StudentRepository studentRepository,
                          FacultyService facultyService) {
        this.studentRepository = studentRepository;
        this.facultyService = facultyService;
    }

    public Student createStudent(Student student) {
        if (student.getFaculty() != null) {
            Faculty faculty = facultyService.getFaculty(student.getFaculty().getId());
            student.setFaculty(faculty);
        }
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, Student student) {
        student.setId(id);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByAge(int age) {
        return studentRepository.findByAge(age);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    @Transactional(readOnly = true)
    public Faculty getFacultyByStudentId(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public long getStudentsCount() {
        return studentRepository.getStudentsCount();
    }

    @Transactional(readOnly = true)
    public double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    @Transactional(readOnly = true)
    public List<Student> getLastFiveStudents() {
        return studentRepository.getLastFiveStudents();
    }
}