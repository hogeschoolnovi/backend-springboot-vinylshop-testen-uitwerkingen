package nl.novi.vinylshop.mappers;


import nl.novi.vinylshop.dtos.album.AlbumExtendedResponseDTO;
import nl.novi.vinylshop.entities.AlbumEntity;
import org.springframework.stereotype.Component;


@Component
public class AlbumExtendedDTOMapper extends AlbumDTOMapper {

    private final StockDTOMapper stockDTOMapper;

    public AlbumExtendedDTOMapper(PublisherDTOMapper publisherDtoMapper , StockDTOMapper stockDTOMapper, GenreDTOMapper genreDTOMapper) {
        super(publisherDtoMapper,genreDTOMapper);
        this.stockDTOMapper = stockDTOMapper;
    }

    @Override
    public AlbumExtendedResponseDTO mapToDto(AlbumEntity model) {
        AlbumExtendedResponseDTO result = (AlbumExtendedResponseDTO) super.mapToDto(model);
        result.setStock(stockDTOMapper.mapToDto(model.getStockItems()));
        return result;
    }
}
