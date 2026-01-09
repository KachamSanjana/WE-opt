package com.dtzi.app;

import java.util.HashMap;
import java.util.Map;

public class Skills {
  public class NotEnoughSkillPointsException extends Exception {
    NotEnoughSkillPointsException(String message) {
      super(message);
    }
  }
  public class LevelTooHighException extends Exception {
    LevelTooHighException(String message) {
      super(message);
    }
  }
  private Map<String, Float> stats = new HashMap<String, Float>() {
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
      put("entre", 0f);
      put("energy", 0f);
      put("production", 0f);
      put("companies", 0f);
    }
  };
  private final Map<String, Float> statIncrements = new HashMap<String, Float>() {
    {
      put("attackDamage", 20f);
      put("criticalRate", 0.05f);
      put("criticalDamage", 0.2f);
      put("armor", 0.04f);
      put("precision", 0.05f);
      put("dodge", 0.04f);
      put("health", 1f);
      put("lootChance", 0.01f);
      put("hunger", 1f);
      put("entre", 0.5f);
      put("energy", 1f);
      put("production", 3f);
      put("companies", 1f);
    }
  };
  private Map<String, Integer> upgradeCost = new HashMap<>() {
    {
      put("attackDamage", 1);
      put("criticalRate", 1);
      put("criticalDamage", 1);
      put("armor", 1);
      put("precision", 1);
      put("dodge", 1);
      put("health", 1);
      put("lootChance", 1);
      put("hunger", 1);
      put("entre", 1);
      put("energy", 1);
      put("production", 1);
      put("companies", 1);
    }
  };
  private int skillPoints;
  public int originalSkillPoints;

  public Skills(int sp) {
    this.skillPoints = sp;
    this.originalSkillPoints = sp;
  }

  public void increaseLevel(String statType) throws NotEnoughSkillPointsException, LevelTooHighException {
    if (this.upgradeCost.get(statType) > 10) {
      throw new LevelTooHighException("Current level: " + this.upgradeCost.get(statType));
    }
    this.skillPoints = this.skillPoints - this.upgradeCost.get(statType);
    if (this.skillPoints < 0) {
      this.skillPoints = this.skillPoints + this.upgradeCost.get(statType);
      throw new NotEnoughSkillPointsException("Available skill points: " + this.skillPoints + "\n" +
          "Necessary skill points: " + this.upgradeCost.get(statType));
    }
    switch (statType) {
      case "attackDamage", "health", "hunger", "criticalDamage", "companies", "production", "energy", "entre":
        this.stats.put(statType,
            this.stats.get(statType) + this.statIncrements.get(statType));
        break;
      case "armor":
        this.stats.put(statType,
            (float) Math.min(0.9, this.stats.get(statType) + this.statIncrements.get(statType)));
        break;
      default:
        this.stats.put(statType,
            (float) Math.min(1, this.stats.get(statType) + this.statIncrements.get(statType)));
    }
    this.upgradeCost.put(statType, this.upgradeCost.get(statType) + 1);
  }

  public void decreaseLevel(String statType) {
    this.skillPoints = this.skillPoints + this.upgradeCost.get(statType) - 1;
    switch (statType) {
      case "attackDamage", "health", "hunger", "criticalDamage", "companies", "production", "energy", "entre":
        this.stats.put(statType,
            this.stats.get(statType) - this.statIncrements.get(statType));
        break;
      case "armor":
        this.stats.put(statType,
            (float) Math.min(0.9, this.stats.get(statType) - this.statIncrements.get(statType)));
        break;
      default:
        this.stats.put(statType,
            (float) Math.min(1,
                this.stats.get(statType).floatValue() - this.statIncrements.get(statType).floatValue()));
    }
    this.upgradeCost.put(statType, this.upgradeCost.get(statType) - 1);
  }

  public Map<String, Float> getStats() {
    return this.stats;
  }

  public Map<String, Float> getStatIncrements() {
    return this.statIncrements;
  }

  public Map<String, Integer> getUpgradeCost() {
    return this.upgradeCost;
  }

  public int getSkillPoints() {
    return this.skillPoints;
  }

  public void resetSkillPoints() {
    this.stats = new HashMap<String, Float>() {
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
        put("entre", 0f);
        put("energy", 0f);
        put("production", 0f);
        put("companies", 0f);
      }
    };
    this.upgradeCost = new HashMap<>() {
      {
        put("attackDamage", 1);
        put("criticalRate", 1);
        put("criticalDamage", 1);
        put("armor", 1);
        put("precision", 1);
        put("dodge", 1);
        put("health", 1);
        put("lootChance", 1);
        put("hunger", 1);
        put("entre", 1);
        put("energy", 1);
        put("production", 1);
        put("companies", 1);
      }
    };
    this.skillPoints = originalSkillPoints;
  }
}
