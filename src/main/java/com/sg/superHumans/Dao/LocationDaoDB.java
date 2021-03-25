package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.Location;
import com.sg.superHumans.Entity.Sightings;
import com.sg.superHumans.Entity.SuperHuman;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LocationDaoDB implements LocationDao {
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Location getLocationById(int id) {
        try {
            final String GET_LOCATION_BY_ID = "SELECT * FROM location WHERE id = ?";
            return jdbc.queryForObject(GET_LOCATION_BY_ID, new LocationMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Location> getAllLocations() {
        final String GET_ALL_LOCATIONS = "SELECT * FROM location";
        return jdbc.query(GET_ALL_LOCATIONS, new LocationMapper());
    }

    @Override
    public List<Location> getLocationForSuperHuman(SuperHuman superHuman) {
        final String superHumanFromLocation = "SELECT l.* FROM location l JOIN sightings " +
                "s ON o.id = os.organizationId WHERE os.superHumanId = ?";
        return jdbc.query(superHumanFromLocation, new LocationMapper(), superHuman.getId());
    }

    @Override
    public List<Location> getLocationForSightings(Sightings sightings) {
        final String superHumanFromOrg = "SELECT l.* FROM location l JOIN sightings s " +
                "ON l.id = s.organizationId WHERE s.superHumanId = ?";
        return jdbc.query(superHumanFromOrg, new LocationMapper(), sightings.getId());
    }


    @Override
    public Location addLocation(Location location) {
       final String INSERT_LOCATION = "INSERT INTO location(name, description, address, latitude, longitude) VALUES(?,?,?,?,?)";
       jdbc.update(INSERT_LOCATION,
               location.getName(),
               location.getDescription(),
               location.getAddress(),
               location.getLatitude(),
               location.getLongitude());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setId(newId);
        return location;
    }
    @Override
    public void updateLocation(Location location) {
        final String UPDATE_LOCATION = "UPDATE location SET name = ?, description =?, address =?, latitude =?, longitude=? WHERE id=?";
        jdbc.update(UPDATE_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude());
    }

    @Override
    public void deleteLocation(int id) {
        final String DELETE_LOCATION= "DELETE FROM location WHERE id =?";
        jdbc.update(DELETE_LOCATION, id);
    }

    public static final class LocationMapper implements RowMapper<Location>{

        @Override
        public Location mapRow(ResultSet rs, int index) throws SQLException {
           Location location = new Location();
           location.setId(rs.getInt("id"));
           location.setName(rs.getString("name"));
           location.setDescription(rs.getString("description"));
           location.setAddress(rs.getString("address"));
           location.setLatitude(rs.getDouble("latitude"));
           location.setLongitude(rs.getDouble("longitude"));

            return location;
        }
    }
}
