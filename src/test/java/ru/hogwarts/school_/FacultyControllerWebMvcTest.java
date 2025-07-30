package ru.hogwarts.school_;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school_.controller.FacultyController;
import ru.hogwarts.school_.model.Faculty;
import ru.hogwarts.school_.service.FacultyService;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
@Import(FacultyControllerWebMvcTest.TestConfig.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FacultyService facultyService; // мокируемый бин

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public FacultyService facultyService() {
            return Mockito.mock(FacultyService.class);
        }
    }

    @Test
    void testGetFacultyById() throws Exception {
        Faculty faculty = Faculty.builder()
                .id(1L)
                .name("Physics")
                .color("Blue")
                .build();

        Mockito.when(facultyService.getFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Physics"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }

    @Test
    @DisplayName("POST /faculty - success")
    void createFacultyTest() throws Exception {
        Faculty input = Faculty.builder()
                .name("Слизерин")
                .color("Зеленый")
                .build();

        Faculty saved = Faculty.builder()
                .id(2L)
                .name("Слизерин")
                .color("Зеленый")
                .build();

        Mockito.when(facultyService.createFaculty(Mockito.any(Faculty.class))).thenReturn(saved);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value(saved.getName()))
                .andExpect(jsonPath("$.color").value(saved.getColor()));
    }

    @Test
    @DisplayName("PUT /faculty - success")
    void editFacultyTest() throws Exception {
        Faculty faculty = Faculty.builder()
                .id(1L)
                .name("Когтевран")
                .color("Синий")
                .build();

        Mockito.when(facultyService.updateFaculty(Mockito.anyLong(), Mockito.any(Faculty.class)))
                .thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    @DisplayName("DELETE /faculty/{id} - success")
    void deleteFacultyTest() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());

        Mockito.verify(facultyService).deleteFaculty(1L);
    }
}
