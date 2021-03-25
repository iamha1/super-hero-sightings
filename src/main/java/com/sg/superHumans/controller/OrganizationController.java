package com.sg.superHumans.controller;


import com.sg.superHumans.Dao.*;
import com.sg.superHumans.Entity.Location;
import com.sg.superHumans.Entity.Organization;
import com.sg.superHumans.Entity.SuperPower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class OrganizationController {
    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    SuperPowerDao superPowerDao;

    @Autowired
    SightingsDao sightingDao;

    @Autowired
    SuperHumanDao superHumanDao;

    Set<ConstraintViolation<Organization>> violations= new HashSet<>();

    @GetMapping("organizations")
    public String displayOrganizations(Model model) {
        List<Organization> organizations = organizationDao.getAllOrganizations();
        model.addAttribute("organizations", organizations);
        model.addAttribute("errors", violations);
        return "organizations";
    }

    @PostMapping("addOrganization")
    public String addOrganization(String name, String description, String address, String contact) {

        Organization organization = new Organization();
        organization.setName(name);
        organization.setDescription(description);
        organization.setAddress(address);
        organization.setContact(contact);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(organization);

        if(violations.isEmpty()) {
            organizationDao.addOrganization(organization);
        }

        return "redirect:/organizations";
    }

    @GetMapping("editOrganization")
    public String editOrganization(HttpServletRequest request, Model model) {
        int id =  Integer.parseInt(request.getParameter("id"));
        Organization org = organizationDao.getOrganizationById(id);
        model.addAttribute("organizations", org);
        model.addAttribute("errors", violations);
        return "editOrganization";
    }

    @GetMapping("deleteOrganization")
    public String deleteOrganization(Integer id) {
        organizationDao.deleteOrganization(id);
        return "redirect:/organizations";
    }

 @PostMapping("editOrganization")
 public String performEditOrg(Organization organization) {
    Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
    violations = validate.validate(organization);

            if(violations.isEmpty()) {
        organizationDao.updateOrganization(organization);
    }
            return "redirect:/organizations";
}

    @GetMapping("organizationDetail")
    public String organizationDetail(Integer id, Model model) {
        Organization organization = organizationDao.getOrganizationById(id);
        model.addAttribute("organizations", organization);
        return "organizationDetail";
    }
}
