package me.plainoldmoose.trinkets.GUI.components;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StatsIcon {
    private ItemStack item;
    private int slot;
    private List<String> rawStatNames = new ArrayList<>();

    public StatsIcon(ItemStack item, int slot, List<String> rawStatNames) {
        this.item = item;
        this.slot = slot;
        this.rawStatNames = rawStatNames;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public List<String> getStatsList() {
        return item.getItemMeta().getLore();
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setListOfStats(List<String> statsList) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(statsList);
        item.setItemMeta(meta);
    }

    public List<String> getRawStatNames() {
        return rawStatNames;
    }

    public void setRawStatNames(List<String> rawStatNames) {
        this.rawStatNames = rawStatNames;
    }
}
