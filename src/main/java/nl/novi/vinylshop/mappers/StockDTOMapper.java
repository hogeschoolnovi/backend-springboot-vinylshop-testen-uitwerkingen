package nl.novi.vinylshop.mappers;

import nl.novi.vinylshop.dtos.stock.StockRequestDTO;
import nl.novi.vinylshop.dtos.stock.StockResponseDTO;
import nl.novi.vinylshop.entities.StockEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockDTOMapper implements DTOMapper<StockResponseDTO, StockRequestDTO, StockEntity>{

    @Override
    public StockResponseDTO mapToDto(StockEntity model) {
        var result = new StockResponseDTO();
        result.setId(model.getId());
        result.setCondition(model.getCondition());
        result.setPrice(model.getPrice());
        return result;
    }


    @Override
    public List<StockResponseDTO> mapToDto(List<StockEntity> models) {
        if(models == null) { return new ArrayList<StockResponseDTO>();}
        return models.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public StockEntity mapToEntity(StockRequestDTO requestDTO) {
        var model = new StockEntity();
        model.setCondition(requestDTO.getCondition());
        model.setPrice(requestDTO.getPrice());
        return model;
    }
}
