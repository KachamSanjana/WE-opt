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
  private static Map<String, Float> BASE_STATS = new HashMap<>() {
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

  private float costOfFood(float hunger, float hitsOver8h) {
    float hungerRegen = (float) hunger/10;
    return (hungerRegen * 8 + hunger) / hitsOver8h * this.food.getPrice();
  }
  private float costOfArmor(float dodge) {
    return this.gear.getArmorCost() * (1 - dodge) / 100;
  }
  private float costOfShooting() {
    return this.gear.getWeapon().getPrice() / 100 + this.gear.getWeapon().getAmmo().getPrice();
  }

  public float getCostPerHit(Map<String, Float> statsMap, float hitsOver8h) {
    float costPerHit;

    float hunger = statsMap.get("hunger");
    float dodge = statsMap.get("dodge");
    float costOfFood = this.costOfFood(hunger, hitsOver8h);
    float costOfArmor = this.costOfArmor(dodge);
    float costOfShooting = this.costOfShooting();
    costPerHit = costOfArmor + costOfShooting + costOfFood + this.buffs.pill().price() / hitsOver8h;

    return costPerHit;
  }

  private float totalCases(Map<String, Float> statsMap, float hitsOver8h) {
    float lootChance = statsMap.get("lootChance");
    return lootChance * hitsOver8h * this.getCasesOverDebuff(statsMap);
  }

  public float[] getDamageEfficiency() {
    Map<String, Float> fullMap = sumStats(BASE_STATS, this.gear.getStats(), this.skills.getStats());

    float[] returnValue = new float[2];
    int hitsOver8h = this.hitsOver8h(fullMap);
    float hitDamage = this.hitDamage(fullMap) * this.buffs.getMultiplier() * this.gear.getWeapon().getAmmo().getBonus();
    float dmgOver8h = hitDamage * hitsOver8h;

    float costPerHit = this.getCostPerHit(fullMap, hitsOver8h);
    float totalCases = this.totalCases(fullMap, hitsOver8h);
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

  public float getCasesOverDebuff(Map<String, Float> statsMap) {
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

  private Number[] prune() {
    for (String statType : this.totalStats.
  }

  public Map<String, Integer> optimizeSkillPoints() {
    this.totalStats = sumStats(BASE_STATS, this.gear.getStats(), this.skills.getStats());

    // Termination condition
    int skillPoints = this.skills.getSkillPoints();
    if (StatsMaps.allLargerThan(this.skills.getUpgradeCost(), skillPoints)) {
      Map<String, Integer> upgradeCost = StatsMaps.subtractAll(this.skills.getUpgradeCost(), 1);
      return upgradeCost;
    }
    return optimizeSkillPoints();
  }
}
