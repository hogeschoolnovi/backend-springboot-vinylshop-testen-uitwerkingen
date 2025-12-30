package nl.novi.vinylshop.dtos.publisher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class PublisherRequestDTO {
    @NotBlank(message = "Naam mag niet leeg zijn")
    @Size(max = 50, message = "Naam moet tussen 2 en 100 karakters lang zijn")
    private String name;
    private String address;
    private String contactDetails;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublisherRequestDTO that = (PublisherRequestDTO) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getContactDetails(), that.getContactDetails());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAddress(), getContactDetails());
    }
}

