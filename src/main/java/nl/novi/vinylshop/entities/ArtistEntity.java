package nl.novi.vinylshop.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
public class ArtistEntity extends BaseEntity {
    private String name;
    private String biography;
    @ManyToMany(mappedBy = "artists")
    private Set<AlbumEntity> albums = new HashSet<>();

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Set<AlbumEntity> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<AlbumEntity> albums) {
        this.albums = albums;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
