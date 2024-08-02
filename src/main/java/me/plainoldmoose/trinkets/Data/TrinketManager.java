package me.plainoldmoose.trinkets.Data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TrinketManager {
    private List<Trinket> trinketList = new ArrayList<Trinket>();

    public TrinketManager() {
        Trinket test = new Trinket(0, new ItemStack(Material.DIAMOND), true);
        trinketList.add(test);
    }

    public void addTrinket(Trinket trinket) {
        this.trinketList.add(trinket);
    }

    public void removeTrinket(Trinket trinket) {
        this.trinketList.remove(trinket);
    }

    public ItemStack getTrinketByName(String trinketName) {
        for (Trinket t : trinketList) {
            if (t.getTrinketItem().getItemMeta().getDisplayName() == trinketName) {
                return t.getTrinketItem();
            }
        }
        return null;
    }

    public ItemStack getTrinketByID(int trinketID) {
        for (Trinket t : trinketList) {
            if (t.getTrinketID() == trinketID) {
                return t.getTrinketItem();
            }
        }
        return null;
    }

    public List<Trinket> getTrinketList() {
        return trinketList;
    }
}