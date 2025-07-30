package ru.hogwarts.school_;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school_.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    void testCreateStudent() {
        Student student = new Student(null, "Harry", 11);
        ResponseEntity<Student> response = restTemplate.postForEntity(
                baseUrl(), student, Student.class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry");
    }

    @Test
    void testGetStudentById() {
        Student student = new Student(null, "Ron", 12);
        Student created = restTemplate.postForEntity(baseUrl(), student, Student.class).getBody();

        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl() + "/" + created.getId(), Student.class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ron");
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student(null, "Hermione", 13);
        Student created = restTemplate.postForEntity(baseUrl(), student, Student.class).getBody();

        created.setName("Hermione Granger");
        created.setAge(14);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> request = new HttpEntity<>(created, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl(), HttpMethod.PUT, request, Student.class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getName()).isEqualTo("Hermione Granger");
        assertThat(response.getBody().getAge()).isEqualTo(14);
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student(null, "Draco", 14);
        Student created = restTemplate.postForEntity(baseUrl(), student, Student.class).getBody();

        restTemplate.delete(baseUrl() + "/" + created.getId());

        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl() + "/" + created.getId(), Student.class
        );

        assertThat(response.getStatusCode().is4xxClientError()).isTrue(); // Предположим, будет 404
    }

    @Test
    void testGetStudentsByAge() {
        Student student = new Student(null, "Neville", 15);
        restTemplate.postForEntity(baseUrl(), student, Student.class);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                baseUrl() + "/filter?age=15", Student[].class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotEmpty();
    }
}
