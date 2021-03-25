package com.sg.superHumans.controller;

import com.sg.superHumans.Dao.*;
import com.sg.superHumans.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SightingsController {

        @Autowired
        OrganizationDao organizationDao;

        @Autowired
        LocationDao locationDao;

        @Autowired
        SuperPowerDao superPowerDao;

        @Autowired
        SightingsDao sightingsDao;

        @Autowired
        SuperHumanDao superHumanDao;

        Set<ConstraintViolation<Sightings>> violations= new HashSet<>();

     @GetMapping("/")
     public String displayLatestSightings(Model model) {
        List<Sightings> sightings = sightingsDao.getMostRecentSightings();
        model.addAttribute("sightings", sightings);
        return "index";
    }

        @GetMapping("sightings")
        public String displaySightings(Model model) {
            List<Sightings> sightings = sightingsDao.getAllSightings();
            model.addAttribute("sightings", sightings);
            model.addAttribute("errors", violations);
            return "sightings";
        }

        @PostMapping("addSightings")
        public String addSightings(int id, SuperHuman superHuman, Location location, LocalDate date) {

            Sightings sightings = new Sightings();
            sightings.setId(id);
            sightings.setSuperHuman(superHumanDao.getSuperHumanById(id));
            sightings.setLocation(locationDao.getLocationById(id));
            sightings.setDate(LocalDate.now());


            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            violations = validate.validate(sightings);

            if(violations.isEmpty()) {
                sightingsDao.addSightings(sightings);
            }

            return "redirect:/sightings";
        }
        @GetMapping("editSightings")

        public String editSightings(Integer id, Model model) {
        Sightings sightings = sightingsDao.getSightingsById(id);
        model.addAttribute("sightings", sightings);
        List<SuperHuman> superHuman = superHumanDao.getAllSuperHumans();
        model.addAttribute("superHuman", superHuman);
        List<Location> location = locationDao.getAllLocations();
        model.addAttribute("location", location);
        return "editSuperHuman";
    }

        @PostMapping("editSightings")
        public String performEditSightings(@Valid Sightings sightings, BindingResult result, HttpServletRequest request) {
           String superHumanId = request.getParameter("superHumanId");
           String locationId = request.getParameter("locationId");
           String localDate= request.getParameter("date");

           String sightingsId = request.getParameter("superHumanId");
           sightings.setId(Integer.parseInt(sightingsId));
           sightings.setSuperHuman(superHumanDao.getSuperHumanById(Integer.parseInt(superHumanId)));
           sightings.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));

            if(result.hasErrors()) {
                return "editSightings";
            }
            sightingsDao.addSightings(sightings);

            return "redirect:/sightings";
        }
        @GetMapping("deleteSightings")
        public String deleteSightings(Integer id) {
            sightingsDao.deleteSightings(id);
            return "redirect:/sightings";
        }

        @GetMapping("sightingsDetail")
        public String SightingsDetail(Integer id, Model model) {
            Sightings sightings = sightingsDao.getSightingsById(id);
            model.addAttribute("sightings", sightings);
            return "sightingsDetail";
        }

    }


