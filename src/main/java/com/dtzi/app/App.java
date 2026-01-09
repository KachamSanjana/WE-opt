package com.dtzi.app;

import com.dtzi.app.Equipment.*;

import java.util.Map;
import java.time.Duration;
import java.time.Instant;

import com.dtzi.app.Buffs.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
      Instant start = Instant.now();
      Gear gear = new Gear();
      Ammo ammo = new Ammo(0.1f, 0.15f);
      // Ammo ammo = new Ammo(0, 0);
      gear.setWeapon(new Weapon(87, 0.15f, ammo, 25.4f));
      gear.setHelmet(new Helmet(19, 7.1f));
      gear.setChest(new Chest(10, 7.1f));
      gear.setGloves(new Gloves(10, 7.1f));
      gear.setPants(new Pants(10, 7.1f));
      gear.setBoots(new Boots(10, 7.5f));
      CountryBonuses countryBonus = new CountryBonuses(0.15f, 0.15f);
      MilitaryUnitBonuses muBonus = new MilitaryUnitBonuses(0.15f, 0.2f);
      PoliticalBonuses polBonus = new PoliticalBonuses(false, true);
      RegionalBonuses regBonus = new RegionalBonuses(0f, false);
      Pill pill = new Pill(true);
      Buffs buffs = new Buffs(pill, 0.12f, countryBonus, muBonus, regBonus, polBonus);
      Skills skills = new Skills(90);
      Food food = new Food(3, 6);
      // gear.setWeapon(new Weapon(0, 0, ammo, 0));
      // gear.setHelmet(new Helmet(0, 0));
      // gear.setChest(new Chest(0, 0));
      // gear.setGloves(new Gloves(0, 0));
      // gear.setPants(new Pants(0, 0));
      // gear.setBoots(new Boots(0, 0));
      // CountryBonuses countryBonus = new CountryBonuses(0, 0);
      // MilitaryUnitBonuses muBonus = new MilitaryUnitBonuses(0, 0);
      // PoliticalBonuses polBonus = new PoliticalBonuses(false, false);
      // RegionalBonuses regBonus = new RegionalBonuses(0f, false);
      // Pill pill = new Pill(false);
      // Buffs buffs = new Buffs(pill, 0, countryBonus, muBonus, regBonus, polBonus);
      // Skills skills = new Skills(0);
      Player player = new Player(gear, skills, buffs, food);
      System.out.println(player.optimizeSkillPoints2());
      System.out.println("Damage per 8 hours: " + player.getDamageEfficiency()[0]);
      Instant end = Instant.now();
      System.out.println(Duration.between(start, end).toMillis());
    }
}
