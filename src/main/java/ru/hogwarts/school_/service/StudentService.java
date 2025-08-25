package ru.hogwarts.school_.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school_.model.Faculty;
import ru.hogwarts.school_.model.Student;
import ru.hogwarts.school_.repository.StudentRepository;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final FacultyService facultyService;


    public Student createStudent(Student student) {
        logger.info("Was invoked method createStudent with student: {}", student);
        if (student.getFaculty() != null) {
            Faculty faculty = facultyService.getFaculty(student.getFaculty().getId());
            student.setFaculty(faculty);
            logger.debug("Student assigned to faculty: {}", faculty);
        }
        Student saved = studentRepository.save(student);
        logger.info("Student was saved: {}", saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public Student getStudent(Long id) {
        logger.info("Was invoked method getStudent with id = {}", id);
        return studentRepository.findById(id).orElseGet(() -> {
            logger.error("There is no student with id = {}", id);
            return null;
        });
    }

    public Student updateStudent(Long id, Student student) {
        logger.info("Was invoked method updateStudent with id = {} and student: {}", id, student);
        student.setId(id);
        Student updated = studentRepository.save(student);
        logger.debug("Student was updated: {}", updated);
        return updated;
    }

    public void deleteStudent(Long id) {
        logger.warn("Was invoked method deleteStudent with id = {}", id);
        studentRepository.deleteById(id);
        logger.info("Student with id = {} was deleted", id);
    }

    @Transactional(readOnly = true)
    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method getAllStudents");
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method getStudentsByAge with age = {}", age);
        return studentRepository.findByAge(age);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByAgeBetween(int min, int max) {
        logger.info("Was invoked method getStudentsByAgeBetween with min = {} and max = {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    @Transactional(readOnly = true)
    public Faculty getFacultyByStudentId(Long studentId) {
        logger.info("Was invoked method getFacultyByStudentId with studentId = {}", studentId);
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseGet(() -> {
                    logger.error("No faculty found for studentId = {}", studentId);
                    return null;
                });
    }

    @Transactional(readOnly = true)
    public long getStudentsCount() {
        logger.info("Was invoked method getStudentsCount");
        return studentRepository.getStudentsCount();
    }

    @Transactional(readOnly = true)
    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method getLastFiveStudents");
        return studentRepository.getLastFiveStudents();
    }

    @Transactional(readOnly = true)
    public List<String> getStudentNamesStartingWithA() {
        logger.info("Filtering student names starting with 'A'...");
        List<String> names = studentRepository.findAll()
                .parallelStream()
                .map(Student::getName)
                .filter(name -> name.toUpperCase().startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .toList();
        logger.info("Found {} names starting with 'A'", names.size());
        return names;
    }

    @Transactional(readOnly = true)
    public double getAverageAgeOfAllStudents() {
        logger.info("Calculating average age of all students...");
        double avg = studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
        logger.info("Average age calculated: {}", avg);
        return avg;
    }

}
