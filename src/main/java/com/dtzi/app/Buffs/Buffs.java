package com.dtzi.app.Buffs;

public record Buffs(Pill pill, float militaryRank, CountryBonuses countryBonus, MilitaryUnitBonuses muBonus,
    RegionalBonuses regionBonus, PoliticalBonuses politicalBonus) {
  public float getMultiplier() {
    float totalBuff;
    totalBuff = (1 + pill.bonus()) * (1 + militaryRank) * this.sumAdditiveBonuses();
    return totalBuff;
  }

  public float sumAdditiveBonuses() {
    return (1 + countryBonus.bonus() + muBonus.bonus() + regionBonus.bonus() + politicalBonus.bonus());
  }
}
