package com.sg.superHumans.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Sightings {
    private int id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    private SuperHuman superHuman;
    private Location location;

    public Sightings() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public SuperHuman getSuperHuman() {
        return superHuman;
    }

    public void setSuperHuman(SuperHuman superHuman) {
        this.superHuman = superHuman;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sightings sightings = (Sightings) o;
        return id == sightings.id && Objects.equals(date, sightings.date) && Objects.equals(superHuman, sightings.superHuman) && Objects.equals(location, sightings.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, superHuman, location);
    }
}


