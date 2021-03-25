package com.sg.superHumans.Dao;

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
import java.util.List;
@Repository
public class SuperPowerDaoDB implements SuperPowerDao {
    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    SuperHumanDao superHumanDao;

    @Override
    public SuperPower getSuperPowerById(int id) {
        try {
            final String GET_SUPERPOWER_BY_ID = "SELECT * FROM superpower WHERE id = ?";
            return jdbc.queryForObject(GET_SUPERPOWER_BY_ID, new SuperPowerMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<SuperPower> getAllSuperPowers() {
        final String GET_ALL_SUPERPOWERS = "SELECT * FROM superpower";
        return jdbc.query(GET_ALL_SUPERPOWERS, new SuperPowerMapper());
    }

    @Override
    public SuperPower addSuperPower(SuperPower superPower) {
        final String ADD_SUPERPOWER = "INSERT INTO superpower(name) VALUES(?)";
        jdbc.update(ADD_SUPERPOWER, superPower.getName());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superPower.setId(newId);
        return superPower;
    }

    @Override
    public void updateSuperPower(SuperPower superPower) {
        final String UPDATE_SUPERPOWER = "UPDATE superpower SET name = ? WHERE id = ?";
        jdbc.update(UPDATE_SUPERPOWER, superPower.getName(), superPower.getId());
    }

    @Override
    @Transactional
    public void deleteSuperPower(int id) {
        List<SuperHuman> superHuman;
        try {
            superHuman = superHumanDao.getAllSuperHumans();
        } catch (DataAccessException ex) {
            superHuman = null;
        }
        if (superHuman != null) {
            for (SuperHuman s : superHuman) {

                final String DELETE_ORGANIZATION_SUPERHUMAN = "DELETE os.* FROM organization_superHuman os " +
                        "JOIN superHuman sh ON sh.id = os.superHumanId WHERE os.superHumanId = ? AND sh.powerId = ?";
                jdbc.update(DELETE_ORGANIZATION_SUPERHUMAN, s.getId(), id);

                final String DELETE_SIGHTING_SUPERHUMAN = "DELETE s.* FROM sightings s " +
                        "JOIN superHuman sh ON sh.id = s.superHumanId WHERE s.superHumanId = ? AND sh.powerId = ?";
                jdbc.update(DELETE_SIGHTING_SUPERHUMAN, s.getId(), id);

                final String DELETE_SUPERHUMAN = "DELETE FROM superHuman WHERE id = ? AND powerId = ?";
                jdbc.update(DELETE_SUPERHUMAN, s.getId(), id);

                final String DELETE_SUPERPOWER = "DELETE FROM superpower WHERE id =?";
                jdbc.update(DELETE_SUPERPOWER, id);
            }
        }
    }

        public static final class SuperPowerMapper implements RowMapper<SuperPower> {

            @Override
            public SuperPower mapRow(ResultSet rs, int index) throws SQLException {
                SuperPower superPower = new SuperPower();
                superPower.setId(rs.getInt("id"));
                superPower.setName(rs.getString("name"));

                return superPower;
            }
        }

    }
