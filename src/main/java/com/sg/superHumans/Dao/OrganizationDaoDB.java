package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.Organization;
import com.sg.superHumans.Entity.SuperHuman;
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
public class OrganizationDaoDB implements OrganizationDao{
    @Autowired
    JdbcTemplate jdbc;
    @Override
    public Organization getOrganizationById(int id) {
        try {
            final String GET_ORG_BY_ID = "SELECT * FROM organization WHERE id = ?";
            return jdbc.queryForObject(GET_ORG_BY_ID, new OrgMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }    }

    @Override
    public List<Organization> getAllOrganizations() {
        final String GET_ALL_ORGS = "SELECT * FROM organization";
        return jdbc.query(GET_ALL_ORGS, new OrgMapper());    }

    @Override
    public Organization addOrganization(Organization organization) {
        final String INSERT_ORG = "INSERT INTO organization(name, description, address, contact) " +
                                  "VALUES(?,?,?,?)";
        jdbc.update(INSERT_ORG,
                    organization.getName(),
                    organization.getDescription(),
                    organization.getAddress(),
                    organization.getContact());

            int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            organization.setId(newId);
            return organization;
        }

    @Override
    public void updateOrganization(Organization organization) {
            final String UPDATE_ORG = "UPDATE organization SET name = ?, description = ?, address = ?, contact = ? WHERE id = ?";
            jdbc.update(UPDATE_ORG,
                    organization.getName(),
                    organization.getDescription(),
                    organization.getAddress(),
                    organization.getContact(),
                    organization.getId());
    }

    @Override
    @Transactional
    public void deleteOrganization(int id) {
        final String DELETE_ORG_SUPERHUMAN= "DELETE FROM organization_superHuman WHERE organizationId =?";
        jdbc.update(DELETE_ORG_SUPERHUMAN, id);

        final String DELETE_ORG= "DELETE FROM organization WHERE id =?";
        jdbc.update(DELETE_ORG, id);
    }

    @Override
    public List<Organization> getOrganizationBySuperHuman(SuperHuman superHuman) {
        final String superHumanFromOrg = "SELECT o.* FROM organization o JOIN organization_superHuman " +
                                         "os ON o.id = os.organizationId WHERE os.superHumanId = ?";
      return  jdbc.query(superHumanFromOrg, new OrgMapper(), superHuman.getId());

    }

public static final class OrgMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int index) throws SQLException {
            Organization organization = new Organization();
            organization.setId(rs.getInt("id"));
            organization.setName(rs.getString("name"));
            organization.setDescription(rs.getString("description"));
            organization.setAddress(rs.getString("address"));
            organization.setContact(rs.getString("contact"));

            return organization;
        }
    }
}
