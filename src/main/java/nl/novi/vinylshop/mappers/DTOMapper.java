package nl.novi.vinylshop.mappers;

import nl.novi.vinylshop.entities.BaseEntity;

import java.util.List;

public interface DTOMapper<RESPONSE, REQUEST , ENTITY extends BaseEntity> {
    RESPONSE mapToDto(ENTITY model);

    List<RESPONSE> mapToDto(List<ENTITY> models);

    ENTITY mapToEntity(REQUEST genreModel);
}
