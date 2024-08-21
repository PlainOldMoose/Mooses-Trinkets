package me.plainoldmoose.trinkets.GUI.interactions;

import com.willfp.ecoskills.api.EcoSkillsAPI;
import com.willfp.ecoskills.api.modifiers.ModifierOperation;
import com.willfp.ecoskills.api.modifiers.StatModifier;
import com.willfp.ecoskills.stats.Stats;
import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.handlers.Keys;
import me.plainoldmoose.trinkets.GUI.components.TrinketSlotButton;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Map;

public class TrinketInteractionHandler {
    private final TrinketManager trinketManager = Trinkets.getInstance().getManager();

    public void handleButtonClick(Player player, int slot, TrinketSlotButton trinketSlotButton) {
        ItemStack buttonItem = trinketSlotButton != null ? trinketSlotButton.getContainedItem() : null;
        ItemStack itemOnCursor = player.getItemOnCursor();

        if (itemOnCursor.getType() == Material.AIR) {
            handleEmptyCursorClick(trinketSlotButton, player);
        } else {
            handleCursorItemClick(itemOnCursor, trinketSlotButton, player);
        }
    }

    private void handleEmptyCursorClick(TrinketSlotButton trinketSlotButton, Player player) {
        ItemStack buttonItem = trinketSlotButton.getContainedItem();

        Trinket trinket = trinketManager.getTrinket(buttonItem);
        if (trinket != null) {
            updatePlayerStats(player, trinket.getStats(), false);
            player.setItemOnCursor(trinketSlotButton.pop());
        }
    }

    private void handleCursorItemClick(ItemStack itemOnCursor, TrinketSlotButton trinketSlotButton, Player player) {
        Trinket trinket = trinketManager.getTrinket(itemOnCursor);
        if (trinket == null || !itemHasTrinketKey(itemOnCursor)) {
            return;
        }

        if (!trinket.getType().equals(trinketSlotButton.getType())) {
            return;
        }

        updatePlayerStats(player, trinket.getStats(), true);
        if (itemOnCursor.getType() != Material.AIR) {
            ItemStack itemToReturn = trinketSlotButton.pop();
            trinketSlotButton.push(itemOnCursor);
            player.setItemOnCursor(itemToReturn);
        }
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
     * @param stats the stats to be applied
     * @param addStats true to add the stats, false to remove the stats
     */
    public static void updatePlayerStats(Player player, Map<String, Integer> stats, boolean addStats) {
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            String statName = entry.getKey();
            int statValue = entry.getValue();
            System.out.println(">>> UPDATED " + statName + " to " + statName);

            int adjustment = addStats ? statValue : -statValue;
            StatModifier modifier = new StatModifier(player.getUniqueId(), Stats.INSTANCE.get(statName), adjustment, ModifierOperation.ADD);
            EcoSkillsAPI.addStatModifier(player, modifier);
        }
    }
}
