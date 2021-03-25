package com.sg.superHumans.controller;

import com.sg.superHumans.Dao.*;
import com.sg.superHumans.Entity.Location;
import com.sg.superHumans.Entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class LocationController {
    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganizationDao organizationsDao;

    @Autowired
    SuperPowerDao superPowerDao;

    @Autowired
    SightingsDao sightingDao;

    @Autowired
    SuperHumanDao superHumanDao;


    Set<ConstraintViolation<Location>> violations= new HashSet<>();

    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("errors", violations);
        return "locations";
    }

    @PostMapping("addLocation")
    public String addLocation(String name, String description, String address, double latitude, double longitude) {

        Location location = new Location();
        location.setName(name);
        location.setDescription(description);
        location.setAddress(address);
        location.setLatitude(latitude);
        location.setLongitude(longitude);

       Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
       violations = validate.validate(location);

        if(violations.isEmpty()) {
            locationDao.addLocation(location);
        }

        return "redirect:/locations";
    }

    @GetMapping("deleteLocation")
    public String deleteLocation(Integer id) {
        locationDao.deleteLocation(id);
        return "redirect:/locations";
    }


    @GetMapping("editLocation")
    public String editLocation(Integer id, Model model) {
        Location locations = locationDao.getLocationById(id);
        model.addAttribute("locations", locations);
        return "editLocation";
    }

    @PostMapping("editLocation")
    public String performEditLocation(@Valid Location locations, BindingResult result) {
        if(result.hasErrors()) {
            return "editLocation";
        }
        locationDao.updateLocation(locations);

        return "redirect:/locations";
    }

    @GetMapping("confirmDeleteLocation")
    public String confirmDeleteLocation(Integer id, Model model) {
        Location locations = locationDao.getLocationById(id);
        model.addAttribute("locations", locations);
        return "confirmDeleteLocation";
    }

    @GetMapping("locationDetail")
    public String locationDetail(Integer id, Model model) {
        Location locations = locationDao.getLocationById(id);
        model.addAttribute("locations", locations);
        return "locationDetail";
    }
//    @GetMapping("findLocation")
//    public String findLocation(Integer superHumanId, Model model) {
//        locationDao.getLocationById(superHumanId);
//
//        model.addAttribute("locations");
//        return "locationDetail";
//    }

}

