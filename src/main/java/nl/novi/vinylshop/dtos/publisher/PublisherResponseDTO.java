package nl.novi.vinylshop.dtos.publisher;

import java.util.Objects;

public class PublisherResponseDTO {

    private long id;
    private String name;
    private String address;
    private String contactDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
        PublisherResponseDTO that = (PublisherResponseDTO) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getContactDetails(), that.getContactDetails());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAddress(), getContactDetails());
    }
}
