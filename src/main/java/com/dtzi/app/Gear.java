package com.dtzi.app;

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
  Map<String, Integer> stats = new HashMap<>() {{
    put("attackDamage", 0);
    put("criticalRate", 0);
    put("criticalDamage", 0);
    put("armor", 0);
    put("precision", 0);
    put("dodge", 0);
  }};
  float totalCost; // to represent the price in the future

  public Gear() {
  }

  void swap(Weapon oldWeapon, Weapon newWeapon) {
    try {
      stats.put("attackDamage", this.stats.get("attackDamage") - oldWeapon.getAttackDamage() + newWeapon.getAttackDamage());
      stats.put("criticalRate", this.stats.get("criticalRate") - oldWeapon.getCriticalRate() + newWeapon.getCriticalRate());
    } catch (NullPointerException e) {
      stats.put("attackDamage", this.stats.get("attackDamage") + newWeapon.getAttackDamage());
      stats.put("criticalRate", this.stats.get("criticalRate") + newWeapon.getCriticalRate());
    }
  }

  void swap(Helmet oldHelmet, Helmet newHelmet) {
    String statType = newHelmet.getStatType();
    try {
      stats.put(statType, this.stats.get(statType) - oldHelmet.getStat() + newHelmet.getStat());
    } catch (NullPointerException e) {
      stats.put(statType, this.stats.get(statType) + newHelmet.getStat());
    }
  }
  void swap(Chest oldChest, Chest newChest) {
    String statType = newChest.getStatType();
    try {
      stats.put(statType, this.stats.get(statType) - oldChest.getStat() + newChest.getStat());
    } catch (NullPointerException e) {
      stats.put(statType, this.stats.get(statType) + newChest.getStat());
    }
  }
  void swap(Gloves oldGloves, Gloves newGloves) {
    String statType = newGloves.getStatType();
    try {
      stats.put(statType, this.stats.get(statType) - oldGloves.getStat() + newGloves.getStat());
    } catch (NullPointerException e) {
      stats.put(statType, this.stats.get(statType) + newGloves.getStat());
    }
  }
  void swap(Pants oldPants, Pants newPants) {
    String statType = newPants.getStatType();
    try {
      stats.put(statType, this.stats.get(statType) - oldPants.getStat() + newPants.getStat());
    } catch (NullPointerException e) {
      stats.put(statType, this.stats.get(statType) + newPants.getStat());
    }
  }

  void swap(Boots oldBoots, Boots newBoots) {
    String statType = newBoots.getStatType();
    try {
      stats.put(statType, this.stats.get(statType) - oldBoots.getStat() + newBoots.getStat());
    } catch (NullPointerException e) {
      stats.put(statType, this.stats.get(statType) + newBoots.getStat());
    }
  }
  public void setHelmet(Helmet newHelmet) {
      this.swap(this.helmet, newHelmet);
      this.helmet = newHelmet;
  }
  public void setChest(Chest newChest) {
    this.swap(this.chest, newChest);
    this.chest = newChest;
  }
  public void setGloves(Gloves newGloves) {
    this.swap(this.gloves, newGloves);
    this.gloves = newGloves;
  }
  public void setPants(Pants newPants) {
    this.swap(this.pants, newPants);
    this.pants = newPants;
  }
  public void setBoots(Boots newBoots) {
    this.swap(this.boots, newBoots);
    this.boots = newBoots;
  }
  public void setWeapon(Weapon newWeapon) {
    this.swap(this.weapon, newWeapon);
    this.weapon = newWeapon;
  }
  public Map<String, Integer> getStats() {
    return stats;
    }
}
