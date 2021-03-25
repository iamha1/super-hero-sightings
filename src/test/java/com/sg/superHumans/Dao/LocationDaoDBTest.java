package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class LocationDaoDBTest {

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


    public LocationDaoDBTest(){

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        List<Location> locations = locationDao.getAllLocations();
        for(Location location : locations) {
           locationDao.deleteLocation(location.getId());
        }

        List<Organization> organizations = organizationDao.getAllOrganizations();
        for(Organization organization : organizations) {
            organizationDao.deleteOrganization(organization.getId());
        }

        List<SuperPower> powers = superPowerDao.getAllSuperPowers();
        for(SuperPower power : powers) {
            superPowerDao.deleteSuperPower(power.getId());
        }

        List<Sightings> sightings = sightingsDao.getAllSightings();
        for(Sightings sighting : sightings) {
            sightingsDao.deleteSightings(sighting.getId());
        }

        List<SuperHuman> superHumans = superHumanDao.getAllSuperHumans();
        for(SuperHuman superHuman : superHumans) {
            superHumanDao.deleteSuperHuman(superHuman.getId());
        }
    }
    @After
    public void tearDown() {
    }

    @Test
    public void getAllLocations() {
        Location location = new Location();
        location.setDescription("Test Description");
        location.setAddress("123 Testing Lane");
        location.setLatitude(2.33);
        location.setLongitude(4.55);
        locationDao.addLocation(location);

        Location location2 = new Location();
        location2.setDescription("Test Description 2");
        location2.setAddress("123 Testing Lane 2");
        location2.setLatitude(5.66);
        location2.setLongitude(18.56);
        locationDao.addLocation(location2);

        List<Location> locations = locationDao.getAllLocations();

        assertEquals(2, locations.size());
        assertTrue(locations.contains(location));
        assertTrue(locations.contains(location2));
    }


    @Test
    void getLocationById() {
        Location location = new Location();
        location.setDescription("An testing organization");
        location.setAddress("123 Testing");
        location.setLongitude(11.1);
        location.setLatitude(22.2);
        location.setId(1);
        locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getId());

        assertEquals(location, fromDao);
    }

    @Test
    public void deleteLocationById() {
        Location location = new Location();
        location.setDescription("Test Description");
        location.setAddress("123 Testing Lane");
        location.setLatitude(2.33);
        location.setLongitude(4.55);
        locationDao.addLocation(location);

        int testLocationId = location.getId();

        SuperPower powers = new SuperPower();
        powers.setName("Strength");
        powers = superPowerDao.addSuperPower(powers);

        SuperHuman superHuman = new SuperHuman();
        superHuman.setName("TestName");
        superHuman.setDescription("Test Super Strong");
        superHuman = superHumanDao.addSuperHuman(superHuman);

        Sightings sighting = new Sightings();
        sighting.setDate(LocalDate.of(1990,10,10));
        //sighting.setSuperHumanId(superHuman.getId());
       // sighting.setLocationId(testLocationId);
        sightingsDao.addSightings(sighting);

        Location fromDAO = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDAO);

        sightingsDao.deleteSightings(sighting.getId());

        locationDao.deleteLocation(location.getId());
    }
}