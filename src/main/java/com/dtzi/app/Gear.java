package com.dtzi.app;

import java.rmi.NotBoundException;
import java.util.HashMap;
import java.util.Map;

import com.dtzi.app.Equipment.*;

public class Gear {
  Weapon weapon;
  Helmet helmet;
  Chest chest;
  Gloves gloves;
  Pants pants;
  Boots boots;
  Map<String, Number> stats = new HashMap<>() {
    {
      put("attackDamage", 0f);
      put("criticalRate", 0f);
      put("criticalDamage", 0f);
      put("armor", 0f);
      put("precision", 0f);
      put("dodge", 0f);
    }
  };
  float totalCost = 0;

  public Gear() {
  }

  void swap(Weapon newWeapon) {
    stats.put("attackDamage", newWeapon.getAttackDamage());
    stats.put("criticalRate", newWeapon.getCriticalRate());
  }

  void swap(Helmet newHelmet) {
    String statType = newHelmet.getSTAT_TYPE();
    stats.put(statType, newHelmet.getStat());
  }

  void swap(Chest newChest) {
    String statType = newChest.getSTAT_TYPE();
    try {
      stats.put(statType, this.stats.get(statType).floatValue() - this.chest.getStat() + newChest.getStat());
    } catch (NullPointerException e) {
      stats.put(statType, this.stats.get(statType).floatValue() + newChest.getStat());
    }
  }

  void swap(Gloves newGloves) {
    String statType = newGloves.getSTAT_TYPE();
    stats.put(statType, newGloves.getStat());
  }

  void swap(Pants newPants) {
    String statType = newPants.getSTAT_TYPE();
    try {
      stats.put(statType, this.stats.get(statType).floatValue() - this.pants.getStat() + newPants.getStat());
    } catch (NullPointerException e) {
      stats.put(statType, this.stats.get(statType).floatValue() + newPants.getStat());
    }
  }

  void swap(Boots newBoots) {
    String statType = newBoots.getSTAT_TYPE();
    stats.put(statType, newBoots.getStat());
  }

  public void setHelmet(Helmet newHelmet) {
    this.swap(newHelmet);
    this.helmet = newHelmet;
  }

  public void setChest(Chest newChest) {
    this.swap(newChest);
    this.chest = newChest;
  }

  public void setGloves(Gloves newGloves) {
    this.swap(newGloves);
    this.gloves = newGloves;
  }

  public void setPants(Pants newPants) {
    this.swap(newPants);
    this.pants = newPants;
  }

  public void setBoots(Boots newBoots) {
    this.swap(newBoots);
    this.boots = newBoots;
  }

  public void setWeapon(Weapon newWeapon) {
    this.swap(newWeapon);
    this.weapon = newWeapon;
  }

  public Map<String, Number> getStats() {
    return stats;
  }
}
