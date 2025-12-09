package nl.novi.vinylshop.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "publishers")
public class PublisherEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
    private String address;
    private String contactDetails;
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    private List<AlbumEntity> albums;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public List<AlbumEntity> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumEntity> albums) {
        this.albums = albums;
    }
}
