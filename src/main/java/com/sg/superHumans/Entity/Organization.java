package com.sg.superHumans.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class Organization {
    private int id;
    @NotBlank(message = "Name must not be empty.")
    @Size(max = 256, message="Name must be less than 50 characters.")
    private String name;
    @NotBlank(message = "Description must not be empty.")
    @Size(max = 256, message="Description must be less than 50 characters.")
    private String description;
    @NotBlank(message = "Address must not be empty.")
    @Size(max = 256, message="Address must be less than 50 characters.")
    private String address;
    @NotBlank(message = "Contact must not be empty.")
    @Size(max = 256, message="Contact must be less than 50 characters.")
    private String contact;


    public Organization() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(address, that.address) && Objects.equals(contact, that.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, address, contact);
    }
}


