package com.dtzi.app.Buffs;

public record Pill (boolean isPill) {
  public float price() {
    return (isPill ? 26f : 0f);
  }
  public float bonus() {
    return (isPill ? 0.6f : 0f);
    }
}
