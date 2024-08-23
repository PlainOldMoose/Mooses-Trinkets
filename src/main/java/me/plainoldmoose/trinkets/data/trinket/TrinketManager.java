package me.plainoldmoose.trinkets.data.trinket;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of trinkets, allowing for addition, removal, and retrieval of trinkets by name or item stack.
 * This class serves as a central point for handling trinket objects in the game.
 */
public class TrinketManager {
    private static final TrinketManager instance = new TrinketManager();

    private final List<Trinket> trinketList = new ArrayList<>();

    /**
     * Adds a trinket to the manager's list and marks it with a persistent data flag.
     *
     * @param trinket The trinket to add.
     */
    public void addTrinket(Trinket trinket) {
        if (trinket == null) {
            return;
        }

        trinket.setItem(addTrinketFlag(trinket.getTrinketItem()));
        trinketList.add(trinket);
    }

    /**
     * Marks an ItemStack with a persistent data flag indicating it is a trinket.
     *
     * @param item The ItemStack to mark.
     * @return The marked ItemStack with the trinket flag.
     */
    private ItemStack addTrinketFlag(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(Key.TRINKET, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Retrieves a trinket's ItemStack by its display name.
     *
     * @param trinketName The name of the trinket to find.
     * @return The ItemStack of the trinket if found, or null if not found.
     */
    public ItemStack getTrinketItemStack(String trinketName) {
        for (Trinket t : trinketList) {
            if (t.getName().equals(trinketName)) {
                return t.getTrinketItem();
            }
        }
        return null;
    }

    /**
     * Retrieves a trinket by its ItemStack representation.
     *
     * @param trinketItemStack The ItemStack of the trinket.
     * @return The Trinket associated with the ItemStack if found, or null if not found.
     */
    public Trinket getTrinket(ItemStack trinketItemStack) {
        for (Trinket t : trinketList) {
            if (t.getDisplayName().equals(trinketItemStack.getItemMeta().getDisplayName())) {
                return t;
            }
        }
        return null;
    }

    /**
     * Retrieves a list of all trinket names.
     *
     * @return A list of names of all trinkets.
     */
    public List<String> getTrinketNameList() {
        ArrayList<String> list = new ArrayList<>();

        for (Trinket t : trinketList) {
            list.add(t.getName());
        }

        return list;
    }

    /**
     * Retrieves the list of all trinkets managed by this manager.
     *
     * @return The list of all trinkets.
     */
    public List<Trinket> getTrinketList() {
        return trinketList;
    }

    /**
     * Gets the singleton instance of TrinketManager.
     *
     * @return The singleton instance of TrinketManager.
     */
    public static TrinketManager getInstance() {
        return instance;
    }
}
