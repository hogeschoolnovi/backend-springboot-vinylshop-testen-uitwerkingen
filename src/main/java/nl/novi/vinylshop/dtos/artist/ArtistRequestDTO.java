package nl.novi.vinylshop.dtos.artist;

import jakarta.validation.constraints.NotNull;

public class ArtistRequestDTO {
    @NotNull
    private String name;
    private String biography;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
