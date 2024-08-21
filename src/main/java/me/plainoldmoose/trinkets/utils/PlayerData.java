package me.plainoldmoose.trinkets.utils;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class PlayerData {
    private final List<ItemStack> items;
    private final Map<String, Integer> originalStats;

    public PlayerData(List<ItemStack> items, Map<String, Integer> originalStats) {
        this.items = items;
        this.originalStats = originalStats;
    }

    public PlayerData(List<ItemStack> items) {
        this.items = items;
        this.originalStats = null;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public Map<String, Integer> getOriginalStats() {
        return originalStats;
    }
}
