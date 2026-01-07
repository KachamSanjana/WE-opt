package com.dtzi.app;

import java.util.HashMap;
import java.util.Map;

import com.dtzi.app.Equipment.*;

public class Gear {
  private Weapon weapon;
  private Helmet helmet;
  private Chest chest;
  private Gloves gloves;
  private Pants pants;
  private Boots boots;
  Map<String, Float> stats = new HashMap<>() {
    {
      put("attackDamage", 0f);
      put("criticalRate", 0f);
      put("criticalDamage", 0f);
      put("armor", 0f);
      put("precision", 0f);
      put("dodge", 0f);
      put("health", 0f);
      put("lootChance", 0f);
      put("hunger", 0f);
    }
  };
  float armorCost = 0f;

  public Gear() {
  }

  void swap(Weapon newWeapon) {
    stats.put("attackDamage", newWeapon.getAttackDamage());
    stats.put("criticalRate", newWeapon.getCriticalRate());
  }

  void swap(Helmet newHelmet) {
    try {
    this.armorCost = this.armorCost - this.helmet.getPrice() + newHelmet.getPrice();
    } catch (NullPointerException e) {
      this.armorCost = this.armorCost + newHelmet.getPrice();
    }
    String statType = newHelmet.getStatType();
    stats.put(statType, newHelmet.getStat());
  }

  void swap(Chest newChest) {
    String statType = newChest.getStatType();
    try {
      this.armorCost = this.armorCost - this.chest.getPrice() + newChest.getPrice();
      stats.put(statType, this.stats.get(statType).floatValue() - this.chest.getStat() + newChest.getStat());
    } catch (NullPointerException e) {
      this.armorCost = this.armorCost + newChest.getPrice();
      stats.put(statType, this.stats.get(statType).floatValue() + newChest.getStat());
    }
  }

  void swap(Gloves newGloves) {
    String statType = newGloves.getStatType();
    try {
      this.armorCost = this.armorCost - this.gloves.getPrice() + newGloves.getPrice();
    } catch (NullPointerException e) {
      this.armorCost = this.armorCost + newGloves.getPrice();
    }
    stats.put(statType, newGloves.getStat());
  }

  void swap(Pants newPants) {
    String statType = newPants.getStatType();
    try {
      this.armorCost = this.armorCost - this.pants.getPrice() + newPants.getPrice();
      stats.put(statType, this.stats.get(statType).floatValue() - this.pants.getStat() + newPants.getStat());
    } catch (NullPointerException e) {
      this.armorCost = this.armorCost + newPants.getPrice();
      stats.put(statType, this.stats.get(statType).floatValue() + newPants.getStat());
    }
  }

  void swap(Boots newBoots) {
    try {
    this.armorCost = this.armorCost - this.boots.getPrice() + newBoots.getPrice();
    } catch (NullPointerException e) {
      this.armorCost = this.armorCost + newBoots.getPrice();
    }
    String statType = newBoots.getStatType();
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

  public Weapon getWeapon() {
    return this.weapon;
  }
  public Helmet getHelmet() {
    return this.helmet;
  }
  public Chest getChest() {
    return this.chest;
  }
  public Gloves getGloves() {
    return this.gloves;
  }
  public Pants getPants() {
    return this.pants;
  }
  public Boots getBoots() {
    return this.boots;
  }

  public Map<String, Float> getStats() {
    return this.stats;
  }

  public float getArmorCost() {
    return this.armorCost;
  }
}
