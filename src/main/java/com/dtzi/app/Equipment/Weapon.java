package com.dtzi.app.Equipment;

public class Weapon {
  private float attackDamage;
  private float criticalRate;
  private Ammo ammo;
  public Weapon(float attackDamage, float criticalRate, Ammo ammo, float price) {
    this.attackDamage = attackDamage;
    this.criticalRate = criticalRate;
    this.ammo = ammo;
    this.price = price;
  }
  public float getAttackDamage() {
    return this.attackDamage;
  }
  public float getCriticalRate() {
    return this.criticalRate;
  }
  public Ammo getAmmo() {
    return this.ammo;
  }
  private float price;
  public float getPrice() {
    return this.price;
  }
}
