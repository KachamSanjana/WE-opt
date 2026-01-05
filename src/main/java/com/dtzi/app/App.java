package com.dtzi.app;

import com.dtzi.app.Equipment.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
      Gear gear = new Gear();
      gear.setWeapon(new Weapon(0, 0, 5f));
      gear.setHelmet(new Helmet(0, 5f));
      gear.setChest(new Chest(0, 5f));
      gear.setGloves(new Gloves(0, 5f));
      gear.setPants(new Pants(0, 5f));
      gear.setBoots(new Boots(0, 5f));
      Player player = new Player(gear, 50, 0);
      System.out.println(player.optimizeSkillPoints());
    }
}
