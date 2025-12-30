package nl.novi.vinylshop;


import nl.novi.vinylshop.dtos.genre.GenreRequestDTO;
import nl.novi.vinylshop.dtos.genre.GenreResponseDTO;
import nl.novi.vinylshop.entities.AlbumEntity;
import nl.novi.vinylshop.entities.GenreEntity;
import nl.novi.vinylshop.exceptions.RecordNotFoundException;
import nl.novi.vinylshop.mappers.GenreDTOMapper;
import nl.novi.vinylshop.repositories.AlbumRepository;
import nl.novi.vinylshop.repositories.GenreRepository;
import nl.novi.vinylshop.services.GenreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTests {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private GenreDTOMapper genreMapper;

    @InjectMocks
    private GenreService genreService;

    @Test
    void findAllGenres_shouldReturnListOfGenreResponseDTOs() {
        // Arrange
        GenreEntity genreEntity1 = new GenreEntity();
        genreEntity1.setName("test 1");
        genreEntity1.setDescription("test 1 description");
        GenreEntity genreEntity2 = new GenreEntity();
        genreEntity2.setName("test 2");
        genreEntity2.setDescription("test 2 description");
        GenreResponseDTO genreResponseDTO1 = new GenreResponseDTO();
        genreResponseDTO1.setName("test 1");
        genreResponseDTO1.setDescription("test 1 description");
        GenreResponseDTO genreResponseDTO2 = new GenreResponseDTO();
        genreResponseDTO2.setName("test 2");
        genreResponseDTO2.setDescription("test 2 description");

        when(genreRepository.findAll()).thenReturn(Arrays.asList(genreEntity1, genreEntity2));
        when(genreMapper.mapToDto(List.of(genreEntity1, genreEntity2))).thenReturn(List.of(genreResponseDTO1, genreResponseDTO2));

        // Act
        List<GenreResponseDTO> result = genreService.findAllGenres();

        // Assert
        assertEquals(2, result.size());
        assertEquals(genreResponseDTO1, result.get(0));
        assertEquals(genreResponseDTO2, result.get(1));
        verify(genreRepository, times(1)).findAll();
        verify(genreMapper, times(1)).mapToDto(List.of(genreEntity1, genreEntity2));
    }

    @Test
    void findGenreById_shouldReturnGenreResponseDTO_whenGenreExists() {
        // Arrange
        Long id = 1L;
        GenreResponseDTO genreResponseDTO = new GenreResponseDTO();
        genreResponseDTO.setName("Updated Name");
        genreResponseDTO.setDescription("Updated Description");

        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setName("Old Name");
        genreEntity.setDescription("Old Description");

        when(genreRepository.findById(id)).thenReturn(Optional.of(genreEntity));
        when(genreMapper.mapToDto(genreEntity)).thenReturn(genreResponseDTO);

        // Act
        GenreResponseDTO result = genreService.findGenreById(id);

        // Assert
        assertEquals(genreResponseDTO, result);
        verify(genreRepository, times(1)).findById(id);
        verify(genreMapper, times(1)).mapToDto(genreEntity);
    }

    @Test
    void findGenreById_shouldThrowEntityNotFoundException_whenGenreDoesNotExist() {
        // Arrange
        Long id = 1L;

        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            genreService.findGenreById(id);
        });
        assertEquals("Genre " + id + " not found", exception.getMessage());
        verify(genreRepository, times(1)).findById(id);
    }

    @Test
    void createGenre_shouldReturnCreatedGenreResponseDTO() {
        // Arrange

        GenreResponseDTO genreResponseDTO = new GenreResponseDTO();
        genreResponseDTO.setName("Updated Name");
        genreResponseDTO.setDescription("Updated Description");

        GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("updated Name");
        genreRequestDTO.setDescription("Updated Description");

        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setName("Old Name");
        genreEntity.setDescription("Old Description");

        when(genreMapper.mapToEntity(genreRequestDTO)).thenReturn(genreEntity);
        when(genreRepository.save(genreEntity)).thenReturn(genreEntity);
        when(genreMapper.mapToDto(genreEntity)).thenReturn(genreResponseDTO);

        // Act
        GenreResponseDTO result = genreService.createGenre(genreRequestDTO);

        // Assert
        assertEquals(genreResponseDTO, result);
        verify(genreMapper, times(1)).mapToEntity(genreRequestDTO);
        verify(genreRepository, times(1)).save(genreEntity);
        verify(genreMapper, times(1)).mapToDto(genreEntity);
    }

    @Test
    void updateGenre_shouldReturnUpdatedGenreResponseDTO_whenGenreExists() {
        // Arrange
        Long id = 1L;
        GenreResponseDTO genreResponseDTO = new GenreResponseDTO();
        genreResponseDTO.setName("Updated Name");
        genreResponseDTO.setDescription("Updated Description");

        GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("Updated Name");
        genreRequestDTO.setDescription("Updated Description");

        GenreEntity existingGenreEntity = new GenreEntity();
        existingGenreEntity.setName("Old Name");
        existingGenreEntity.setDescription("Old Description");

        when(genreRepository.findById(id)).thenReturn(Optional.of(existingGenreEntity));
        when(genreRepository.save(existingGenreEntity)).thenReturn(existingGenreEntity);
        when(genreMapper.mapToDto(existingGenreEntity)).thenReturn(genreResponseDTO);

        // Act
        GenreResponseDTO result = genreService.updateGenre(id, genreRequestDTO);

        // Assert
        assertEquals(genreResponseDTO, result);
        assertEquals("Updated Name", existingGenreEntity.getName());
        assertEquals("Updated Description", existingGenreEntity.getDescription());
        verify(genreRepository, times(1)).findById(id);
        verify(genreRepository, times(1)).save(existingGenreEntity);
        verify(genreMapper, times(1)).mapToDto(existingGenreEntity);
    }

    @Test
    void updateGenre_shouldThrowEntityNotFoundException_whenGenreDoesNotExist() {
        // Arrange
        Long id = 1L;
        GenreRequestDTO genreRequestDTO = new GenreRequestDTO();
        genreRequestDTO.setName("updated Name");
        genreRequestDTO.setDescription("Updated Description");

        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            genreService.updateGenre(id, genreRequestDTO);
        });
        assertEquals("Genre " + id + " not found", exception.getMessage());
        verify(genreRepository, times(1)).findById(id);
    }

    @Test
    void deleteGenre_withoutAlbums_shouldCallDeleteById() {
        // Arrange
        Long id = 1L;
        when(albumRepository.findByGenre_Id(id)).thenReturn(new ArrayList<>());

        // Act
        genreService.deleteGenre(id);

        // Assert
        verify(genreRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteGrente_withAlbums_shouldCallAlbumSave(){
        // Arrange
        Long id = 1L;
        GenreEntity genre = new GenreEntity();
        genre.setName("album genre");
        genre.setDescription("album description");

        AlbumEntity album1 = new AlbumEntity();
        AlbumEntity album2 = new AlbumEntity();
        AlbumEntity album3 = new AlbumEntity();
        ArrayList<AlbumEntity> albums = new ArrayList<>(List.of(album1, album2, album3));
        albums.forEach(albumEntity -> albumEntity.setGenre(genre));

        when(albumRepository.findByGenre_Id(id)).thenReturn(albums);
        when(albumRepository.save(any(AlbumEntity.class))).thenReturn(new AlbumEntity());

        // Act
        genreService.deleteGenre(id);

        // Assert
        verify(genreRepository, times(1)).deleteById(id);
        verify(albumRepository, times(3)).save(any());
    }
}

