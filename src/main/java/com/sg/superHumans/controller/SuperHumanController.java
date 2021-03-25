package com.sg.superHumans.controller;

import com.sg.superHumans.Dao.*;

import com.sg.superHumans.Entity.SuperHuman;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SuperHumanController {

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

    Set<ConstraintViolation<SuperHuman>> violations= new HashSet<>();

    @GetMapping("superHumans")
    public String displaySuperHuman(Model model) {
        List<SuperHuman> SuperHuman = superHumanDao.getAllSuperHumans();
        model.addAttribute("superHuman", SuperHuman);
        List<SuperPower> SuperPower = superPowerDao.getAllSuperPowers();
        model.addAttribute("SuperPower", SuperPower);
        model.addAttribute("errors", violations);
        return "superHumans";
    }

    @PostMapping("addSuperHuman")
    public String addSuperHuman (SuperHuman superhuman, HttpServletRequest request){
        //superhuman superhuman = new superhuman();
        String powerId = request.getParameter("powerId");
        String superhumanDescription = request.getParameter("description");
        String isEvil = request.getParameter("isEvil");
        String superhumanName = request.getParameter("name");

        superhuman.setName(superhumanName);
        superhuman.setDescription(superhumanDescription);
        superhuman.setEvil(Boolean.parseBoolean(isEvil));
        superhuman.setSuperPower(superPowerDao.getSuperPowerById(Integer.parseInt(powerId)));

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(superhuman);

        if(violations.isEmpty()){
            superHumanDao.addSuperHuman(superhuman);
        }

        return "redirect:/superHumans";
    }

    @GetMapping("deleteSuperHuman")
    public String deleteSuperHuman(Integer id) {
        superHumanDao.deleteSuperHuman(id);
        return "redirect:/superHumans";
    }

    @GetMapping("editSuperHuman")
    public String editSuperHuman(Integer id, Model model) {
        SuperHuman superHuman = superHumanDao.getSuperHumanById(id);
        model.addAttribute("superHuman", superHuman);
        List<SuperPower> SuperPower = superPowerDao.getAllSuperPowers();
        model.addAttribute("SuperPower", SuperPower);
        return "editSuperHuman";
    }

    @PostMapping("editSuperHuman")
    public String performEditSuperHuman(@Valid SuperHuman superHuman, BindingResult result, HttpServletRequest request) {
        String powerId = request.getParameter("powerId");
        String superhumanDescription = request.getParameter("description");
        String isEvil = request.getParameter("isEvil");
        String superhumanName = request.getParameter("name");

        String superHumanId = request.getParameter("superHumanId");
        superHuman.setId(Integer.parseInt(superHumanId));
        superHuman.setName(superhumanName);
        superHuman.setDescription(superhumanDescription);
        superHuman.setEvil(Boolean.parseBoolean(isEvil));
        superHuman.setSuperPower(superPowerDao.getSuperPowerById(Integer.parseInt(powerId)));

        if(result.hasErrors()) {
            return "editSuperHuman";
        }
        superHumanDao.updateSuperHuman(superHuman);
        return "redirect:/superHumans";
    }

    @GetMapping("superHumanDetail")
    public String SuperHumanDetail(Integer id, Model model) {
        SuperHuman superHuman = superHumanDao.getSuperHumanById(id);
        model.addAttribute("superHuman", superHuman);
        return "superHumanDetail";
    }

}