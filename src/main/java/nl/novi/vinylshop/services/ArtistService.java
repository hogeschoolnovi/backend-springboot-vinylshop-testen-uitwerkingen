package nl.novi.vinylshop.services;

import jakarta.persistence.EntityNotFoundException;
import nl.novi.vinylshop.dtos.artist.ArtistRequestDTO;
import nl.novi.vinylshop.dtos.artist.ArtistResponseDTO;
import nl.novi.vinylshop.entities.ArtistEntity;
import nl.novi.vinylshop.mappers.ArtistDTOMapper;
import nl.novi.vinylshop.repositories.ArtistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistDTOMapper artistDTOMapper;

    public ArtistService(ArtistRepository artistRepository, ArtistDTOMapper artistDTOMapper) {
        this.artistRepository = artistRepository;
        this.artistDTOMapper = artistDTOMapper;
    }

    @Transactional(readOnly = true)
    public List<ArtistResponseDTO> findAllArtists() {
        return artistDTOMapper.mapToDto(artistRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ArtistResponseDTO findArtistById(Long id) throws EntityNotFoundException {
        ArtistEntity artistEntity = getArtistEntity(id);
        return artistDTOMapper.mapToDto(artistEntity);
    }

    private ArtistEntity getArtistEntity(Long id) {
        ArtistEntity artistEntity = artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist " + id + " not found"));
        return artistEntity;
    }

    @Transactional
    public ArtistResponseDTO createArtist(ArtistRequestDTO artistModel) {
        ArtistEntity artistEntity = artistDTOMapper.mapToEntity(artistModel);
        artistEntity = artistRepository.save(artistEntity);
        return artistDTOMapper.mapToDto(artistEntity);
    }

    @Transactional
    public ArtistResponseDTO updateArtist(Long id, ArtistRequestDTO artistModel) throws EntityNotFoundException {
        ArtistEntity existingArtistEntity = getArtistEntity(id);

        existingArtistEntity.setName(artistModel.getName());
        existingArtistEntity.setBiography(artistModel.getBiography());

        existingArtistEntity = artistRepository.save(existingArtistEntity);
        return artistDTOMapper.mapToDto(existingArtistEntity);
    }

    @Transactional
    public void deleteArtist(Long id) {
        artistRepository.deleteById(id);
    }

    public List<ArtistResponseDTO> getArtistsForAlbum(Long albumId) {
        return artistDTOMapper.mapToDto(artistRepository.findArtistsByAlbumsId(albumId));
    }
}
