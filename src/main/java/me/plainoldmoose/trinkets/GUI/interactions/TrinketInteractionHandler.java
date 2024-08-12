package me.plainoldmoose.trinkets.GUI.interactions;

import com.willfp.eco.core.data.PlayerProfile;
import com.willfp.eco.core.data.keys.PersistentDataKey;
import com.willfp.eco.core.data.keys.PersistentDataKeyType;
import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.handlers.Keys;
import me.plainoldmoose.trinkets.GUI.components.Button;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Map;

public class TrinketInteractionHandler {
    private final TrinketManager trinketManager = Trinkets.getInstance().getManager();

    public void handleButtonClick(Player player, int slot, Button button) {
        ItemStack buttonItem = button != null ? button.getContainedItem() : null;
        ItemStack itemOnCursor = player.getItemOnCursor();
        PlayerProfile profile = PlayerProfile.load(player.getUniqueId());

        if (itemOnCursor.getType() == Material.AIR) {
            handleEmptyCursorClick(button, buttonItem, profile, player);
        } else {
            handleCursorItemClick(itemOnCursor, button, profile, player);
        }
    }

    private void handleEmptyCursorClick(Button button, ItemStack buttonItem, PlayerProfile profile, Player player) {
        if (buttonItem == null) return;

        Trinket trinket = getTrinketFromItem(buttonItem);
        if (trinket != null) {
            updatePlayerStats(profile, trinket.getStats(), false);
            player.setItemOnCursor(button.pop());
        }
    }

    private void handleCursorItemClick(ItemStack itemOnCursor, Button button, PlayerProfile profile, Player player) {
        Trinket trinket = getTrinketFromItem(itemOnCursor);
        if (trinket == null || !itemHasTrinketKey(itemOnCursor)) return;

        updatePlayerStats(profile, trinket.getStats(), true);
        if (itemOnCursor.getType() != Material.AIR) {
            ItemStack itemToReturn = button.pop();
            button.push(itemOnCursor);
            player.setItemOnCursor(itemToReturn);
        }
    }

    private Trinket getTrinketFromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta itemMeta = item.getItemMeta();
        String displayName = itemMeta.getDisplayName();
        return trinketManager.getTrinketByDisplayName(displayName);
    }

    private boolean itemHasTrinketKey(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer itemContainer = itemMeta.getPersistentDataContainer();
        return itemContainer.has(Keys.TRINKET);
    }

    /**
     * Updates the player's stats based on the provided stats map.
     *
     * @param profile the player's profile
     * @param stats the stats to be applied
     * @param addStats true to add the stats, false to remove the stats
     */
    private void updatePlayerStats(PlayerProfile profile, Map<String, Integer> stats, boolean addStats) {
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            String stat = entry.getKey();
            int value = entry.getValue();

            // Adjust value based on add or remove operation
            int adjustment = addStats ? value : -value;

            NamespacedKey statKey = new NamespacedKey("ecoskills", stat);
            PersistentDataKey<Integer> intKey = new PersistentDataKey<>(statKey, PersistentDataKeyType.INT, 0);

            Integer currentStatValue = profile.read(intKey);

            int updatedStatValue = currentStatValue + adjustment;
            profile.write(intKey, updatedStatValue);
        }
    }
}
