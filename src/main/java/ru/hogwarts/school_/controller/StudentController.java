package ru.hogwarts.school_.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school_.model.Faculty;
import ru.hogwarts.school_.model.Student;
import ru.hogwarts.school_.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping("/{id}/update")
    public Student updateStudent(@PathVariable Long id,
                                 @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/all-student")
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/filter")
    public List<Student> getStudentsByAge(@RequestParam int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("/age-between")
    public List<Student> getStudentsByAgeBetween(
            @RequestParam int min,
            @RequestParam int max) {
        return studentService.getStudentsByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFacultyByStudentId(@PathVariable Long id) {
        return studentService.getFacultyByStudentId(id);
    }

    @GetMapping("/count")
    public long getStudentsCount() {
        return studentService.getStudentsCount();
    }


    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-starting-with-a")
    public List<String> getStudentNamesStartingWithA() {
        return studentService.getStudentNamesStartingWithA();
    }

    @GetMapping("/students/average-age")
    public double getAverageAge() {
        return studentService.getAverageAgeOfAllStudents();
    }

    @GetMapping("/students/print-parallel")
    public void printStudentsParallel() {
        List<Student> students = studentService.getAllStudents().stream().toList();

        if (students.size() < 6) {
            System.out.println("Недостаточно студентов (нужно минимум 6)");
            return;
        }

        // первые два имени — основной поток
        System.out.println(Thread.currentThread().getName() + " → " + students.get(0).getName());
        System.out.println(Thread.currentThread().getName() + " → " + students.get(1).getName());

        // третье и четвертое имя — первый параллельный поток
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " → " + students.get(2).getName());
            System.out.println(Thread.currentThread().getName() + " → " + students.get(3).getName());
        }).start();

        // пятое и шестое имя — второй параллельный поток
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " → " + students.get(4).getName());
            System.out.println(Thread.currentThread().getName() + " → " + students.get(5).getName());
        }).start();
    }

    @GetMapping("/students/print-synchronized")
    public void printStudentsSynchronized() {
        List<Student> students = studentService.getAllStudents().stream().toList();

        if (students.size() < 6) {
            System.out.println("Недостаточно студентов (нужно минимум 6)");
            return;
        }

        // синхронизированный метод для вывода имени
        Consumer<String> printName = name -> {
            synchronized (System.out) {
                System.out.println(Thread.currentThread().getName() + " → " + name);
            }
        };

        // первые два имени — основной поток
        students.stream().limit(2).forEach(s -> printName.accept(s.getName()));

        // третье и четвертое имя — первый параллельный поток
        new Thread(() -> students.stream().skip(2).limit(2)
                .forEach(s -> printName.accept(s.getName()))
        ).start();

        // пятое и шестое имя — второй параллельный поток
        new Thread(() -> students.stream().skip(4).limit(2)
                .forEach(s -> printName.accept(s.getName()))
        ).start();
    }

}