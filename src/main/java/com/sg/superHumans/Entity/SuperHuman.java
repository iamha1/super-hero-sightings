package com.sg.superHumans.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class SuperHuman {
    private int id;
    @NotBlank(message = "Name must not be empty.")
    @Size(max = 256, message="Name must be less than 50 characters.")
    private String name;
    @NotBlank(message = "Description must not be empty.")
    @Size(max = 256, message="Name must be less than 50 characters.")
    private String description;
    private boolean isEvil;
    private SuperPower superPower;
    private List<Organization> organization;



    public SuperHuman() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEvil() {
        return isEvil;
    }

    public void setEvil(boolean evil) {
        isEvil = evil;
    }

    public SuperPower getSuperPower() {
        return superPower;
    }

    public List<Organization> getOrganization() {
        return organization;
    }

    public void setOrganization(List<Organization> organization) {
        this.organization = organization;
    }

    public void setSuperPower(SuperPower superPower) {
        this.superPower = superPower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperHuman that = (SuperHuman) o;
        return id == that.id && isEvil == that.isEvil && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(superPower, that.superPower) && Objects.equals(organization, that.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isEvil, superPower, organization);
    }
}
