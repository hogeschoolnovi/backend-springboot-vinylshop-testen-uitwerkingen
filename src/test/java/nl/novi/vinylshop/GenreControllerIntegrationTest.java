package nl.novi.vinylshop;


import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.vinylshop.dtos.genre.GenreRequestDTO;
import nl.novi.vinylshop.dtos.genre.GenreResponseDTO;
import nl.novi.vinylshop.entities.GenreEntity;
import nl.novi.vinylshop.repositories.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest()
public class GenreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        genreRepository.deleteAll();
    }

    @Test
    void getAllGenres_shouldReturnListOfGenres() throws Exception {
        // Create test data
        GenreEntity genre1 = new GenreEntity();
        genre1.setName("Rock");
        genre1.setDescription("Rock genre description");
        genreRepository.save(genre1);

        GenreEntity genre2 = new GenreEntity();
        genre2.setName("Jazz");
        genre2.setDescription("Jazz genre description");
        genreRepository.save(genre2);

        // Test the retrieval of genres
       mockMvc.perform(get("/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Rock"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Jazz"));

    }

    @Test
    void getGenreById_shouldReturnGenre_whenGenreExists() throws Exception {
        // Create test data
        GenreEntity genre = new GenreEntity();
        genre.setName("Rock");
        genre.setDescription("Rock genre description");
        Long genreId = genreRepository.save(genre).getId();

        // Test the retrieval of a specific genre by ID
        mockMvc.perform(get("/genres/{id}", genreId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rock"));
    }

    @Test
    void getGenreById_shouldReturnNotFound_whenGenreDoesNotExist() throws Exception {
        // Test retrieval of a non-existent genre by ID
        mockMvc.perform(get("/genres/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createGenre_shouldCreateAndReturnGenre() throws Exception {
        // Arrange
        GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("Blues");
        genreRequestDTO.setDescription("Blues description");
        GenreResponseDTO genreResponseDTO = new GenreResponseDTO();
        genreResponseDTO.setName("Blues");
        genreResponseDTO.setDescription("Blues description");

        // Act & Assert
       var response =mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name").value("Blues"))
                .andExpect(jsonPath("$.description").value("Blues description"))
               .andReturn(); // de andReturn is alleen nodig als je nog iets met de data wilt doen.

        // Optionele controle op de inhoud van de ontvangen dto
        GenreResponseDTO genreResult = objectMapper.readValue(response.getResponse().getContentAsString(), GenreResponseDTO.class);

//        Voor een "asserEquals" waarbij je het hele object vergelijkt, is het belangrijk om een "equals"-methode in je DTO te implementeren.
        Assertions.assertEquals(genreResponseDTO, genreResult);

//        Je kunt natuurlijk ook de losse velden uit het object vergelijken
        Assertions.assertEquals(genreResponseDTO.getDescription(), genreResult.getDescription());
        Assertions.assertEquals(genreResponseDTO.getName(), genreResult.getName());
    }

    @Test
    void updateGenre_shouldUpdateAndReturnGenre_whenGenreExists() throws Exception {
        // Create test data
        GenreEntity genre = new GenreEntity();
        genre.setName("Rock");
        genre.setDescription("Rock genre description");
        Long genreId = genreRepository.save(genre).getId();

        // Prepare request data for update
        GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("Updated Genre");
        genreRequestDTO.setDescription("Updated Description");

        // Test genre update
        mockMvc.perform(put("/genres/{id}", genreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Genre"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    void updateGenre_shouldReturnNotFound_whenGenreDoesNotExist() throws Exception {
        // Prepare request data for update
        GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("Nonexistent Genre");
        genreRequestDTO.setDescription("Nonexistent Description");

        // Test genre update for a non-existent ID
        mockMvc.perform(put("/genres/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGenre_shouldDeleteGenre_whenGenreExists() throws Exception {
        // Create test data
        GenreEntity genre = new GenreEntity();
        genre.setName("Rock");
        genre.setDescription("Rock genre description");
        Long genreId = genreRepository.save(genre).getId();

        // Test genre deletion
        mockMvc.perform(delete("/genres/{id}", genreId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify the genre was deleted
        mockMvc.perform(get("/genres/{id}", genreId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGenre_shouldReturnNotFound_whenGenreDoesNotExist() throws Exception {
        // Test genre deletion for a non-existent ID
        mockMvc.perform(delete("/genres/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}


