package nl.novi.vinylshop.services;


import nl.novi.vinylshop.dtos.publisher.PublisherRequestDTO;
import nl.novi.vinylshop.dtos.publisher.PublisherResponseDTO;
import nl.novi.vinylshop.entities.AlbumEntity;
import nl.novi.vinylshop.entities.PublisherEntity;
import nl.novi.vinylshop.exceptions.RecordNotFoundException;
import nl.novi.vinylshop.mappers.PublisherDTOMapper;
import nl.novi.vinylshop.repositories.AlbumRepository;
import nl.novi.vinylshop.repositories.PublisherRepository;
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
class PublisherServiceTests {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private PublisherDTOMapper publisherDTOMapper;

    @InjectMocks
    private PublisherService publisherService;

    @Test
    void findAllPublishers_shouldReturnListOfPublisherResponseDTOs() {
        // Arrange
        PublisherResponseDTO publisherdto1 = new PublisherResponseDTO();
        publisherdto1.setName("Updated Name");
        publisherdto1.setAddress("Updated Address");
        publisherdto1.setContactDetails("Updated Contact");

        PublisherResponseDTO publisherdto2 = new PublisherResponseDTO();
        publisherdto2.setName("Updated Name");
        publisherdto2.setAddress("Updated Address");
        publisherdto2.setContactDetails("Updated Contact");

        PublisherEntity publisherEntity1 = new PublisherEntity();
        publisherEntity1.setName("Old Name");
        publisherEntity1.setAddress("Old Address");
        publisherEntity1.setContactDetails("Old Contact");

        PublisherEntity publisherEntity2 = new PublisherEntity();
        publisherEntity2.setName("Old Name");
        publisherEntity2.setAddress("Old Address");
        publisherEntity2.setContactDetails("Old Contact");


        when(publisherRepository.findAll()).thenReturn(Arrays.asList(publisherEntity1, publisherEntity2));
        when(publisherDTOMapper.mapToDto(List.of(publisherEntity1, publisherEntity2))).thenReturn(List.of(publisherdto1, publisherdto2));

        // Act
        List<PublisherResponseDTO> result = publisherService.findAllPublishers();

        // Assert
        assertEquals(2, result.size());
        assertEquals(publisherdto1, result.get(0));
        assertEquals(publisherdto2, result.get(1));
        verify(publisherRepository, times(1)).findAll();
        verify(publisherDTOMapper, times(1)).mapToDto(List.of(publisherEntity1, publisherEntity2));
    }

    @Test
    void findPublisherById_shouldReturnPublisherResponseDTO_whenPublisherExists() {
        // Arrange
        Long id = 1L;
        PublisherResponseDTO publisherResponseDTO = new PublisherResponseDTO();
        publisherResponseDTO.setName("Updated Name");
        publisherResponseDTO.setAddress("Updated Address");
        publisherResponseDTO.setContactDetails("Updated Contact");

        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setName("Old Name");
        publisherEntity.setAddress("Old Address");
        publisherEntity.setContactDetails("Old Contact");


        when(publisherRepository.findById(id)).thenReturn(Optional.of(publisherEntity));
        when(publisherDTOMapper.mapToDto(publisherEntity)).thenReturn(publisherResponseDTO);

        // Act
        PublisherResponseDTO result = publisherService.findPublisherById(id);

        // Assert
        assertEquals(publisherResponseDTO, result);
        verify(publisherRepository, times(1)).findById(id);
        verify(publisherDTOMapper, times(1)).mapToDto(publisherEntity);
    }

    @Test
    void findPublisherById_shouldThrowEntityNotFoundException_whenPublisherDoesNotExist() {
        // Arrange
        Long id = 1L;

        when(publisherRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            publisherService.findPublisherById(id);
        });
        assertEquals("Publisher " + id + " not found", exception.getMessage());
        verify(publisherRepository, times(1)).findById(id);
    }

    @Test
    void createPublisher_shouldReturnCreatedPublisherResponseDTO() {
        // Arrange
        PublisherResponseDTO publisherResponseDTO = new PublisherResponseDTO();
        publisherResponseDTO.setName("Updated Name");
        publisherResponseDTO.setAddress("Updated Address");
        publisherResponseDTO.setContactDetails("Updated Contact");

        PublisherRequestDTO publisherRequestDTO = new PublisherRequestDTO();
        publisherRequestDTO.setName("Updated Name");
        publisherRequestDTO.setAddress("Updated Address");
        publisherRequestDTO.setContactDetails("Updated Contact");

        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setName("Old Name");
        publisherEntity.setAddress("Old Address");
        publisherEntity.setContactDetails("Old Contact");


        when(publisherDTOMapper.mapToEntity(publisherRequestDTO)).thenReturn(publisherEntity);
        when(publisherRepository.save(publisherEntity)).thenReturn(publisherEntity);
        when(publisherDTOMapper.mapToDto(publisherEntity)).thenReturn(publisherResponseDTO);

        // Act
        PublisherResponseDTO result = publisherService.createPublisher(publisherRequestDTO);

        // Assert
        assertEquals(publisherResponseDTO, result);
        verify(publisherDTOMapper, times(1)).mapToEntity(publisherRequestDTO);
        verify(publisherRepository, times(1)).save(publisherEntity);
        verify(publisherDTOMapper, times(1)).mapToDto(publisherEntity);
    }

    @Test
    void updatePublisher_shouldReturnUpdatedPublisherResponseDTO_whenPublisherExists() {
        // Arrange
        Long id = 1L;
        PublisherResponseDTO publisherResponseDTO = new PublisherResponseDTO();
        publisherResponseDTO.setName("Updated Name");
        publisherResponseDTO.setAddress("Updated Address");
        publisherResponseDTO.setContactDetails("Updated Contact");

        PublisherRequestDTO publisherRequestDTO = new PublisherRequestDTO();
        publisherRequestDTO.setName("Updated Name");
        publisherRequestDTO.setAddress("Updated Address");
        publisherRequestDTO.setContactDetails("Updated Contact");

        PublisherEntity existingPublisherEntity = new PublisherEntity();
        existingPublisherEntity.setName("Old Name");
        existingPublisherEntity.setAddress("Old Address");
        existingPublisherEntity.setContactDetails("Old Contact");

        when(publisherRepository.findById(id)).thenReturn(Optional.of(existingPublisherEntity));
        when(publisherRepository.save(existingPublisherEntity)).thenReturn(existingPublisherEntity);
        when(publisherDTOMapper.mapToDto(existingPublisherEntity)).thenReturn(publisherResponseDTO);

        // Act
        PublisherResponseDTO result = publisherService.updatePublisher(id, publisherRequestDTO);

        // Assert
        assertEquals(publisherResponseDTO, result);
        assertEquals("Updated Name", existingPublisherEntity.getName());
        assertEquals("Updated Address", existingPublisherEntity.getAddress());
        assertEquals("Updated Contact", existingPublisherEntity.getContactDetails());
        verify(publisherRepository, times(1)).findById(id);
        verify(publisherRepository, times(1)).save(existingPublisherEntity);
        verify(publisherDTOMapper, times(1)).mapToDto(existingPublisherEntity);
    }

    @Test
    void updatePublisher_shouldThrowEntityNotFoundException_whenPublisherDoesNotExist() {
        // Arrange
        Long id = 1L;
        PublisherResponseDTO publisherResponseDTO = new PublisherResponseDTO();
        publisherResponseDTO.setName("Updated Name");
        publisherResponseDTO.setAddress("Updated Address");
        publisherResponseDTO.setContactDetails("Updated Contact");

        PublisherRequestDTO publisherRequestDTO = new PublisherRequestDTO();
        publisherRequestDTO.setName("Updated Name");
        publisherRequestDTO.setAddress("Updated Address");
        publisherRequestDTO.setContactDetails("Updated Contact");

        when(publisherRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            publisherService.updatePublisher(id, publisherRequestDTO);
        });
        assertEquals("Publisher " + id + " not found", exception.getMessage());
        verify(publisherRepository, times(1)).findById(id);
    }

    @Test
    void deletePublisher_withoutAlbums_shouldCallDeleteById() {
        // Arrange
        Long id = 1L;

        PublisherEntity existingPublisherEntity = new PublisherEntity();
        existingPublisherEntity.setName("Name");
        existingPublisherEntity.setAddress("Address");
        existingPublisherEntity.setContactDetails("Contact");


        when(publisherRepository.findById(id)).thenReturn(Optional.of(existingPublisherEntity));

        // Act
        publisherService.deletePublisher(id);

        // Assert
        verify(publisherRepository, times(1)).deleteById(id);
    }

    @Test
    void deletePublisher_withAlbums_shoudlCall(){
        // Arrange
        Long id = 1L;
        AlbumEntity album1 = new AlbumEntity();
        AlbumEntity album2 = new AlbumEntity();
        AlbumEntity album3 = new AlbumEntity();
        ArrayList<AlbumEntity> albums = new ArrayList<>(List.of(album1, album2, album3));

        PublisherEntity existingPublisherEntity = new PublisherEntity();
        existingPublisherEntity.setName("Name");
        existingPublisherEntity.setAddress("Address");
        existingPublisherEntity.setContactDetails("Contact");
        existingPublisherEntity.setAlbums(albums);

        when(publisherRepository.findById(id)).thenReturn(Optional.of(existingPublisherEntity));
        when(albumRepository.save(any())).thenReturn(new AlbumEntity());

        // Act
        publisherService.deletePublisher(id);

        // Assert
        verify(publisherRepository, times(1)).deleteById(id);
    }
}
