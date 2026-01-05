package com.dtzi.app;

import java.util.Map;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.dtzi.app.StatsMaps;

public class Player {
  private Gear gear;
  private Skills skills;
  private int foodQuality;
  private Map<String, Float> totalStats;
  static Map<String, Float> BASE_STATS = new HashMap<>() {
    {
      put("attackDamage", 100f);
      put("criticalRate", 0.1f);
      put("criticalDamage", 1f);
      put("armor", 0f);
      put("precision", 0.5f);
      put("dodge", 0f);
      put("health", 5f);
      put("lootChance", 0.05f);
      put("hunger", 5f);
    }
  };

  Player(Gear gear, int sp, int food) {
    this.gear = gear;
    this.foodQuality = food / 10;
    this.totalStats = StatsMaps.addMaps(gear.getStats(), BASE_STATS);
    this.skills = new Skills(sp);
  }

  void setGear(Gear gear) {
    this.gear = gear;
  }

  Gear getGear() {
    return this.gear;
  }

  void setSkillPoints(Skills sp) {
    this.skills = sp;
  }

  Skills getSkillPoints() {
    return this.skills;
  }

  private Map<String, Float> sumStats(Map<String, Float> base, Map<String, Float> gear, Map<String, Float> skills) {
    Map<String,Float> fullMap = StatsMaps.addMaps(base, gear);
    fullMap = StatsMaps.addMaps(fullMap, skills);
    return fullMap;

  }

  public float calcDamage() {
    Map<String,Float> fullMap = sumStats(BASE_STATS, this.gear.getStats(), this.skills.getStats());
    float DMG_OVER_8H;
    int ATT = fullMap.get("attackDamage").intValue();
    float CD = fullMap.get("criticalDamage").floatValue();
    float CR = fullMap.get("criticalRate").floatValue();
    float PREC = fullMap.get("precision").floatValue();
    float ARMOR = fullMap.get("armor").floatValue();
    float DODGE = fullMap.get("dodge").floatValue();
    int HP = fullMap.get("health").intValue();
    int HUNGER = fullMap.get("hunger").intValue();
    float HP_REGEN = (float) HP / 10; // Integer division ...
    float HUNGER_REGEN = (float) HUNGER / 10;
    float MISSED_HIT = ATT / 2;
    float SUCCESSFUL_HIT = ATT * ((1 - CR) + CR * (1 + CD));
    float DMG_PER_HIT = MISSED_HIT * (1 - PREC) + SUCCESSFUL_HIT * PREC;
    float TOTAL_HP_OVER_8H = (HP + HP_REGEN * 8 + this.foodQuality * HUNGER_REGEN * 8 + this.foodQuality * HUNGER)
        / (1 - DODGE)
        / (1 - ARMOR);
    int HITS_OVER_8H = (int) Math.floor(TOTAL_HP_OVER_8H);
    DMG_OVER_8H = DMG_PER_HIT * HITS_OVER_8H;
    return DMG_OVER_8H;
  }
  

  private List<Number> testUpgrades(Skills skills, String statType) {
    int largestDamageUpgradeCount = 0;
    float largestDamageIncreasePerSkillPoint = Float.NEGATIVE_INFINITY;
    int skillPoints = skills.getSkillPoints();
    float base_dmg = this.calcDamage();
    for (int upgradeCount = 1; upgradeCount < 11; upgradeCount++) {

      this.skills.increaseLevel(statType, upgradeCount);
      int skillPointsLeft = this.skills.getSkillPoints();

      if (skillPointsLeft < 0) {
        this.skills.decreaseLevel(statType, upgradeCount);
        return Arrays.asList(largestDamageIncreasePerSkillPoint, largestDamageUpgradeCount);
      }

      int skillPointsDiff = skillPoints - skillPointsLeft;
      float damageDiff = this.calcDamage() - base_dmg;
      float damageIncreasePerSkillPoint = damageDiff / skillPointsDiff;
      if (damageIncreasePerSkillPoint > largestDamageIncreasePerSkillPoint) {
        largestDamageIncreasePerSkillPoint = damageIncreasePerSkillPoint;
        largestDamageUpgradeCount = upgradeCount;
      }

      this.skills.decreaseLevel(statType, upgradeCount);
    }
    return Arrays.asList(largestDamageIncreasePerSkillPoint, largestDamageUpgradeCount);
  }

  private float testUpgrade(Skills skills, String statType) {
    float base_dmg = this.calcDamage();
    int requiredSkillPoints = this.skills.getUpgradeCost().get(statType);
    this.skills.increaseLevel(statType);
    float dmg_over_8h = this.calcDamage();
    float damageIncreasePerSkillPoint = (dmg_over_8h - base_dmg) / requiredSkillPoints;
    this.skills.decreaseLevel(statType);
      if (statType == "hunger") {
        System.out.println(damageIncreasePerSkillPoint);
      }
    return damageIncreasePerSkillPoint;
  }

  public Map<String, Integer> optimizeSkillPoints() {

    // Termination condition
    int skillPoints = this.skills.getSkillPoints();
    if (StatsMaps.allLargerThan(this.skills.getUpgradeCost(), skillPoints)) {
      Map<String, Integer> upgradeCost = StatsMaps.subtractAll(this.skills.getUpgradeCost(), 1); // represents the actual upgrades costs.
      return upgradeCost;
    }

    float largestDamageIncreasePerSkillPoint = Float.NEGATIVE_INFINITY;
    int largestDamageUpgradeCount = 0;
    String largestDamageStat = "";

    for (String statType : this.totalStats.keySet()) {
      System.out.println(statType);
      switch (statType) {
        case "dodge", "armor":
          List<Number> bestUpgrade = this.testUpgrades(this.skills, statType);
          float damageRatio = bestUpgrade.get(0).floatValue();
          int upgradeCount = bestUpgrade.get(1).intValue();
          if (damageRatio > largestDamageIncreasePerSkillPoint) {
            largestDamageIncreasePerSkillPoint = damageRatio;
            largestDamageUpgradeCount = upgradeCount;
            largestDamageStat = statType;
          }
          break;
        default:
          damageRatio = this.testUpgrade(skills, statType);
          if (damageRatio > largestDamageIncreasePerSkillPoint) {
            largestDamageIncreasePerSkillPoint = damageRatio;
            largestDamageStat = statType;
          }
      }
    }

    switch (largestDamageStat) {
      case "armor", "dodge":
        this.skills.increaseLevel(largestDamageStat, largestDamageUpgradeCount);
        break;
      default:
        this.skills.increaseLevel(largestDamageStat);
    }
    return optimizeSkillPoints();
  }
}
