package nl.novi.vinylshop.repositories;

import nl.novi.vinylshop.entities.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {
//    List<AlbumEntity> findByReleaseYear(int releaseYear);
//    List<AlbumEntity> findByTitleContainingIgnoreCase(String title);

    List<AlbumEntity> findByStockItemsNotEmpty();
    List<AlbumEntity> findByStockItemsEmpty();
    List<AlbumEntity> findByGenre_Id(Long genreId);

}


