package com.dtzi.app.Equipment;

public class Ammo  {
  private float bonus;
  private float price;
  public Ammo (float bonus, float price) {
    this.bonus = 1 + bonus;
    this.price = price;
  }

  public float getBonus() {
    return this.bonus;
  }
  public float getPrice() {
    return this.price;
  }
  public void setPrice(float price) {
    this.price = price;
  }
  public void setBonus(float bonus) {
    this.bonus = bonus;
  }
}
