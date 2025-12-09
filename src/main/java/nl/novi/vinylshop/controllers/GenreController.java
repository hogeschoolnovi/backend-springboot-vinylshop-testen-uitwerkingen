package nl.novi.vinylshop.controllers;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import nl.novi.vinylshop.dtos.genre.GenreRequestDTO;
import nl.novi.vinylshop.dtos.genre.GenreResponseDTO;
import nl.novi.vinylshop.helpers.UrlHelper;
import nl.novi.vinylshop.mappers.GenreDTOMapper;
import nl.novi.vinylshop.services.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//GET /genres - Haalt een lijst van alle genres op.
//GET /genres/{id} - Haalt een specifiek genre op basis van ID op.
//POST /genres - Creëert een nieuw genre.
//PUT /genres/{id} - Werkt een bestaand genre bij.
//DELETE /genres/{id} - Verwijdert een genre.


@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;
    private final UrlHelper urlHelper;


    public GenreController(GenreService genreService, UrlHelper urlHelper) {
        this.genreService = genreService;
        this.urlHelper = urlHelper;

    }

    @GetMapping
    public ResponseEntity<List<GenreResponseDTO>> getAllGenres() {
        List<GenreResponseDTO> genres = genreService.findAllGenres();
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> getGenreById(@PathVariable Long id) throws EntityNotFoundException {
        GenreResponseDTO genre = genreService.findGenreById(id);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GenreResponseDTO> createGenre(@RequestBody @Valid GenreRequestDTO genreModel) {
        GenreResponseDTO newGenre = genreService.createGenre(genreModel);
        return ResponseEntity.created(urlHelper.getCurrentUrlWithId(newGenre.getId())).body(newGenre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponseDTO> updateGenre(@PathVariable Long id, @RequestBody @Valid GenreRequestDTO genreModel) throws EntityNotFoundException {
        GenreResponseDTO updatedGenre = genreService.updateGenre(id, genreModel);;
        return new ResponseEntity<>(updatedGenre, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


