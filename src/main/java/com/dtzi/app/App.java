package com.dtzi.app;

import com.dtzi.app.Equipment.*;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.dtzi.app.Buffs.*;

/**
 * Hello world!
 */
public class App {
  public static Weapon[] generateWeapons(Ammo[] ammos) {
    Weapon[] weapons = new Weapon[6];
    Map<String, Map<String, Float>> possibleStats = new HashMap<>() {
      {
        put("grey", new HashMap<String, Float>() {
          {
            put("attackDamage", 37f);
            put("criticalRate", 0.05f);
            put("price", 2f);
          }
        });
        put("green", new HashMap<String, Float>() {
          {
            put("attackDamage", 59f);
            put("criticalRate", 0.1f);
            put("price", 6f);
          }
        });
        put("blue", new HashMap<String, Float>() {
          {
            put("attackDamage", 87f);
            put("criticalRate", 0.15f);
            put("price", 25f);
          }
        });
        put("purple", new HashMap<String, Float>() {
          {
            put("attackDamage", 117f);
            put("criticalRate", 0.20f);
            put("price", 100f);
          }
        });
        put("yellow", new HashMap<String, Float>() {
          {
            put("attackDamage", 155f);
            put("criticalRate", 0.29f);
            put("price", 200f);
          }
        });
        put("red", new HashMap<String, Float>() {
          {
            put("attackDamage", 265f);
            put("criticalRate", 0.39f);
            put("price", 600f);
          }
        });
      }
    };
    int i = 0;
    for (Map<String, Float> weaponStats : possibleStats.values()) {
      float attackDamage = weaponStats.get("attackDamage");
      float criticalRate = weaponStats.get("criticalRate");
      float price = weaponStats.get("price");
      weapons[i++] = new Weapon(attackDamage, criticalRate, ammos[0], price);
      // weapons[i++] = new Weapon(attackDamage, criticalRate, ammos[1], price);
      // weapons[i++] = new Weapon(attackDamage, criticalRate, ammos[2], price);
    }
    return weapons;
  }

  public static void main(String[] args) {
    CountryBonuses countryBonus = new CountryBonuses(0.15f, 0.15f);
    MilitaryUnitBonuses muBonus = new MilitaryUnitBonuses(0.15f, 0.2f);
    PoliticalBonuses polBonus = new PoliticalBonuses(false, true);
    RegionalBonuses regBonus = new RegionalBonuses(0f, false);
    Pill pill = new Pill(true);
    Buffs buffs = new Buffs(pill, 0.12f, countryBonus, muBonus, regBonus, polBonus);
    Food food = new Food(3, 7f);

    Instant start = Instant.now();
    Ammo[] ammos = { 
      new Ammo(0.1f, 0.17f), 
      // new Ammo(0.2f, 0.7f),
    //  new Ammo(0.4f, 2.94f),
    };
    Weapon[] weapons = generateWeapons(ammos);
    Helmet[] helmets = { new Helmet(9f, 1.8f), new Helmet(19f, 6.3f), new Helmet(29f, 20.8f),
        new Helmet(39f, 58.7f), new Helmet(57f, 187.4f), new Helmet(77f, 660) };
    Chest[] chests = { new Chest(5f, 1.8f), new Chest(10f, 6.3f), new Chest(15f, 20.8f),
        new Chest(20f, 58.7f), new Chest(29f, 187.4f), new Chest(39f, 660) };
    Gloves[] gloves = { new Gloves(5f, 1.8f), new Gloves(10f, 6.3f), new Gloves(15f, 20.8f),
        new Gloves(20f, 58.7f), new Gloves(29f, 187.4f), new Gloves(39f, 660) };
    Pants[] pants = { new Pants(5f, 1.8f), new Pants(10f, 6.3f), new Pants(15f, 20.8f),
        new Pants(20f, 58.7f), new Pants(29f, 187.4f), new Pants(39f, 660) };
    Boots[] boots = { new Boots(0.05f, 1.8f), new Boots(10f, 6.3f), new Boots(15f, 20.8f),
        new Boots(20f, 58.7f), new Boots(29f, 187.4f), new Boots(39f, 660) };
    // Define gear quality range: indices 1 to 3 inclusive
    int minIndex = 1;
    int maxIndex = 3;

    // Track best result
    float bestScore = 0;
    String bestConfig = "";
    int totalTests = 0;
    Skills skills = new Skills(88);
    Player player = new Player(skills, buffs, food);
    for (Weapon weapon : weapons) {
      if (!isBlueWeapon(weapon))
        continue;

      for (int h = minIndex; h <= maxIndex; h++) {
        for (int c = minIndex; c <= maxIndex; c++) {
          for (int g = minIndex; g <= maxIndex; g++) {
            for (int b = minIndex; b <= maxIndex; b++) {
              for (int p = minIndex; p <= maxIndex; p++) {
                Gear gear = new Gear();
                gear.setWeapon(weapon);
                gear.setHelmet(helmets[h]);
                gear.setChest(chests[c]);
                gear.setGloves(gloves[g]);
                gear.setBoots(boots[b]);
                gear.setPants(pants[p]);

                player.setGear(gear);
                Map<String, Integer> optimizedPoints = player.optimizeSkillPoints(); // assumed to return int or void?
                float[] efficiency = player.getDamageEfficiency(); // [damagePer8h, costPer1k]
                float score = player.getScore(player.getSkills().originalSkillPoints);
                float damage = efficiency[0];
                if (score > bestScore) {
                  bestScore = score;
                  bestConfig = String.format(
                      "Weapon: AD=%.1f CR=%.2f Ammo(Q=%.1f,P=%.2f) | " +
                          "H%d C%d G%d P%d B%d | Damage: %.2f | Cost/1k: %.2f",
                      weapon.getAttackDamage(), weapon.getCriticalRate(),
                      weapon.getAmmo().getBonus(), weapon.getAmmo().getPrice(),
                      h, c, g, p, b,
                      damage, efficiency[1]) + optimizedPoints.toString();
                }

                totalTests++;
                System.out.println(totalTests);
                player.resetSkills();
              }
            }
          }
        }
      }
    }
// Total combinations tested: 486
// Weapon: AD=87.0 CR=0.15 Ammo(Q=1.1,P=0.17) | H2 C2 G2 P2 B1 | Damage: 1
// 84073.75 | Cost/1k: -0.00{dodge=6, lootChance=2, production=0, precisio
// n=2, health=2, hunger=3, criticalRate=2, companies=6, armor=4, critical
// Damage=2, entre=0, attackDamage=5, energy=0}
    Instant end = Instant.now();
    long durationMs = Duration.between(start, end).toMillis();
    System.out.println("=== OPTIMIZATION RESULTS ===");
    System.out.println("Total combinations tested: " + totalTests);
    System.out.println("Best configuration:");
    System.out.println(bestConfig);
    System.out.println("\nTime taken: " + durationMs + " ms");
  }

  public static boolean isBlueWeapon(Weapon w) {
    return w.getAttackDamage() == 59f || w.getAttackDamage() == 87f;
  }
}
