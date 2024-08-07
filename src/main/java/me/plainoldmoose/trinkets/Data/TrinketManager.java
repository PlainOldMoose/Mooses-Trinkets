package me.plainoldmoose.trinkets.Data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of trinkets, allowing addition, removal, and retrieval by name or ID.
 */
public class TrinketManager {
    private final List<Trinket> trinketList = new ArrayList<>();

    /**
     * Constructs a new TrinketManager and adds a default test trinket.
     */
    public TrinketManager() {
        Trinket test = new Trinket(new ItemStack(Material.DIAMOND), "example1","This is an example", true);
        trinketList.add(test);
    }

    public void addTrinket(Trinket trinket) {
        trinketList.add(trinket);
    }

    public void removeTrinket(Trinket trinket) {
        trinketList.remove(trinket);
    }

    /**
     * Gets a trinket by its display name.
     *
     * @param trinketName The name of the trinket to find
     * @return The ItemStack of the trinket, or null if not found
     */
    public ItemStack getTrinketByName(String trinketName) {
        for (Trinket t : trinketList) {
            if (t.getName().equals(trinketName)) {
                return t.getTrinketItem();
            }
        }
        return null;
    }

    public List<String> getTrinketNameList() {
        ArrayList<String> list = new ArrayList<String>();

        for (Trinket t : trinketList) {
            list.add(t.getName());
        }

        return list;
    }

    public List<Trinket> getTrinketList() {
        return new ArrayList<>(trinketList);
    }
}
