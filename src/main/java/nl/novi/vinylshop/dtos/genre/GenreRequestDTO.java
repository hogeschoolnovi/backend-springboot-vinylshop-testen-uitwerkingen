package nl.novi.vinylshop.dtos.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GenreRequestDTO {
    @NotBlank(message = "Naam mag niet leeg zijn")
    @Size(min = 2, max = 100, message = "Naam moet tussen 2 en 100 karakters lang zijn")
    private String name;
    @Size(max = 255, message = "Beschrijving mag niet langer zijn dan 255 karakters")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
