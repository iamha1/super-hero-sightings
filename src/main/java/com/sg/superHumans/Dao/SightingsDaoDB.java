package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.Location;
import com.sg.superHumans.Entity.Sightings;
import com.sg.superHumans.Entity.SuperHuman;
import com.sg.superHumans.Entity.SuperPower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class SightingsDaoDB implements SightingsDao {
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Sightings getSightingsById(int id) {
        try {
            final String GET_SIGHTINGS_BY_ID = "SELECT * FROM sightings WHERE id = ?";
            Sightings sightings = jdbc.queryForObject(GET_SIGHTINGS_BY_ID, new SightingsMapper(), id);
            sightings.setSuperHuman(getSuperHumanForSightings(id));
            sightings.setLocation(getLocationForSightings(id));
            return sightings;

        } catch (DataAccessException ex) {
            return null;
        }
    }

    private Location getLocationForSightings(int id) {
        final String SELECT_LOCATION_FOR_SIGHTINGS = "SELECT l.* FROM location l JOIN sightings s ON l.id = s.locationId WHERE s.id = ?";
        return jdbc.queryForObject(SELECT_LOCATION_FOR_SIGHTINGS, new LocationDaoDB.LocationMapper(), id);
    }

    private SuperHuman getSuperHumanForSightings(int id) {
        final String SELECT_HUMAN_FOR_SIGHTINGS= "SELECT sh.* FROM superHuman sh JOIN sightings s ON s.superHumanId = sh.id WHERE s.id = ?";
        return jdbc.queryForObject(SELECT_HUMAN_FOR_SIGHTINGS, new SuperHumanDaoDB.SuperHumanMapper(), id);
    }


    @Override
    public List<Sightings> getAllSightings() {
        final String GET_ALL_SIGHTINGS = "SELECT * FROM sightings";
        List<Sightings> sightings = jdbc.query(GET_ALL_SIGHTINGS, new SightingsMapper());
        associateLocationAndHuman(sightings);
        return sightings;
    }
    private void associateLocationAndHuman(List<Sightings> sightings){
        for(Sightings sighting: sightings){
            sighting.setSuperHuman(getSuperHumanForSightings(sighting.getId()));
            sighting.setLocation(getLocationForSightings(sighting.getId()));
        }
    }

    @Override
    public List<Sightings> getAllSightingsByDate(LocalDate date) {
        final String GET_ALL_SIGHTINGS_DATE = "SELECT * FROM sightings WHERE date = ?";
        List<Sightings> sightingsByDate = jdbc.query(GET_ALL_SIGHTINGS_DATE, new SightingsMapper(), date);
        associateLocationAndHuman(sightingsByDate);
        return sightingsByDate;
    }

    @Override
        public void updateSightings(Sightings sightings) {
            final String UPDATE_SIGHTINGS = "UPDATE sighting SET date = ?, superHumanId = ?, locationId = ? "
                    + "WHERE id = ?";
            jdbc.update(UPDATE_SIGHTINGS,
                    sightings.getDate(),
                    sightings.getSuperHuman().getId(),
                    sightings.getLocation().getId(),
                    sightings.getId());
        }


    @Override
    public void deleteSightings(int id) {
        final String DELETE_SIGHTINGS_SUPERHUMAN = "DELETE FROM sightings WHERE id = ?";
            jdbc.update(DELETE_SIGHTINGS_SUPERHUMAN, id);
        }

    @Override
    @Transactional
    public Sightings addSightings(Sightings sightings) {
        final String INSERT_SIGHTINGS = "INSERT INTO sightings(date, superHumanId, locationId) "
                + "VALUES(?,?,?)";
        jdbc.update(INSERT_SIGHTINGS,
                sightings.getDate(),
                sightings.getSuperHuman().getId(),
                sightings.getLocation().getId());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sightings.setId(newId);
        return sightings;
    }

    @Override
    public List<Sightings> getMostRecentSightings() {
        final String SELECT_SIGHTINGS_BY_DATE = "SELECT * FROM sightings ORDER BY date DESC LIMIT 10";
        List<Sightings> recentSightings= jdbc.query(SELECT_SIGHTINGS_BY_DATE, new SightingsMapper());
        associateLocationAndHuman(recentSightings);
        return recentSightings;
    }

    @Override
    public List<Sightings> getSightingsByLocation(Location location) {
        final String SELECT_SIGHTINGS_BY_LOCATION = "SELECT * FROM sightings WHERE locationId = ?";
        List<Sightings> sightingsByLocation= jdbc.query(SELECT_SIGHTINGS_BY_LOCATION, new SightingsMapper());
        associateLocationAndHuman(sightingsByLocation);
        return sightingsByLocation;
}

    @Override
    public List<Sightings> getSightingsForSuperHuman(SuperHuman superHuman) {
        final String SELECT_SIGHTINGS_BY_HUMAN = "SELECT * FROM sightings WHERE superHumanId = ?";
        List<Sightings> sightingsForSuperHuman= jdbc.query(SELECT_SIGHTINGS_BY_HUMAN, new SightingsMapper());
        associateLocationAndHuman(sightingsForSuperHuman);
        return sightingsForSuperHuman;
}



    public static final class SightingsMapper implements RowMapper<Sightings> {

        @Override
        public Sightings mapRow(ResultSet rs, int index) throws SQLException {
            Sightings sightings = new Sightings();
            sightings.setId(rs.getInt("id"));
            //sightings.setSuperHumanId(rs.getInt("superHumanId"));
            //sightings.setLocationId(rs.getInt("locationId"));
            sightings.setDate(rs.getDate("date").toLocalDate());


            return sightings;
        }
    }
}
