package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.Location;
import com.sg.superHumans.Entity.Organization;
import com.sg.superHumans.Entity.SuperHuman;
import com.sg.superHumans.Entity.SuperPower;

import java.util.List;

public interface SuperHumanDao {
    SuperHuman getSuperHumanById(int id);
    List<SuperHuman> getAllSuperHumans();
    List<SuperHuman> getSuperHumanForOrganization(Organization organization);
    List<SuperHuman> getSuperHumanPower(SuperPower superPower);
    SuperHuman addSuperHuman(SuperHuman superHuman);
    void updateSuperHuman(SuperHuman superHuman);
    void deleteSuperHuman(int id);
}
