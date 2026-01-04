package com.dtzi.app.Equipment;

public class Weapon implements Equipment {
  private int attackDamage;
  private int criticalRate;
  public Weapon(int attackDamage, int criticalRate) {
    this.attackDamage = attackDamage;
    this.criticalRate = criticalRate;
  }
  public int getAttackDamage() {
    return this.attackDamage;
  }
  public int getCriticalRate() {
    return this.criticalRate;
  }
}
