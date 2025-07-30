package ru.hogwarts.school_;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school_.model.Faculty;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculty";
    }

    @Test
    void testCreateFaculty() {
        Faculty faculty = Faculty.builder()
                .name("Ravenclaw")
                .color("Blue")
                .build();

        ResponseEntity<Faculty> response = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ravenclaw");
        assertThat(response.getBody().getColor()).isEqualTo("Blue");
    }

    @Test
    void testGetFacultyById() {
        Faculty faculty = Faculty.builder()
                .name("Hufflepuff")
                .color("Yellow")
                .build();

        Faculty created = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class).getBody();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/" + created.getId(), Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Hufflepuff");
    }

    @Test
    void testUpdateFaculty() {
        Faculty faculty = Faculty.builder()
                .name("Slytherin")
                .color("Green")
                .build();

        Faculty created = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class).getBody();
        created.setColor("Dark Green");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Faculty> entity = new HttpEntity<>(created, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange(getBaseUrl(), HttpMethod.PUT, entity, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getColor()).isEqualTo("Dark Green");
    }

    @Test
    void testDeleteFaculty() {
        Faculty faculty = Faculty.builder()
                .name("Durmstrang")
                .color("Black")
                .build();

        Faculty created = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class).getBody();

        restTemplate.delete(getBaseUrl() + "/" + created.getId());

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/" + created.getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
