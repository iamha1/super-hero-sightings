package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.Location;
import com.sg.superHumans.Entity.Organization;
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
public class SuperHumanDaoDB implements SuperHumanDao {
    @Autowired
    JdbcTemplate jdbc;


    @Override
    public SuperHuman getSuperHumanById(int id) {
        try {
            final String GET_SUPERHUMAN_BY_ID = "SELECT * FROM superHuman WHERE id = ?";
            SuperHuman superHuman = jdbc.queryForObject(GET_SUPERHUMAN_BY_ID, new SuperHumanMapper(), id);
            superHuman.setSuperPower(getPowerForSuperhuman(id));
            superHuman.setOrganization(getOrganizationForSuperHuman(id));
            return superHuman;
        } catch(DataAccessException ex) {
           return null;
        }
    }

    private List<Organization> getOrganizationForSuperHuman(int id) {
        final String SELECT_ORG_FOR_SUPERHUMAN = "SELECT o.* FROM organization o " +
                "JOIN organization_superHuman os ON os.organizationId = o.id " +
                "WHERE os.superHumanId = ?";
        return jdbc.query(SELECT_ORG_FOR_SUPERHUMAN, new OrganizationDaoDB.OrgMapper(), id);
    }

    @Override
    public List<SuperHuman> getAllSuperHumans() {
        final String GET_ALL_SUPER_HUMANS = "SELECT * FROM superHuman";
        List<SuperHuman> superHumans = jdbc.query(GET_ALL_SUPER_HUMANS, new SuperHumanMapper());
        associatePowerAndOrganization(superHumans);
        return superHumans;
    }

    private void associatePowerAndOrganization(List<SuperHuman> superHumans){
        for(SuperHuman superHuman: superHumans){
            superHuman.setSuperPower(getPowerForSuperhuman(superHuman.getId()));
            superHuman.setOrganization(getOrganizationForSuperHuman(superHuman.getId()));
        }
    }

    @Override
    @Transactional
    public SuperHuman addSuperHuman(SuperHuman superHuman) {
        final String INSERT_SUPER_HUMANS = "INSERT INTO superHuman(name, description, isEvil, powerId) VALUES(?,?,?,?)";
        jdbc.update(INSERT_SUPER_HUMANS, superHuman.getName(), superHuman.getDescription(), superHuman.isEvil(), superHuman.getSuperPower().getId());

      int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
       superHuman.setId(newId);
       // insertSuperHumanOrganization(superHuman);
        return superHuman;
    }

    private void insertSuperHumanOrganization(SuperHuman superHuman){
        final String INSERT_ORG_FOR_SUPERHUMAN = "INSERT INTO organization_superHuman(superHumanId,organizationId) VALUES(?,?) ";
        for (Organization organization: superHuman.getOrganization()){
            jdbc.update(INSERT_ORG_FOR_SUPERHUMAN,
                    superHuman.getId(),
                    organization.getId());
        }
    }

    private SuperPower getPowerForSuperhuman(int id) {
        final String SELECT_POWER_FOR_SUPER = "SELECT sp.* FROM superpower sp JOIN superhuman sh ON sh.powerId = sp.id WHERE sh.id = ?";
        return jdbc.queryForObject(SELECT_POWER_FOR_SUPER, new SuperPowerDaoDB.SuperPowerMapper(), id);
    }

    @Override
    public List<SuperHuman> getSuperHumanForOrganization(Organization organization) {
        final String GET_SUPER_HUMAN_BY_ORG = "SELECT sh.* FROM superHuman sh " +
                "JOIN organization_superHuman os ON os.superHumanId = sh.id WHERE os.organizationId = ?";
        List<SuperHuman> superHumanList = jdbc.query(GET_SUPER_HUMAN_BY_ORG, new SuperHumanMapper(), organization.getId());
        associatePowerAndOrganization(superHumanList);
        return superHumanList;
    }

    @Override
    public List<SuperHuman> getSuperHumanPower(SuperPower superPower) {
        final String GET_SUPER_HUMAN_POWER = "SELECT sh.* FROM superHuman sh " +
                "JOIN organization_superHuman os ON os.superHumanId = sh.id WHERE os.organizationId = ?";
        List<SuperHuman> superHumanList = jdbc.query(GET_SUPER_HUMAN_POWER, new SuperHumanMapper(), superPower.getId());
        associatePowerAndOrganization(superHumanList);
        return superHumanList;
    }

    @Override
    //@Transactional
    public void updateSuperHuman(SuperHuman superHuman) {
        final String UPDATE_SUPER_HUMAN = "UPDATE superHuman SET name = ?, description = ?," +
                "isEvil = ?, powerId = ? WHERE id = ?";
        jdbc.update(UPDATE_SUPER_HUMAN, superHuman.getName(), superHuman.getDescription(),
                superHuman.isEvil(), superHuman.getSuperPower().getId(), superHuman.getId());

//        final String DELETE_HUMAN_ORG = "DELETE FROM organization_superHuman WHERE superHumanId = ?";
//        jdbc.update(DELETE_HUMAN_ORG, superHuman.getId());
//        insertSuperHumanOrganization(superHuman);
    }

    @Override
    @Transactional
    public void deleteSuperHuman(int id) {
        final String DELETE_SUPER_HUMAN_POWER = "DELETE FROM organization_superHuman WHERE superHumanId = ?";
        jdbc.update(DELETE_SUPER_HUMAN_POWER,id);

        final String DELETE_SIGHTINGS = "DELETE FROM sightings WHERE superHumanId =?";
        jdbc.update(DELETE_SIGHTINGS, id);

        final String DELETE_SUPER_HUMAN= "DELETE FROM superHuman WHERE id = ?";
        jdbc.update(DELETE_SUPER_HUMAN, id);

    }

    public static final class SuperHumanMapper implements RowMapper<SuperHuman> {
        @Override
        public SuperHuman mapRow(ResultSet rs, int index) throws SQLException {
            SuperHuman superHuman = new SuperHuman();
            superHuman.setId(rs.getInt("id"));
            superHuman.setName(rs.getString("name"));
            superHuman.setDescription(rs.getString("description"));
            superHuman.setEvil(rs.getBoolean("isEvil"));
            return superHuman;

        }
    }
}

