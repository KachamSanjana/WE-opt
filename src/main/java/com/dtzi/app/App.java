package com.dtzi.app;

import com.dtzi.app.Equipment.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
      Gear gear = new Gear();
      gear.setWeapon(new Weapon(87, 15));
      gear.setHelmet(new Helmet(20));
      gear.setChest(new Chest(10));
      gear.setGloves(new Gloves(10));
      gear.setPants(new Pants(10));
      gear.setBoots(new Boots(10));
      System.out.println(gear.getStats().toString());
      System.out.println(gear.weapon.getAttackDamage());
    }
}
