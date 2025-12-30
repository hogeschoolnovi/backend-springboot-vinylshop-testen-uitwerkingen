package nl.novi.vinylshop.controllers;


import nl.novi.vinylshop.dtos.album.AlbumExtendedResponseDTO;
import nl.novi.vinylshop.dtos.album.AlbumRequestDTO;
import nl.novi.vinylshop.dtos.album.AlbumResponseDTO;
import nl.novi.vinylshop.dtos.artist.ArtistResponseDTO;
import nl.novi.vinylshop.helpers.UrlHelper;
import nl.novi.vinylshop.services.AlbumService;
import nl.novi.vinylshop.services.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final ArtistService artistService;
    private final UrlHelper urlHelper;


    public AlbumController(AlbumService albumService, ArtistService artistService, UrlHelper urlHelper) {
        this.albumService = albumService;
        this.artistService = artistService;
        this.urlHelper = urlHelper;
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> getAllAlbums(@RequestParam(required = false) Boolean stock) {
        List<AlbumResponseDTO> albums;
        if(stock == null) {
            albums = albumService.findAllAlbums();
        } else {
            albums = albumService.getAlbumsWithStock(stock);
        }
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumExtendedResponseDTO> getAlbumById(@PathVariable Long id) {
        AlbumExtendedResponseDTO album = albumService.findAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDTO> createAlbum(@RequestBody AlbumRequestDTO albumRequestDTO) {;
        AlbumResponseDTO albumDTO = albumService.createAlbum(albumRequestDTO);
        return ResponseEntity.created(urlHelper.getCurrentUrlWithId(albumDTO.getId())).body(albumDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> updateAlbum(@PathVariable Long id, @RequestBody  AlbumRequestDTO albumRequestDTO) {
        AlbumResponseDTO albumDTO = albumService.updateAlbum(id, albumRequestDTO);
        return new ResponseEntity<>(albumDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{albumId}/artists/{artistId}")
    public ResponseEntity<Void> linkArtist(@PathVariable Long albumId, @PathVariable Long artistId) {
        albumService.linkArtist(albumId, artistId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{albumId}/artists/{artistId}")
    public ResponseEntity<Void> unlinkArtist(@PathVariable Long albumId, @PathVariable Long artistId) {
        albumService.unlinkArtist(albumId, artistId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/artists")
    public ResponseEntity<List<ArtistResponseDTO>> linkArtist(@PathVariable Long id) {
        List<ArtistResponseDTO> artists = artistService.getArtistsForAlbum(id);
        return ResponseEntity.ok(artists);
    }
}
