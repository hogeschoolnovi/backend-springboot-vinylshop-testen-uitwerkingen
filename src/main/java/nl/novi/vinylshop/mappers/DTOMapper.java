package nl.novi.vinylshop.mappers;

import nl.novi.vinylshop.entities.BaseEntity;


import java.util.List;
import java.util.function.Supplier;

public interface DTOMapper<RESPONSE, REQUEST , ENTITY extends BaseEntity> {
    RESPONSE mapToDto(ENTITY model);

    List<RESPONSE> mapToDto(List<ENTITY> models);

    ENTITY mapToEntity(REQUEST genreModel);
}
