package com.sg.superHumans.Dao;

import com.sg.superHumans.Entity.SuperPower;

import java.util.List;

public interface SuperPowerDao {
    SuperPower getSuperPowerById(int id);
    List<SuperPower> getAllSuperPowers();
    SuperPower addSuperPower(SuperPower superPower);
    void updateSuperPower(SuperPower superPower);
    void deleteSuperPower(int id);

}
