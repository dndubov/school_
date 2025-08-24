package ru.hogwarts.school_;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import ru.hogwarts.school_.controller.StudentController;
import ru.hogwarts.school_.model.Student;
import ru.hogwarts.school_.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import(StudentControllerWebMvcTest.StudentServiceTestConfig.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentService studentService;

    @TestConfiguration
    static class StudentServiceTestConfig {
        @Bean
        public StudentService studentService() {
            return mock(StudentService.class);
        }
    }

    @Test
    void createStudent() throws Exception {
        Student student = new Student(1L, "Ivan", 20, null);
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    void getStudent() throws Exception {
        Student student = new Student(1L, "Ivan", 20, null);
        when(studentService.getStudent(any(Long.class))).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    void updateStudent() throws Exception {
        Student student = new Student(1L, "Updated", 22, null);
        when(studentService.updateStudent(eq(1L), any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.age").value(22));
    }

    @Test
    void deleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(any(Long.class));

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getStudentsByAge() throws Exception {
        Student student = new Student(1L, "Ivan", 20, null);
        when(studentService.getStudentsByAge(20)).thenReturn(List.of(student));

        mockMvc.perform(get("/student/filter?age=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(20));
    }
}
