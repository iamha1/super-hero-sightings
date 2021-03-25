package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.Location;
import com.sg.superHumans.Entity.Sightings;
import com.sg.superHumans.Entity.SuperHuman;

import java.util.List;

public interface LocationDao {

    Location getLocationById(int id);
    List<Location> getAllLocations();
    List<Location> getLocationForSuperHuman(SuperHuman superHuman);
    List<Location> getLocationForSightings(Sightings sightings);
    Location addLocation(Location location);
    void updateLocation(Location location);
    void deleteLocation(int id);

}
