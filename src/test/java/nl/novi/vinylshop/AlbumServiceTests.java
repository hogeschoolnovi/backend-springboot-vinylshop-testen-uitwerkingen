package nl.novi.vinylshop;

import nl.novi.vinylshop.dtos.album.AlbumExtendedResponseDTO;
import nl.novi.vinylshop.dtos.album.AlbumRequestDTO;
import nl.novi.vinylshop.dtos.album.AlbumResponseDTO;
import nl.novi.vinylshop.entities.*;
import nl.novi.vinylshop.exceptions.RecordNotFoundException;
import nl.novi.vinylshop.mappers.AlbumDTOMapper;
import nl.novi.vinylshop.mappers.AlbumExtendedDTOMapper;
import nl.novi.vinylshop.repositories.*;

import nl.novi.vinylshop.services.AlbumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTests {

    private AlbumRepository albumRepository;

    private ArtistRepository artistRepository;

    private PublisherRepository publisherRepository;

    private GenreRepository genreRepository;

    private AlbumDTOMapper albumDTOMapper;

    private AlbumExtendedDTOMapper albumExtendedDTOMapper;

    private AlbumService albumService;

    private AlbumEntity albumEntity;
    private AlbumRequestDTO requestDTO;
    private AlbumResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        albumDTOMapper = mock(AlbumDTOMapper.class);
        albumExtendedDTOMapper = mock(AlbumExtendedDTOMapper.class);
        genreRepository = mock(GenreRepository.class);
        publisherRepository = mock(PublisherRepository.class);
        artistRepository = mock(ArtistRepository.class);
        albumRepository = mock(AlbumRepository.class);

        albumService = new AlbumService(
                albumRepository,
                artistRepository,
                albumDTOMapper,
                albumExtendedDTOMapper,
                publisherRepository,
                genreRepository
        );

        albumEntity = new AlbumEntity();
        albumEntity.setId(1L);
        albumEntity.setTitle("Test Album");
        albumEntity.setReleaseYear(1999);
        albumEntity.setArtists(new HashSet<>());
        albumEntity.setStockItems(new ArrayList<>());

        requestDTO = new AlbumRequestDTO();
        requestDTO.setTitle("Test Album");
        requestDTO.setReleaseYear(1999);
        requestDTO.setGenreId(1L);
        requestDTO.setPublisherId(1L);

        responseDTO = new AlbumResponseDTO();
        responseDTO.setTitle("Test Album");
        responseDTO.setReleaseYear(1999);
    }

    @Test
    void findAllAlbums_shouldReturnListOfDTOs() {
        when(albumRepository.findAll()).thenReturn(List.of(albumEntity));
        when(albumDTOMapper.mapToDto(List.of(albumEntity))).thenReturn(List.of(responseDTO));

        List<AlbumResponseDTO> result = albumService.findAllAlbums();

        assertEquals(1, List.of(responseDTO).size());
        assertEquals("Test Album", result.get(0).getTitle());
        verify(albumRepository, times(1)).findAll();
        verify(albumDTOMapper, times(1)).mapToDto(List.of(albumEntity));
    }

    @Test
    void findAlbumById_shouldReturnAlbumExtendedDTO() {
        AlbumExtendedResponseDTO extendedResponseDTO = new AlbumExtendedResponseDTO();
        extendedResponseDTO.setTitle("Test Album");

        when(albumRepository.findById(1L)).thenReturn(Optional.of(albumEntity));
        when(albumExtendedDTOMapper.mapToDto(albumEntity)).thenReturn(extendedResponseDTO);

        AlbumExtendedResponseDTO result = albumService.findAlbumById(1L);

        assertEquals("Test Album", result.getTitle());
        verify(albumRepository).findById(1L);
    }

    @Test
    void createAlbum_shouldReturnAlbumResponseDTO() {
        GenreEntity genre = new GenreEntity();
        PublisherEntity publisher = new PublisherEntity();


        when(albumDTOMapper.mapToEntity(requestDTO)).thenReturn(albumEntity);
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(albumRepository.save(albumEntity)).thenReturn(albumEntity);
        when(albumDTOMapper.mapToDto(albumEntity)).thenReturn(responseDTO);

        AlbumResponseDTO result = albumService.createAlbum(requestDTO);

        assertEquals("Test Album", result.getTitle());
        verify(albumRepository).save(albumEntity);
    }

    @Test
    void createAlbumWithoutGenreIdAndPublisherId_shouldReturnAlbumResponseDTO() {
        GenreEntity genre = new GenreEntity();
        PublisherEntity publisher = new PublisherEntity();
        requestDTO.setPublisherId(null);
        requestDTO.setGenreId(null);

        when(albumDTOMapper.mapToEntity(requestDTO)).thenReturn(albumEntity);
        when(albumRepository.save(albumEntity)).thenReturn(albumEntity);
        when(albumDTOMapper.mapToDto(albumEntity)).thenReturn(responseDTO);

        AlbumResponseDTO result = albumService.createAlbum(requestDTO);

        assertEquals("Test Album", result.getTitle());
        verify(albumRepository).save(albumEntity);
    }

    @Test
    void updateAlbum_shouldReturnUpdatedDTO() {
        GenreEntity genre = new GenreEntity();
        PublisherEntity publisher = new PublisherEntity();

        when(albumRepository.findById(1L)).thenReturn(Optional.of(albumEntity));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(albumRepository.save(albumEntity)).thenReturn(albumEntity);
        when(albumDTOMapper.mapToDto(albumEntity)).thenReturn(responseDTO);

        AlbumResponseDTO result = albumService.updateAlbum(1L, requestDTO);

        assertEquals("Test Album", result.getTitle());
        verify(albumRepository).save(albumEntity);
    }

    @Test
    void deleteAlbum_shouldDeleteWhenNoStock() {
        albumEntity.setStockItems(new ArrayList<>());

        when(albumRepository.findById(1L)).thenReturn(Optional.of(albumEntity));

        albumService.deleteAlbum(1L);

        verify(albumRepository).deleteById(1L);
    }

    @Test
    void deleteAlbum_shouldNotDeleteWhenStockExists() {
        albumEntity.setStockItems(List.of(new StockEntity()));

        when(albumRepository.findById(1L)).thenReturn(Optional.of(albumEntity));

        albumService.deleteAlbum(1L);

        verify(albumRepository, never()).deleteById(1L);
    }

    @Test
    void linkArtist_shouldAddArtistToAlbum() {
        ArtistEntity artist = new ArtistEntity();
        artist.setAlbums(new HashSet<>());

        albumEntity.setArtists(new HashSet<>());

        when(albumRepository.findById(1L)).thenReturn(Optional.of(albumEntity));
        when(artistRepository.findById(2L)).thenReturn(Optional.of(artist));
        when(albumRepository.save(albumEntity)).thenReturn(albumEntity);

        albumService.linkArtist(1L, 2L);

        assertTrue(albumEntity.getArtists().contains(artist));
        assertTrue(artist.getAlbums().contains(albumEntity));
    }

    @Test
    void unlinkArtist_shouldRemoveArtistFromAlbum() {
        ArtistEntity artist = new ArtistEntity();
        artist.setAlbums(new HashSet<>(Set.of(albumEntity)));

        albumEntity.setArtists(new HashSet<>(Set.of(artist)));

        when(albumRepository.findById(1L)).thenReturn(Optional.of(albumEntity));
        when(artistRepository.findById(2L)).thenReturn(Optional.of(artist));
        when(albumRepository.save(albumEntity)).thenReturn(albumEntity);

        albumService.unlinkArtist(1L, 2L);

        assertFalse(albumEntity.getArtists().contains(artist));
        assertFalse(artist.getAlbums().contains(albumEntity));
    }

    @Test
    void getAlbumsWithStock_shouldReturnAlbumsWithStock() {
        when(albumRepository.findByStockItemsNotEmpty()).thenReturn(List.of(albumEntity));
        when(albumDTOMapper.mapToDto(List.of(albumEntity))).thenReturn(List.of(responseDTO));

        List<AlbumResponseDTO> result = albumService.getAlbumsWithStock(true);

        assertEquals(1, result.size());
        verify(albumRepository).findByStockItemsNotEmpty();
    }

    @Test
    void getAlbumsWithStock_shouldReturnAlbumsWithoutStock() {
        when(albumRepository.findByStockItemsEmpty()).thenReturn(List.of(albumEntity));
        when(albumDTOMapper.mapToDto(List.of(albumEntity))).thenReturn(List.of(responseDTO));

        List<AlbumResponseDTO> result = albumService.getAlbumsWithStock(false);

        assertEquals(1, result.size());
        verify(albumRepository).findByStockItemsEmpty();
    }

    @Test
    void getAlbumEntity_shouldThrowException_WhenNotFound() {
        when(albumRepository.findById(100L)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () ->
                albumService.findAlbumById(100L));

        assertEquals("Album 100 not found", exception.getMessage());
    }

    @Test
    void createAlbum_shouldThrowException_WhenGenreNotFound() {
        when(genreRepository.findById(1L)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () ->
                albumService.createAlbum(requestDTO));

        assertTrue(exception.getMessage().contains("genre"));
    }

    @Test
    void createAlbum_shouldThrowException_WhenPublisherNotFound() {
        when(albumDTOMapper.mapToEntity(requestDTO)).thenReturn(albumEntity);
        when(genreRepository.findById(1L)).thenReturn(Optional.of(new GenreEntity()));
        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () ->
                albumService.createAlbum(requestDTO));

        assertTrue(exception.getMessage().contains("publisher"));
    }
}