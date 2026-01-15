package com.dtzi.app;

import java.util.Map;
import java.util.HashMap;
import com.dtzi.app.Buffs.Buffs;
import com.dtzi.app.Equipment.*;
import com.dtzi.app.Skills.SkillPointException;

public class Player {
  private Gear gear;
  private Skills skills;
  private Food food;
  private Map<String, Float> totalStats;
  private Buffs buffs;
  private final int DEPTH = 5;
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

  Player(Skills skills, Buffs buffs, Food food) {
    this.food = food;
    this.skills = skills;
    this.totalStats = StatsMaps.addMaps(BASE_STATS, skills.getStats());
    this.buffs = buffs;
  }

  void setGear(Gear gear) {
    if (this.gear != null) {
      this.totalStats = StatsMaps.subtractMaps(this.totalStats, this.gear.getStats());
      this.totalStats = StatsMaps.addMaps(this.totalStats, gear.getStats());
      this.gear = gear;
    } else {
      this.totalStats = StatsMaps.addMaps(this.totalStats, gear.getStats());
      this.gear = gear;
    }
  }

  void resetSkills() {
    this.totalStats = StatsMaps.subtractMaps(this.totalStats, this.skills.getStats());
    skills.reset();
  }

  Gear getGear() {
    return this.gear;
  }

  Skills getSkills() {
    return this.skills;
  }

  private void increaseSkillLevel(String skill) throws SkillPointException {
    this.skills.increaseLevel(skill);
    switch (skill) {
      case "armor":
        this.totalStats.put(skill, (float) Math.min(0.9, this.totalStats.get(skill) + this.skills.getStatIncrements().get(skill)));
        break;
      case "precision", "criticalRate":
        this.totalStats.put(skill, (float) Math.min(1, this.totalStats.get(skill) + this.skills.getStatIncrements().get(skill)));
        break;
      default:
        this.totalStats.put(skill, this.totalStats.get(skill) + this.skills.getStatIncrements().get(skill));
        break;
    }
  }

  private void decreaseSkillLevel(String skill) {
    this.skills.decreaseLevel(skill);
    float stat = this.totalStats.get(skill);
    int upgradeCost = this.skills.getUpgradeCost().get(skill);
    float increment = this.skills.getStatIncrements().get(skill);
    switch (skill) {
      case "armor":
        this.totalStats.put(skill, (float) Math.min(0.9, BASE_STATS.get(skill) + this.getGear().getStats().get(skill) + this.skills.getStats().get(skill)));
        break;
      case "precision", "criticalRate":
        this.totalStats.put(skill, (float) Math.min(1, BASE_STATS.get(skill) + this.getGear().getStats().get(skill) + this.skills.getStats().get(skill)));
        break;
      default:
        this.totalStats.put(skill, stat - increment);
        break;
    }
  }


  private float costOfFood(float hunger, float hitsOver8h) {
    float hungerRegen = (float) hunger / 10;
    return (hungerRegen * 8 + hunger) / hitsOver8h * this.food.getPrice();
  }

  private float costOfArmor(float dodge) {
    return this.gear.getArmorCost() * (1 - dodge) / 100;
  }

  private float costOfShooting() {
    return this.gear.getWeapon().getPrice() / 100 + this.gear.getWeapon().getAmmo().getPrice();
  }

  public float costPerHit(Map<String, Float> statsMap, float hitsOver8h) {
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
    return lootChance * hitsOver8h + this.getCasesOverDebuff(statsMap);
  }

  public float[] getDamageEfficiency() {
    Map<String, Float> statsMap = this.totalStats;

    float[] returnValue = new float[2];
    int hitsOver8h = this.hitsOver8h(statsMap);
    float hitDamage = this.hitDamage(statsMap) * this.buffs.getMultiplier() * this.gear.getWeapon().getAmmo().getBonus();
    float dmgOver8h = hitDamage * hitsOver8h;

    float costPerHit = this.costPerHit(statsMap, hitsOver8h);
    float totalCases = this.totalCases(statsMap, hitsOver8h);
    float ecoSkillIncome = this.ecoSkillIncome(statsMap);
    float damageIncome = dmgOver8h * 0.001f * 0.25f;
    float missionIncome = 10 + 4.285f;

    float hitsPer1k = 1000 / hitDamage;
    float caseReturnPer1k = totalCases / hitsOver8h * hitsPer1k * CASE_PRICE;
    float ecoSkillsReturnPer1k = ecoSkillIncome / hitsOver8h * hitsPer1k;
    float damageIncomePer1k = damageIncome / hitsOver8h * hitsPer1k;
    float missionIncomePer1k = missionIncome / hitsOver8h * hitsPer1k;
    float costPer1k = costPerHit * hitsPer1k - caseReturnPer1k - ecoSkillsReturnPer1k - damageIncomePer1k
        - missionIncomePer1k;
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
    float entreIncome = prod * entre * 0.1f * 24 * PP_VALUE;
    float energyIncome = prod * energy * 0.1f * 24 * PP_VALUE * 0.9f; // tax deducted
    income = companyIncome + entreIncome + energyIncome;
    return income;

  }

  public float companyIncome(Map<String, Float> statsMap) {
    float income;
    float AVERAGE_COMPANY_LVL = 5.3333f;
    float PP_VALUE = 0.087f;
    float companies = statsMap.get("companies");
    float incomePerCompany = PP_VALUE * AVERAGE_COMPANY_LVL;
    income = companies * incomePerCompany * 24;
    return income;
  }

  public float getCasesOverDebuff(Map<String, Float> statsMap) {
    float cases;

    Map<String, Float> gearStats = this.gear.getStats();

    float eqArmor = gearStats.get("armor");
    float eqDodge = gearStats.get("dodge");
    float armor = statsMap.get("armor") - eqArmor;
    float dodge = statsMap.get("dodge") - eqDodge;
    float hpRegen = statsMap.get("health");
    int hitsOverDebuff = (int) Math.floor(hpRegen * 6 / (1 - dodge) / (1 - armor));

    float lootChance = statsMap.get("lootChance");
    cases = lootChance * hitsOverDebuff;
    return cases;
  }

  public int hitsOver8h(Map<String, Float> statsMap) {
    int hits;
    int HOURS = 8;
    float hp = statsMap.get("health");
    float hunger = statsMap.get("hunger");
    float dodge = statsMap.get("dodge");
    float armor = statsMap.get("armor");
    float hpRegen = (float) hp * 0.1f;
    float hungerRegen = (float) hunger * 0.1f;
    float hpRestore = this.food.getHpRestore();
    float hpOver8h = (hp + hpRegen * HOURS + hpRestore * hungerRegen * HOURS + hpRestore * hunger)
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
    float missedHitDmg = att * 0.5f;
    float successfulHitDmg = att * ((1 - cr) + cr * (1 + cd));
    float hitDmg = missedHitDmg * (1 - prec) + successfulHitDmg * prec;
    return hitDmg;
  }

  public float getScore(int skillPoints) {
    float[] dmgEff = this.getDamageEfficiency();
    float dmgOver8h = dmgEff[0];
    float costPer1k = dmgEff[1];
    if (costPer1k < 0) {
        float multiplier = 1 - costPer1k / skillPoints;
      return dmgOver8h * multiplier;
    } else {
      float divisor = 1 + costPer1k * skillPoints;
      return dmgOver8h / divisor;
    }
  }

  private class PruneResult {
    float score;
    String statType;

    PruneResult(float score, String statType) {
      this.score = score;
      this.statType = statType;
    }
  }
  public Map<String, Float> getTotalStats() {
    return this.totalStats;
  }

  private PruneResult pruneRecursive(PruneResult best, int depth, int requiredSkillPoints) {
    for (String statType : this.totalStats.keySet()) {
      PruneResult result;
      try {
        int sp = this.skills.getUpgradeCost().get(statType);
        this.increaseSkillLevel(statType);

        float score = this.getScore(requiredSkillPoints + sp);

        if (depth == 0) {
          result = new PruneResult(score, statType);
        } 
        else if (best.score * 0.9f / depth > score) {
          this.decreaseSkillLevel(statType);
          continue;
        }
        else {
          result = this.pruneRecursive(best, depth - 1, requiredSkillPoints + sp);
        }
        if (result.score > best.score) {
          best = new PruneResult(result.score, statType);
        }
        this.decreaseSkillLevel(statType);
      } catch (SkillPointException e) {
        if (depth < this.DEPTH) {
          float score = this.getScore(requiredSkillPoints);
          result = new PruneResult(score, statType);
          if (result.score > best.score) {
            best = new PruneResult(result.score, statType);
          }
        }
        // System.out.println(e.getMessage());
      }
    }
    return best;
  }

  public Map<String, Integer> optimizeSkillPoints() {
    // Termination condition
    float largestScore = Float.NEGATIVE_INFINITY;
    String bestStatType = "";
    PruneResult initial = new PruneResult(largestScore, bestStatType);
    int depth = DEPTH;
    int requiredSkillPoints = 0;
    PruneResult returnValue = this.pruneRecursive(initial, depth, requiredSkillPoints);
    bestStatType = returnValue.statType;
    if (bestStatType.isEmpty()) {
      Map<String, Integer> upgradeCost = StatsMaps.subtractAll(this.skills.getUpgradeCost(), 1);
      return upgradeCost;
    }
    try {
      this.increaseSkillLevel(bestStatType);
    } catch (SkillPointException e) {
      // should not happen?
      System.out.println(e.getMessage());
    }
    return optimizeSkillPoints();
  }
}
