package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.Organization;
import com.sg.superHumans.Entity.SuperHuman;

import java.util.List;

public interface OrganizationDao {

    Organization getOrganizationById(int id);
    List <Organization> getAllOrganizations();
    Organization addOrganization(Organization organization);
    List<Organization> getOrganizationBySuperHuman(SuperHuman superHuman);
    void updateOrganization(Organization organization);
    void deleteOrganization(int id);


}
