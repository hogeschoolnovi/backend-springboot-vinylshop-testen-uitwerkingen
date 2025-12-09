package nl.novi.vinylshop.mappers;


import nl.novi.vinylshop.dtos.artist.ArtistRequestDTO;
import nl.novi.vinylshop.dtos.artist.ArtistResponseDTO;
import nl.novi.vinylshop.entities.ArtistEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArtistDTOMapper implements DTOMapper<ArtistResponseDTO, ArtistRequestDTO, ArtistEntity>{

    @Override
    public ArtistResponseDTO mapToDto(ArtistEntity model) {
        var result = new ArtistResponseDTO();
        result.setId(model.getId());
        result.setName(model.getName());
        result.setBiography(model.getBiography());
        return result;
    }

    @Override
    public List<ArtistResponseDTO> mapToDto(List<ArtistEntity> models) {
        return models.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ArtistEntity mapToEntity(ArtistRequestDTO requestDTO) {
        var model = new ArtistEntity();
        model.setName(requestDTO.getName());
        model.setBiography(requestDTO.getBiography());
        return model;
    }
}