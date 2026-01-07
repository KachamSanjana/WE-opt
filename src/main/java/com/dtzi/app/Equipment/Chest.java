package com.dtzi.app.Equipment;

public class Chest extends AbstractBodyArmor {
  private float stat;
  final private String STAT_TYPE = "armor";
  public Chest(float stat, float price) {
    this.stat = stat / 100;
    this.price = price;
  }
  public float getStat() {
    return this.stat;
  }
  public String getStatType() {
    return this.STAT_TYPE;
  }
  private float price;
  public float getPrice() {
    return this.price;
  }
}
