package nl.novi.vinylshop.services;


import nl.novi.vinylshop.dtos.stock.StockRequestDTO;
import nl.novi.vinylshop.dtos.stock.StockResponseDTO;
import nl.novi.vinylshop.entities.AlbumEntity;
import nl.novi.vinylshop.entities.StockEntity;
import nl.novi.vinylshop.exceptions.RecordNotFoundException;
import nl.novi.vinylshop.mappers.StockDTOMapper;
import nl.novi.vinylshop.repositories.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockDTOMapper stockDTOMapper;

    public StockService(StockRepository stockRepository,
                        StockDTOMapper stockDTOMapper) {
        this.stockRepository = stockRepository;
        this.stockDTOMapper = stockDTOMapper;
    }

    @Transactional(readOnly = true)
    public List<StockResponseDTO> findAllStocks() {
        return stockDTOMapper.mapToDto(stockRepository.findAll());
    }

    @Transactional(readOnly = true)
    public StockResponseDTO findStockById(Long id) {
        StockEntity stockEntity = stockRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Stock " + id + " not found"));
        return stockDTOMapper.mapToDto(stockEntity);
    }

    @Transactional
    public void deleteStock(Long albumId, Long id) {
        stockRepository.deleteByIdAndAlbumId(id, albumId);
    }

    @Transactional
    //nodig om fouten te voorkomen mocht door cascading meerdere entiteiten opgeslagen worden samen met deze entiteit.
    public StockResponseDTO createStock(Long albumId, StockRequestDTO stockDto) {
        StockEntity stockEntity = stockDTOMapper.mapToEntity(stockDto);
        stockEntity.setAlbum(new AlbumEntity(albumId));
        stockEntity = stockRepository.save(stockEntity);
        return stockDTOMapper.mapToDto(stockEntity);
    }

    @Transactional
    public StockResponseDTO updateStock(Long albumId, Long id, StockRequestDTO stockDto) {
        StockEntity stockEntity = getStockEntity(albumId, id);
        stockEntity.setCondition(stockDto.getCondition());
        stockEntity.setPrice(stockDto.getPrice());
        stockEntity = stockRepository.save(stockEntity);
        return stockDTOMapper.mapToDto(stockEntity);
    }

    private StockEntity getStockEntity(Long albumId, Long id) {
        StockEntity stockEntity = getByIdAndAlbumId(albumId, id)
                .orElseThrow(() -> new RecordNotFoundException("Stock " + id + " not found"));
        return stockEntity;
    }

    @Transactional
    public StockResponseDTO findStock(Long albumId, Long id) {
        StockEntity stockEntity = getByIdAndAlbumId(albumId, id)
                .orElseThrow(() -> new RecordNotFoundException("Stock " + id + " not found in album " + albumId));
        return stockDTOMapper.mapToDto(stockEntity);
    }

    private Optional<StockEntity> getByIdAndAlbumId(Long albumId, Long id) {
        return stockRepository.findByIdAndAlbumId(id, albumId);
    }

    public List<StockResponseDTO> findStock(Long albumId) {
        var stocks = stockRepository.findByAlbumId(albumId);
        return stockDTOMapper.mapToDto(stocks);
    }
}

