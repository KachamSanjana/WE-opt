package com.dtzi.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import com.dtzi.app.Equipment.*;

public class EquipmentGenerator {

    public static class EquipmentData {
        public Helmet[] helmets;
        public Chest[] chests;
        public Gloves[] gloves;
        public Pants[] pants;
        public Boots[] boots;
        public Weapon[] weapons;
        
        // Get subset of equipment based on min/max indices
        public Helmet[] getHelmets(int minIndex, int maxIndex) {
            return getSubArray(helmets, minIndex, maxIndex);
        }
        
        public Chest[] getChests(int minIndex, int maxIndex) {
            return getSubArray(chests, minIndex, maxIndex);
        }
        
        public Gloves[] getGloves(int minIndex, int maxIndex) {
            return getSubArray(gloves, minIndex, maxIndex);
        }
        
        public Pants[] getPants(int minIndex, int maxIndex) {
            return getSubArray(pants, minIndex, maxIndex);
        }
        
        public Boots[] getBoots(int minIndex, int maxIndex) {
            return getSubArray(boots, minIndex, maxIndex);
        }
        
        public Weapon[] getWeapons(int minIndex, int maxIndex) {
            return getSubArray(weapons, minIndex, maxIndex);
        }
        
        @SuppressWarnings("unchecked")
        private <T> T[] getSubArray(T[] array, int minIndex, int maxIndex) {
            int startIdx = Math.max(0, minIndex);
            int endIdx = Math.min(array.length - 1, maxIndex);
            
            if (startIdx > endIdx || startIdx >= array.length) {
                return (T[]) java.lang.reflect.Array.newInstance(
                    array.getClass().getComponentType(), 0);
            }
            
            int length = endIdx - startIdx + 1;
            T[] result = (T[]) java.lang.reflect.Array.newInstance(
                array.getClass().getComponentType(), length);
            System.arraycopy(array, startIdx, result, 0, length);
            return result;
        }
    }
    
    // Helper class to track equipment combination with indices
    public static class EquipmentCombination {
        public int helmetIndex;
        public int chestIndex;
        public int glovesIndex;
        public int pantsIndex;
        public int bootsIndex;
        public int weaponIndex;
        public float totalCost;
        public float totalStats;
        
        public EquipmentCombination(int helmet, int chest, int gloves, 
                                   int pants, int boots, int weapon,
                                   float cost, float stats) {
            this.helmetIndex = helmet;
            this.chestIndex = chest;
            this.glovesIndex = gloves;
            this.pantsIndex = pants;
            this.bootsIndex = boots;
            this.weaponIndex = weapon;
            this.totalCost = cost;
            this.totalStats = stats;
        }
        
        @Override
        public String toString() {
            return String.format("Helmet:%d,Chest:%d,Gloves:%d,Pants:%d,Boots:%d,Weapon:%d",
                helmetIndex, chestIndex, glovesIndex, pantsIndex, bootsIndex, weaponIndex);
        }
        
        public String toDetailedString() {
            return String.format("Combination[H:%d C:%d G:%d P:%d B:%d W:%d] Cost:%.2f Stats:%.2f",
                helmetIndex, chestIndex, glovesIndex, pantsIndex, bootsIndex, weaponIndex,
                totalCost, totalStats);
        }
    }

    public static EquipmentData generateFromJson(String jsonFilePath, Ammo[] ammos) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jsonFilePath));
        JsonNode medianPrices = root.get("median_prices");
        
        EquipmentData data = new EquipmentData();
        
        // Generate Helmets
        Helmet[] helmets = new Helmet[6];
        helmets[0] = createHelmet(medianPrices, "helmet1", 9f);
        helmets[1] = createHelmet(medianPrices, "helmet2", 19f);
        helmets[2] = createHelmet(medianPrices, "helmet3", 29f);
        helmets[3] = createHelmet(medianPrices, "helmet4", 39f);
        helmets[4] = createHelmet(medianPrices, "helmet5", 57f);
        helmets[5] = createHelmet(medianPrices, "helmet6", 77f);
        data.helmets = helmets;
        
        // Generate Chests
        Chest[] chests = new Chest[6];
        chests[0] = createChest(medianPrices, "chest1", 5f);
        chests[1] = createChest(medianPrices, "chest2", 10f);
        chests[2] = createChest(medianPrices, "chest3", 15f);
        chests[3] = createChest(medianPrices, "chest4", 20f);
        chests[4] = createChest(medianPrices, "chest5", 29f);
        chests[5] = createChest(medianPrices, "chest6", 39f);
        data.chests = chests;
        
        // Generate Gloves
        Gloves[] gloves = new Gloves[6];
        gloves[0] = createGloves(medianPrices, "gloves1", 5f);
        gloves[1] = createGloves(medianPrices, "gloves2", 10f);
        gloves[2] = createGloves(medianPrices, "gloves3", 15f);
        gloves[3] = createGloves(medianPrices, "gloves4", 20f);
        gloves[4] = createGloves(medianPrices, "gloves5", 29f);
        gloves[5] = createGloves(medianPrices, "gloves6", 39f);
        data.gloves = gloves;
        
        // Generate Pants
        Pants[] pants = new Pants[6];
        pants[0] = createPants(medianPrices, "pants1", 5f);
        pants[1] = createPants(medianPrices, "pants2", 10f);
        pants[2] = createPants(medianPrices, "pants3", 15f);
        pants[3] = createPants(medianPrices, "pants4", 20f);
        pants[4] = createPants(medianPrices, "pants5", 29f);
        pants[5] = createPants(medianPrices, "pants6", 39f);
        data.pants = pants;
        
        // Generate Boots
        Boots[] boots = new Boots[6];
        boots[0] = createBoots(medianPrices, "boots1", 5f);
        boots[1] = createBoots(medianPrices, "boots2", 10f);
        boots[2] = createBoots(medianPrices, "boots3", 15f);
        boots[3] = createBoots(medianPrices, "boots4", 20f);
        boots[4] = createBoots(medianPrices, "boots5", 29f);
        boots[5] = createBoots(medianPrices, "boots6", 39f);
        data.boots = boots;
        
        // Generate Weapons
        Weapon[] weapons = new Weapon[6];
        weapons[0] = createWeapon(medianPrices, "knife", 37f, 0.05f, ammos[0]);
        weapons[1] = createWeapon(medianPrices, "gun", 59f, 0.1f, ammos[0]);
        weapons[2] = createWeapon(medianPrices, "rifle", 87f, 0.15f, ammos[0]);
        weapons[3] = createWeapon(medianPrices, "sniper", 117f, 0.20f, ammos[0]);
        weapons[4] = createWeapon(medianPrices, "tank", 155f, 0.29f, ammos[0]);
        weapons[5] = createWeapon(medianPrices, "jet", 265f, 0.39f, ammos[0]);
        data.weapons = weapons;
        
        return data;
    }

    private static Helmet createHelmet(JsonNode medianPrices, String itemCode, float stat) {
        float price = getMedianPrice(medianPrices, itemCode, 1.8f);
        return new Helmet(stat, price);
    }

    private static Chest createChest(JsonNode medianPrices, String itemCode, float stat) {
        float price = getMedianPrice(medianPrices, itemCode, 1.8f);
        return new Chest(stat, price);
    }

    private static Gloves createGloves(JsonNode medianPrices, String itemCode, float stat) {
        float price = getMedianPrice(medianPrices, itemCode, 1.8f);
        return new Gloves(stat, price);
    }

    private static Pants createPants(JsonNode medianPrices, String itemCode, float stat) {
        float price = getMedianPrice(medianPrices, itemCode, 1.8f);
        return new Pants(stat, price);
    }

    private static Boots createBoots(JsonNode medianPrices, String itemCode, float stat) {
        float price = getMedianPrice(medianPrices, itemCode, 1.8f);
        return new Boots(stat, price);
    }

    private static Weapon createWeapon(JsonNode medianPrices, String itemCode, 
                                       float attack, float criticalChance, Ammo ammo) {
        float price = getMedianPrice(medianPrices, itemCode, 2f);
        return new Weapon(attack, criticalChance, ammo, price);
    }

    private static float getMedianPrice(JsonNode medianPrices, String itemCode, float defaultPrice) {
        if (medianPrices != null && medianPrices.has(itemCode)) {
            JsonNode itemNode = medianPrices.get(itemCode);
            if (itemNode.has("median_price")) {
                return (float) itemNode.get("median_price").asDouble();
            }
        }
        return defaultPrice;
    }
}
