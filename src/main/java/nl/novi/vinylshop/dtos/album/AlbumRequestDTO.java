package nl.novi.vinylshop.dtos.album;


import jakarta.validation.constraints.*;

public class AlbumRequestDTO {
    @NotBlank(message = "Naam mag niet leeg zijn")
    @Size(min = 3, max = 100, message = "Naam moet tussen 3 en 100 karakters lang zijn")
    private String title;
    @NotNull
    @Min(1877)
    @Max(2100)
    private Integer releaseYear;
    @NotNull
    private Long genreId;
    private Long publisherId;

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    // Getters en setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
}


