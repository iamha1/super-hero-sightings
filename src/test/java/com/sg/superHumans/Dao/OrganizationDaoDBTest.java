package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrganizationDaoDBTest {

    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SightingsDao sightingsDao;

    @Autowired
    SuperHumanDao superHumanDao;

    @Autowired
    SuperPowerDao superPowerDao;

    public OrganizationDaoDBTest(){
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        List<Organization> organizations = organizationDao.getAllOrganizations();
        for (Organization organization : organizations) {
            organizationDao.deleteOrganization(organization.getId());
        }
        List<Location> locations = locationDao.getAllLocations();
        for(Location location : locations) {
            locationDao.deleteLocation(location.getId());
        }

        List<SuperPower> powers = superPowerDao.getAllSuperPowers();
        for (SuperPower power : powers) {
            superPowerDao.deleteSuperPower(power.getId());
        }

        List<Sightings> sightings = sightingsDao.getAllSightings();
        for (Sightings sighting : sightings) {
            sightingsDao.deleteSightings(sighting.getId());
        }

        List<SuperHuman> superHumans = superHumanDao.getAllSuperHumans();
        for (SuperHuman superHuman : superHumans) {
            superHumanDao.deleteSuperHuman(superHuman.getId());
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getOrganizationById() {
        Organization organization = new Organization();
        organization.setName("Test organization");
        organization.setAddress("123 Testing");
        organization.setContact("e@mail.com");
        organization.setId(1);
        organizationDao.addOrganization(organization);

        Organization fromDao = organizationDao.getOrganizationById(organization.getId());

        assertEquals(organization, fromDao);
    }

    @Test
    public void getAllOrganizations() {
        Organization organization = new Organization();

        organization.setName("Test organization");
        organization.setDescription("An testing organization");
        organization.setAddress("123 Testing");
        organization.setContact("organizations@gmail.com");
        organizationDao.addOrganization(organization);

        Organization organization2 = new Organization();
        organization2.setName("#2 Test organization");
        organization2.setDescription("#2 An testing organization");
        organization2.setAddress("321 Testing");
        organization2.setContact("organizations2@gmail.com");
        organizationDao.addOrganization(organization2);


        List<Organization> organizations = organizationDao.getAllOrganizations();

        assertEquals(2, organizations.size());
       // assertTrue(organization.contains(organization));
       // assertTrue(organization.contains(organization2));
    }
    @Test
    public void addOrganization() {
        Organization organization = new Organization();
        organization.setName("Test organization");
        organization.setDescription("An testing organization");
        organization.setAddress("123 Testing");
        organization.setContact("organizations@gmail.com");
        organizationDao.addOrganization(organization);
        organization = organizationDao.addOrganization(organization);

        Organization fromDao = organizationDao.getOrganizationById(organization.getId());

        assertEquals(organization, fromDao);
    }
    @Test
    public void updateOrganization() {
        Organization organization = new Organization();
        organization.setName("Test organization");
        organization.setDescription("An testing organization");
        organization.setAddress("123 Testing");
        organization.setContact("organizations@gmail.com");
        organizationDao.addOrganization(organization);
        organization = organizationDao.addOrganization(organization);

        Organization fromDao = organizationDao.getOrganizationById(organization.getId());

        assertEquals(organization, fromDao);

        organization.setName("Test organization Update");
        organization.setDescription("An testing organization Update");
        organization.setAddress("123 Testing Update");
        organization.setContact("updateorganizations@gmail.com");

        organizationDao.updateOrganization(organization);

        assertNotEquals(organization, fromDao);

        fromDao = organizationDao.getOrganizationById(organization.getId());

        assertEquals(organization, fromDao);
    }
    @Test
    public void deleteOrganizationById() {
        Organization organization = new Organization();
        organization.setName("Test organization");
        organization.setDescription("An testing organization");
        organization.setAddress("123 Testing");
        organization.setContact("organizations@gmail.com");
        organizationDao.addOrganization(organization);

        int testOrganizationId = organization.getId();

        Organization fromDao = organizationDao.getOrganizationById(organization.getId());
        assertEquals(organization, fromDao);

        organizationDao.deleteOrganization(testOrganizationId);
    }

}