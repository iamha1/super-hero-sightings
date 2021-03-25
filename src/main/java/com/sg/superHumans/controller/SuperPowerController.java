package com.sg.superHumans.controller;

import com.sg.superHumans.Dao.*;
import com.sg.superHumans.Entity.SuperPower;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SuperPowerController {

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

        Set<ConstraintViolation<SuperPower>> violations= new HashSet<>();

        @GetMapping("superpowers")
        public String displaySuperPower(Model model) {
            List<SuperPower> SuperPower = superPowerDao.getAllSuperPowers();
            model.addAttribute("superPower", SuperPower);
            model.addAttribute("errors", violations);
            return "superpowers";
        }

        @PostMapping("addSuperPower")
        public String addSuperPower(String name) {

            SuperPower superPower = new SuperPower();
            superPower.setName(name);

            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            violations = validate.validate(superPower);

            if(violations.isEmpty()) {
                superPowerDao.addSuperPower(superPower);
            }
            return "redirect:/superpowers";
        }

    @GetMapping("editSuperPower")
    public String editPower(HttpServletRequest request, Model model) {
            int id =  Integer.parseInt(request.getParameter("id"));
        SuperPower power = superPowerDao.getSuperPowerById(id);
        model.addAttribute("superpowers", power);
        model.addAttribute("errors", violations);
        return "editSuperPower";
    }

        @PostMapping("editSuperPower")

        public String performEditSuperPower(SuperPower superPower) {
            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            violations = validate.validate(superPower);

            if(violations.isEmpty()) {
                superPowerDao.updateSuperPower(superPower);
            }
            return "redirect:/superpowers";
        }

    @GetMapping("deleteSuperPower")
    public String deleteSuperPower(Integer id) {
        superPowerDao.deleteSuperPower(id);
        return "redirect:/superpowers";
    }

        @GetMapping("superPowerDetail")
        public String SuperPowerDetail(Integer id, Model model) {
            SuperPower superPower= superPowerDao.getSuperPowerById(id);
            model.addAttribute("superPower", superPower);
            return "superPowerDetail";
        }
    }

