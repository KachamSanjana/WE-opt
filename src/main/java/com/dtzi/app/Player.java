package com.dtzi.app;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import com.dtzi.app.Buffs.Buffs;

public class Player {
  private Gear gear;
  private Skills skills;
  private Food food;
  private Map<String, Float> totalStats;
  private Buffs buffs;
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
      put("hunger", 4f);
      put("entre", 3f);
      put("energy", 3f);
      put("production", 10f);
      put("companies", 2f);
    }
  };
  float CASE_PRICE = 4.7f;

  Player(Gear gear, Skills skills, Buffs buffs, Food food) {
    this.gear = gear;
    this.food = food;
    this.totalStats = StatsMaps.addMaps(gear.getStats(), BASE_STATS);
    this.skills = skills;
    this.buffs = buffs;
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
    Map<String, Float> fullMap = StatsMaps.addMaps(base, gear);
    fullMap = StatsMaps.addMaps(fullMap, skills);
    return fullMap;

  }

  public float[] getDamageEfficiency() {
    Map<String, Float> fullMap = sumStats(BASE_STATS, this.gear.getStats(), this.skills.getStats());

    float[] returnValue = new float[2];
    int hitsOver8h = this.hitsOver8h(fullMap);
    float hitDamage = this.hitDamage(fullMap) * this.buffs.getMultiplier() * this.gear.getWeapon().getAmmo().getBonus();
    float dmgOver8h = hitDamage * hitsOver8h;

    float dodge = fullMap.get("dodge");
    float hunger = fullMap.get("hunger");

    float costOfArmor = this.gear.getArmorCost() * (1 - dodge) / 100;
    float costOfShooting = this.gear.getWeapon().getPrice() / 100 + this.gear.getWeapon().getAmmo().getPrice();
    float hungerRegen = (float) hunger / 10;
    float costOfFood = (hungerRegen*8 + hunger) / hitsOver8h;
    float costPerHit = costOfArmor + costOfShooting + costOfFood + this.buffs.pill().price() / hitsOver8h;

    float lootChance = fullMap.get("lootChance");
    float totalCases = lootChance * hitsOver8h + this.casesOverDebuff(fullMap);
    float ecoSkillIncome = this.ecoSkillIncome(fullMap);

    float hitsPer1k = 1000 / hitDamage;
    float caseReturnPer1k = totalCases / hitsOver8h * hitsPer1k * CASE_PRICE;
    float ecoSkillsReturnPer1k = ecoSkillIncome / hitsOver8h * hitsPer1k;
    float costPer1k = costPerHit * hitsPer1k - caseReturnPer1k - ecoSkillsReturnPer1k;
    // System.out.println(costPerHit * hitsPer1k);
    // System.out.println(caseReturnPer1k + ecoSkillsReturnPer1k);
    // System.out.println(costPer1k);

    returnValue[0] = dmgOver8h;
    returnValue[1] = costPer1k;
    return returnValue;
  }

  public float ecoSkillIncome(Map<String, Float> statsMap) {
    float income;
    float PP_VALUE = 0.087f;
    float prod = statsMap.get("production");
    float energy = statsMap.get("energy");
    float entre = statsMap.get("entre");
    float companyIncome = this.companyIncome(statsMap);
    float entreIncome = prod * entre / 10 * 24 * PP_VALUE;
    float energyIncome = prod * energy / 10 * 24 * PP_VALUE * 0.95f; // tax deducted
    income = companyIncome + entreIncome + energyIncome;  
    return income;

  }

  public float companyIncome (Map<String, Float> statsMap) {
    float income;
    float AVERAGE_COMPANY_LVL = 5.2f;
    float PP_VALUE = 0.087f;
    float companies = statsMap.get("companies");
    float incomePerCompany = PP_VALUE * AVERAGE_COMPANY_LVL;
    income = companies * incomePerCompany * 24;
    return income;
  }

  public float casesOverDebuff(Map<String, Float> statsMap) {
    float cases;
    float lootChance = statsMap.get("lootChance");
    float armor = statsMap.get("armor") - 0.2f;
    float dodge = statsMap.get("dodge") - 0.1f;
    float hpRegen = statsMap.get("health");
    int hitsOverDebuff = (int) Math.floor(hpRegen * 6 / (1-dodge) / (1-armor));
    cases = lootChance * hitsOverDebuff;
    return cases;
  }

  public int hitsOver8h(Map<String, Float> statsMap) {
    int hits;
    float hp = statsMap.get("health");
    float hunger = statsMap.get("hunger");
    float dodge = statsMap.get("dodge");
    float armor = statsMap.get("armor");
    float hpRegen = (float) hp / 10;
    float hungerRegen = (float) hunger / 10;
    float hpOver8h = (hp + hpRegen * 8 + this.food.getHpRestore() * hungerRegen * 8 + this.food.getHpRestore() * hunger)
        / (1 - dodge)
        / (1 - armor);
    hits = (int) Math.floor(hpOver8h);
    return hits;
  }

  public float hitDamage(Map<String, Float> statsMap) {
    float att = statsMap.get("attackDamage");
    float cd = statsMap.get("criticalDamage");
    float cr = statsMap.get("criticalRate");
    float prec = statsMap.get("precision");
    float missedHitDmg = att / 2;
    float successfulHitDmg = att * ((1 - cr) + cr * (1 + cd));
    float hitDmg = missedHitDmg * (1 - prec) + successfulHitDmg * prec;
    return hitDmg;
  }

  private Number[] testUpgrades(Skills skills, String statType) {
    Number[] returnValue = new Number[2];
    int bestUpgradeCount = 0;
    float bestDamageEff = Float.NEGATIVE_INFINITY;
    int skillPoints = skills.getSkillPoints();
    float[] baseDmgEff = this.getDamageEfficiency();
    float baseDmg = baseDmgEff[0];
    float baseCostPer1k = baseDmgEff[1];
    for (int upgradeCount = 0; upgradeCount < 10; upgradeCount++) {
      this.skills.increaseLevel(statType, upgradeCount);
      int skillPointsLeft = this.skills.getSkillPoints();
      if (skillPointsLeft < 0 || this.skills.getUpgradeCost().get(statType) == 10) {
        returnValue[0] = bestDamageEff;
        returnValue[1] = bestUpgradeCount;
        this.skills.decreaseLevel(statType, upgradeCount);
        return returnValue;
      }

      int skillPointsDiff = skillPoints - skillPointsLeft;
      float[] newDmgEff = this.getDamageEfficiency();
      float newDmg = newDmgEff[0];
      float newCostPer1k = newDmgEff[1];
      float relativeDamageIncrease = (1 - newDmg / baseDmg) / skillPointsDiff;
      float relativeCostDecrease = (1 - baseCostPer1k / newCostPer1k) / skillPointsDiff;
      float efficiency = relativeCostDecrease * relativeDamageIncrease;
      if (efficiency > bestDamageEff) {
        bestDamageEff = efficiency;
        bestUpgradeCount = upgradeCount;
      }

      this.skills.decreaseLevel(statType, upgradeCount);
    }
    returnValue[0] = bestDamageEff;
    returnValue[1] = bestUpgradeCount;
    return returnValue;
  }

  private float testUpgrade(Skills skills, String statType) {
    float efficiency;
    float[] baseDmgEff = this.getDamageEfficiency();
    float baseDmg = baseDmgEff[0];
    float baseCostPer1k = baseDmgEff[1];
    int requiredSkillPoints = this.skills.getUpgradeCost().get(statType);
    if (requiredSkillPoints > this.skills.getSkillPoints() || this.skills.getUpgradeCost().get(statType) == 10) {
      return 0;
    }
    this.skills.increaseLevel(statType);
    float[] newDmgEff = this.getDamageEfficiency();
    float newDmg = newDmgEff[0];
    float newCostPer1k = newDmgEff[1];
    float relativeDamageIncrease = (1 - newDmg / baseDmg) / requiredSkillPoints;
    float relativeCostDecrease = (1 - baseCostPer1k / newCostPer1k) / requiredSkillPoints;
    efficiency = relativeCostDecrease * relativeDamageIncrease;
    this.skills.decreaseLevel(statType);
    return efficiency;
  }

  public Map<String, Integer> optimizeSkillPoints() {

    // Termination condition
    int skillPoints = this.skills.getSkillPoints();
    if (StatsMaps.allLargerThan(this.skills.getUpgradeCost(), skillPoints)) {
      Map<String, Integer> upgradeCost = StatsMaps.subtractAll(this.skills.getUpgradeCost(), 1);
      return upgradeCost;
    }

    float largestDamageIncreasePerSkillPoint = Float.NEGATIVE_INFINITY;
    int largestDamageUpgradeCount = 0;
    String largestDamageStat = "";

    for (String statType : this.totalStats.keySet()) {
      switch (statType) {
        case "dodge", "armor":
          Number[] bestUpgrade = this.testUpgrades(this.skills, statType);
          float damageRatio = bestUpgrade[0].floatValue();
          int upgradeCount = bestUpgrade[1].intValue();
          if (damageRatio > largestDamageIncreasePerSkillPoint) {
            largestDamageIncreasePerSkillPoint = damageRatio;
            largestDamageUpgradeCount = upgradeCount;
            largestDamageStat = statType;
          }
          break;
        default:
          damageRatio = this.testUpgrade(this.skills, statType);
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

  public float testEcoSkill (String statType, Map<String, Integer> presetUpgrades) {

    float ratio;
    float[] dmgEff;
    float[] newDmgEff;
    int requiredSkillPoints;

    for (String key : presetUpgrades.keySet()) {
      this.skills.increaseLevel(key, presetUpgrades.get(key));
    }
    requiredSkillPoints = this.skills.getUpgradeCost().get(statType);
    this.optimizeSkillPoints();
    dmgEff = this.getDamageEfficiency();
    float baseDmg = dmgEff[0];
    float baseCost = dmgEff[1];
    if (statType == "companies") {
      System.out.println("Before application: " +
          " stats: " + this.skills.getUpgradeCost() +
          " dmg: " + baseDmg +
          " eff: " + baseCost
          );
    }
    this.skills.resetSkillPoints();

    for (String key : presetUpgrades.keySet()) {
      this.skills.increaseLevel(key, presetUpgrades.get(key));
    }
    this.skills.increaseLevel(statType);
    this.optimizeSkillPoints();
    newDmgEff = this.getDamageEfficiency();
    float newDmg = newDmgEff[0];
    float newCost = newDmgEff[1];
    float relativeDmgDecrease = (1 - newDmg / baseDmg) / requiredSkillPoints;
    float relativeCostDecrease = (1 - baseCost / newCost) / requiredSkillPoints;
    if (statType == "companies") {
      System.out.println("After application: " +
          " stats: " + this.skills.getUpgradeCost() +
          " dmg: " + newDmg +
          " eff: " + newCost
          );
    }
    if (statType == "companies") {
      System.out.println(relativeCostDecrease);
      System.out.println(relativeDmgDecrease);
    }
    if (relativeCostDecrease <= 0 && relativeDmgDecrease <= 0) {
      return 0;
    }
    ratio = relativeCostDecrease / relativeDmgDecrease;
    this.skills.resetSkillPoints();
    return ratio;
  }

  public Map<String, Integer> optimizeLootChance() {
    Set<String> ecoSkills = new HashSet<>() {
      {
        add("lootChance");
        add("entre");
        add("energy");
        add("production");
        add("companies");
      }
    };
    Map<String, Integer> upgrades = new HashMap<>();
    while (true) {
      float largestRatio = Float.NEGATIVE_INFINITY;
      String statType = "";
      for (String stat : ecoSkills) {
        float ratio = this.testEcoSkill(stat, upgrades);
        if (largestRatio == Float.NEGATIVE_INFINITY) {
          largestRatio = ratio;
          statType = stat;
        } else if (largestRatio < 0 && largestRatio > ratio) {
          largestRatio = ratio;
          statType = stat;
        } else if (largestRatio > 0 && largestRatio < ratio) {
          largestRatio = ratio;
          statType = stat;
        }
      }
      
      if (largestRatio == 0) {
        break;
      }

      try {
        upgrades.put(statType, upgrades.get(statType) + 1);
      } catch (NullPointerException e) {
        upgrades.put(statType, 1);
      }
    }
    for (String key : upgrades.keySet()) {
      this.skills.increaseLevel(key, upgrades.get(key));
    }
    this.optimizeSkillPoints();
    return this.skills.getUpgradeCost();
  }
}
