package nl.novi.vinylshop.services;


import jakarta.persistence.EntityNotFoundException;
import nl.novi.vinylshop.dtos.album.AlbumExtendedResponseDTO;
import nl.novi.vinylshop.dtos.album.AlbumRequestDTO;
import nl.novi.vinylshop.dtos.album.AlbumResponseDTO;
import nl.novi.vinylshop.entities.*;
import nl.novi.vinylshop.mappers.AlbumDTOMapper;
import nl.novi.vinylshop.mappers.AlbumExtendedDTOMapper;
import nl.novi.vinylshop.repositories.AlbumRepository;
import nl.novi.vinylshop.repositories.ArtistRepository;
import nl.novi.vinylshop.repositories.GenreRepository;
import nl.novi.vinylshop.repositories.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumDTOMapper albumDTOMapper;
    private final AlbumExtendedDTOMapper albumExtendedDTOMapper;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;

    public AlbumService(AlbumRepository albumRepository,
                        ArtistRepository artistRepository,
                        AlbumDTOMapper albumDTOMapper, AlbumExtendedDTOMapper albumExtendedDTOMapper,
                        PublisherRepository publisherRepository,
                        GenreRepository genreRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.albumDTOMapper = albumDTOMapper;
        this.albumExtendedDTOMapper = albumExtendedDTOMapper;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> findAllAlbums() {
        return albumDTOMapper.mapToDto(albumRepository.findAll());
    }

    @Transactional(readOnly = true)
    public AlbumExtendedResponseDTO findAlbumById(Long id) throws EntityNotFoundException {
        AlbumEntity albumEntity = getAlbumEntity(id);
        return albumExtendedDTOMapper.mapToDto(albumEntity);
    }

    @Transactional
    public AlbumResponseDTO createAlbum(AlbumRequestDTO albumModel) {
        AlbumEntity albumEntity = albumDTOMapper.mapToEntity(albumModel);
        if (albumModel.getGenreId() != null) {
            albumEntity.setGenre(getGenreEntity(albumModel.getGenreId()));
        } else {
            albumEntity.setGenre(new GenreEntity());
        }

        if(albumModel.getPublisherId() != null){
            albumEntity.setPublisher(getPublisherEntity(albumModel.getPublisherId()));
        } else {
            albumEntity.setPublisher(new PublisherEntity());
        }

        albumEntity = albumRepository.save(albumEntity);
        return albumDTOMapper.mapToDto(albumEntity);
    }

    @Transactional
    public AlbumResponseDTO updateAlbum(Long id, AlbumRequestDTO albumModel) throws EntityNotFoundException {
        AlbumEntity existingAlbumEntity = getAlbumEntity(id);

        existingAlbumEntity.setTitle(albumModel.getTitle());
        existingAlbumEntity.setReleaseYear(albumModel.getReleaseYear());
        existingAlbumEntity.setPublisher(getPublisherEntity(albumModel.getPublisherId()));
        existingAlbumEntity.setGenre(getGenreEntity(albumModel.getGenreId()));
        existingAlbumEntity = albumRepository.save(existingAlbumEntity);
        return albumDTOMapper.mapToDto(existingAlbumEntity);
    }

    private PublisherEntity getPublisherEntity(long publisherId) {
        return publisherRepository.findById(publisherId).orElseThrow(() -> new EntityNotFoundException("publisher " + publisherId + " not found"));
    }

    private GenreEntity getGenreEntity(long genreId) {
        return genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException("genre " + genreId + " not found"));
    }

    private AlbumEntity getAlbumEntity(Long id) {
        AlbumEntity existingAlbumEntity = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album " + id + " not found"));
        return existingAlbumEntity;
    }

    @Transactional
    public void deleteAlbum(Long id) {
        AlbumEntity album = getAlbumEntity(id);
//        Verwijder geen albums als je daar nog voorraad van hebt
        if(album.getStockItems().isEmpty()) {
            albumRepository.deleteById(id);
        }
    }

    @Transactional
    public void linkArtist(Long albumId, Long artistId) {
        AlbumEntity existingAlbumEntity = getAlbumEntity(albumId);
        ArtistEntity existingArtistEntity = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist " + artistId + " not found"));
        existingArtistEntity.getAlbums().add(existingAlbumEntity);
        existingAlbumEntity.getArtists().add(existingArtistEntity);
        albumRepository.save(existingAlbumEntity);
    }

    @Transactional
    public void unlinkArtist(Long albumId, Long artistId) {
        AlbumEntity existingAlbumEntity = getAlbumEntity(albumId);
        ArtistEntity existingArtistEntity = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist " + artistId + " not found"));
        existingArtistEntity.getAlbums().remove(existingAlbumEntity);
        existingAlbumEntity.getArtists().remove(existingArtistEntity);
        albumRepository.save(existingAlbumEntity);
    }

    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> getAlbumsWithStock(Boolean stock) {
        if(stock) {
            return albumDTOMapper.mapToDto(albumRepository.findByStockItemsNotEmpty());
        } else {
            return albumDTOMapper.mapToDto(albumRepository.findByStockItemsEmpty());
        }
    }
}

