package com.dtzi.app.Equipment;

public class Weapon {
  private float attackDamage;
  private float criticalRate;
  public Weapon(float attackDamage, float criticalRate, float price) {
    this.attackDamage = attackDamage;
    this.criticalRate = criticalRate;
  }
  public float getAttackDamage() {
    return this.attackDamage;
  }
  public float getCriticalRate() {
    return this.criticalRate;
  }
  private float price;
  public float getPrice() {
    return this.price;
  }
}
