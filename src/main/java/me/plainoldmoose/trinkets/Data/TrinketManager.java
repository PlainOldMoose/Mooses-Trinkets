package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.Data.handlers.Keys;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of trinkets, allowing addition, removal, and retrieval by name or ID.
 */
public class TrinketManager {
    private final List<Trinket> trinketList = new ArrayList<>();

    public void addTrinket(Trinket trinket) {
        if (trinket == null) {
            return;
        }

        trinket.setItem(addTrinketFlag(trinket.getTrinketItem()));
        trinketList.add(trinket);
    }

    private ItemStack addTrinketFlag(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(Keys.TRINKET, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
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

    public Trinket getTrinket(ItemStack trinketItemStack) {
        for (Trinket t : trinketList) {
            if (t.getDisplayName().equals(trinketItemStack.getItemMeta().getDisplayName())) {
                return t;
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
        return trinketList;
    }
}
