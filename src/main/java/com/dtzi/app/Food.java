package com.dtzi.app;

public class Food {
  int hpRestore;
  float price;
  public Food(int hpRestore, float price) {
    this.hpRestore = hpRestore;
    this.price = price;
  }
  public int getHpRestore() {
    return this.hpRestore;
  }
  public float getPrice() {
    return this.price;
  }
  public void setHpRestore(int hpRestore) {
    this.hpRestore = hpRestore;
  }
  public void setPrice(float price) {
    this.price = price;
  }
}
