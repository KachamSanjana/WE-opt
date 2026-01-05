package com.dtzi.app.Equipment;
 
public class Boots extends AbstractBodyArmor {
  private float stat;
  final private String STAT_TYPE = "dodge";
  public Boots(float stat, float price) {
    this.stat = stat / 100;
    this.price = price;
  }
  public float getStat() {
    return this.stat;
  }
  public String getSTAT_TYPE() {
    return this.STAT_TYPE;
  }
  private float price;
  public float getPrice() {
    return this.price;
  }
}
