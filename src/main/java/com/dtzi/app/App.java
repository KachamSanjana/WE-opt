package com.dtzi.app;

import com.dtzi.app.Equipment.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.standard.PrinterLocation;

import com.dtzi.app.Buffs.*;
import com.dtzi.app.EquipmentGenerator.EquipmentData;;

/**
 * Hello world!
 */
public class App {
  public static void main(String[] args) {
    try {
      CountryBonuses countryBonus = new CountryBonuses(0.15f, 0.15f);
      MilitaryUnitBonuses muBonus = new MilitaryUnitBonuses(0.15f, 0.2f);
      PoliticalBonuses polBonus = new PoliticalBonuses(false, true);
      RegionalBonuses regBonus = new RegionalBonuses(0f, false);
      Pill pill = new Pill(true);
      Buffs buffs = new Buffs(pill, 0.13f, countryBonus, muBonus, regBonus, polBonus);
      Food food = new Food(3, 7f);

      Instant start = Instant.now();
      Ammo[] ammos = {
          new Ammo(0.1f, 0.17f),
          new Ammo(0.2f, 0.7f),
          new Ammo(0.4f, 2.94f),
      };
      EquipmentData equipment = EquipmentGenerator.generateFromJson("equipment_prices_20260110.json", ammos);
      // Define gear quality range: indices 1 to 3 inclusive
      int minArmorIndex = 1;
      int maxArmorIndex = 1;

      Helmet[] testHelmets = equipment.getHelmets(minArmorIndex, maxArmorIndex);
      Chest[] testChests = equipment.getChests(minArmorIndex, maxArmorIndex);
      Gloves[] testGloves = equipment.getGloves(minArmorIndex, maxArmorIndex);
      Pants[] testPants = equipment.getPants(minArmorIndex, maxArmorIndex);
      Boots[] testBoots = equipment.getBoots(minArmorIndex, maxArmorIndex);
      Weapon[] testWeapons = equipment.getWeapons(0, 6);

      // Track best result
      float bestScore = 0;
      String bestConfig = "";
      int totalTests = 0;
      Skills skills = new Skills(20*4);
      Player player = new Player(skills, buffs, food);
      for (int w = 0; w < testWeapons.length; w++) {
        if (!isBlueWeapon(testWeapons[w]))
          continue;
        for (int h = 0; h < testHelmets.length; h++) {
          for (int c = 0; c < testChests.length; c++) {
            for (int g = 0; g < testGloves.length; g++) {
              for (int p = 0; p < testPants.length; p++) {
                for (int b = 0; b < testBoots.length; b++) {
                  Gear gear = new Gear();
                  gear.setWeapon(testWeapons[w]);
                  gear.setHelmet(testHelmets[h]);
                  gear.setChest(testChests[c]);
                  gear.setGloves(testGloves[g]);
                  gear.setBoots(testBoots[b]);
                  gear.setPants(testPants[p]);

                  player.setGear(gear);
                  Map<String, Integer> optimizedPoints = player.optimizeSkillPoints(); // assumed to return int or void?
                  float[] efficiency = player.getDamageEfficiency(); // [damagePer8h, costPer1k]
                  float score = player.getScore(player.getSkills().originalSkillPoints);
                  float damage = efficiency[0];
                  System.out.println(score);
                  if (score > bestScore) {
                    bestScore = score;
                    Weapon weapon = testWeapons[w];
                    bestConfig = String.format(
                        "Weapon: AD=%.1f CR=%.2f Ammo(Q=%.1f,P=%.2f) | " +
                            "H%d C%d G%d P%d B%d | Damage: %.2f | Cost/1k: %.2f",
                        weapon.getAttackDamage(), weapon.getCriticalRate(),
                        weapon.getAmmo().getBonus(), weapon.getAmmo().getPrice(),
                        h+minArmorIndex+1, c+minArmorIndex+1, g+minArmorIndex+1, p+minArmorIndex+1, b+minArmorIndex+1,
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
// LVL 21
// === OPTIMIZATION RESULTS ===
// Total combinations tested: 32
// Best configuration:
// Weapon: AD=87.0 CR=0.15 Ammo(Q=1.1,P=0.17) | H2 C3 G2 P2 B2 | Damage: 157836.63 | Cost/1k: -0.01{dodge=
// 5, lootChance=1, production=1, precision=2, health=2, hunger=3, criticalRate=3, companies=6, armor=4, c
// riticalDamage=2, entre=0, attackDamage=5, energy=0}
// LVL 22
// === OPTIMIZATION RESULTS ===
// Total combinations tested: 32
// Best configuration:
// Weapon: AD=87.0 CR=0.15 Ammo(Q=1.1,P=0.17) | H1 C2 G1 P2 B1 | Damage:
//  186544.69 | Cost/1k: -0.01{dodge=6, lootChance=1, production=0, prec
// ision=2, health=3, hunger=3, criticalRate=3, companies=5, armor=3, cr
// iticalDamage=2, entre=0, attackDamage=6, energy=0}
      Instant end = Instant.now();
      long durationMs = Duration.between(start, end).toMillis();
      System.out.println("=== OPTIMIZATION RESULTS ===");
      System.out.println("Total combinations tested: " + totalTests);
      System.out.println("Best configuration:");
      System.out.println(bestConfig);
      System.out.println("\nTime taken: " + durationMs + " ms");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean isBlueWeapon(Weapon w) {
    return w.getAttackDamage() == 87f;
  }
}
