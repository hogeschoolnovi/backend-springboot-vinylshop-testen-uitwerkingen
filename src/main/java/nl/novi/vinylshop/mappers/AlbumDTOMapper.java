package nl.novi.vinylshop.mappers;


import nl.novi.vinylshop.dtos.album.AlbumRequestDTO;
import nl.novi.vinylshop.dtos.album.AlbumResponseDTO;
import nl.novi.vinylshop.entities.AlbumEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlbumDTOMapper implements DTOMapper<AlbumResponseDTO, AlbumRequestDTO, AlbumEntity> {

    private final PublisherDTOMapper publisherDtoMapper;
    private final GenreDTOMapper genreDTOMapper;

    public AlbumDTOMapper(PublisherDTOMapper publisherDtoMapper, GenreDTOMapper genreDTOMapper) {
        this.publisherDtoMapper = publisherDtoMapper;
        this.genreDTOMapper = genreDTOMapper;
    }

    @Override
    public AlbumResponseDTO mapToDto(AlbumEntity model) {
        return mapToDto(model, new AlbumResponseDTO());
    }

    public <D extends AlbumResponseDTO> D mapToDto(AlbumEntity model, D target) {
        target.setId(model.getId());
        target.setTitle(model.getTitle());
        target.setReleaseYear(model.getReleaseYear());
        if(model.getGenre() != null){
            target.setGenre(genreDTOMapper.mapToDto(model.getGenre()));
        }
        if(model.getPublisher() != null) {
            target.setPublisher(publisherDtoMapper.mapToDto(model.getPublisher()));
        }

        return target;
    }


    @Override
    public List<AlbumResponseDTO> mapToDto(List<AlbumEntity> models) {
        return models.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public AlbumEntity mapToEntity(AlbumRequestDTO requestDTO) {
        var model = new AlbumEntity();
        model.setTitle(requestDTO.getTitle());
        model.setReleaseYear(requestDTO.getReleaseYear());
//        Deze twee voeg je in de service toe:
//        model.setGenre(new GenreModel());
//        model.setPublisher(new PublisherModel());
        return model;
    }
}
