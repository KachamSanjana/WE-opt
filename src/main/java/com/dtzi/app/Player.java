package com.dtzi.app;

import java.util.Map;
import java.util.HashMap;

public class Player {
  Gear gear;
  Skills sp;
  Map<String, Number> basePlusGear;
  static Map<String, Number> BASE_STATS = new HashMap<String, Number>() {
    {
      put("attackDamage", 100);
      put("criticalRate", 0.1f);
      put("criticalDamage", 1f);
      put("armor", 0f);
      put("precision", 0.5f);
      put("dodge", 0f);
      put("health", 5);
      put("lootChance", 0.05f);
      put("hunger", 5);
    }
  };

  Player(Gear gear, int sp) {
    this.gear = gear;
    basePlusGear = this.addGearStatsToBase();
    this.sp = new Skills(sp, basePlusGear);
  }

  Map<String, Number> addGearStatsToBase() {
    Map<String, Number> gearStats = this.gear.getStats();
    try {
      this.basePlusGear.clear();
    } catch (NullPointerException e) {
      System.out.println(e.getMessage()); 
      e.printStackTrace();
    }
    this.basePlusGear.put("health", 50);
    this.basePlusGear.put("lootChance", 0.05f);
    this.basePlusGear.put("hunger", 5);
    for (String elem : gearStats.keySet()) {
      switch (elem) {
        case "attackDamage":
          this.basePlusGear.put(elem, BASE_STATS.get(elem).intValue() + gearStats.get(elem).intValue());
          break;
        case "criticalDamage":
          this.basePlusGear.put(elem, BASE_STATS.get(elem).floatValue() + gearStats.get(elem).floatValue());
          break;
        case "armor":
          this.basePlusGear.put(elem,
              Math.max(0, Math.min(0.9, BASE_STATS.get(elem).floatValue() + gearStats.get(elem).floatValue())));
          break;
        default:
          this.basePlusGear.put(elem,
              Math.max(0, Math.min(1, BASE_STATS.get(elem).floatValue() + gearStats.get(elem).floatValue())));
      }
    }
    return basePlusGear;
  }

  void setGear(Gear gear) {
    this.gear = gear;
    Map<String, Number> basePlusGear = this.addGearStatsToBase();
  }

  Gear getGear() {
    return this.gear;
  }

  void setSkillPoints(Skills sp) {
    this.sp = sp;
  }

  Skills getSkillPoints() {
    return this.sp;
  }

 //  public float calcDamage(int food) {
 //    float DMG_OVER_8H;
 //    int ATT = this.stats.get("attackDamage").intValue();
 //    float CD = this.stats.get("criticalDamage").floatValue();
 //    float CR = this.stats.get("criticalRate").floatValue();
 //    float PREC = this.stats.get("precision").floatValue();
 //    float ARMOR = this.stats.get("armor").floatValue();
 //    float DODGE = this.stats.get("dodge").floatValue();
 //    int HP = this.stats.get("health").intValue();
 //    int HUNGER = this.stats.get("hunger").intValue();
 //    float HP_REGEN = HP / 10;
 //    float HUNGER_REGEN = HUNGER / 10;
 //    float MISSED_HIT = ATT / 2;
 //    float SUCCESSFUL_HIT = ATT * ((1 - CR) + CR * (1 + CD));
 //    float DMG_PER_HIT = MISSED_HIT * (1 - PREC) + SUCCESSFUL_HIT * PREC;
 //    float TOTAL_HP_OVER_8H = (float) (HP + HP_REGEN * 8 + food * HUNGER_REGEN * 8 + food * HUNGER) / (1 - DODGE)
 //        / (1 - ARMOR);
 //    int HITS_OVER_8H = (int) Math.floor(TOTAL_HP_OVER_8H / 10);
 //    DMG_OVER_8H = DMG_PER_HIT * HITS_OVER_8H;
 //    return DMG_OVER_8H;
 //  }

} 
