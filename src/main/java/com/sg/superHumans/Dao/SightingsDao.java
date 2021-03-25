package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.Location;
import com.sg.superHumans.Entity.Sightings;
import com.sg.superHumans.Entity.SuperHuman;
import com.sg.superHumans.Entity.SuperPower;

import java.time.LocalDate;
import java.util.List;

public interface SightingsDao {
    Sightings getSightingsById(int id);
    List<Sightings> getAllSightings();
    List <Sightings> getAllSightingsByDate(LocalDate date);
    List<Sightings> getMostRecentSightings();
    List<Sightings> getSightingsByLocation(Location location);
    List<Sightings> getSightingsForSuperHuman(SuperHuman superHuman);
    Sightings addSightings(Sightings sightings);
    void updateSightings(Sightings sightings);
    void deleteSightings(int id);
}
