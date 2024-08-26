package me.plainoldmoose.trinkets.gui.interactions;

import com.willfp.ecoskills.api.EcoSkillsAPI;
import com.willfp.ecoskills.api.modifiers.ModifierOperation;
import com.willfp.ecoskills.api.modifiers.StatModifier;
import com.willfp.ecoskills.stats.Stat;
import com.willfp.ecoskills.stats.Stats;
import me.plainoldmoose.trinkets.data.trinket.Trinket;
import me.plainoldmoose.trinkets.data.trinket.TrinketManager;
import me.plainoldmoose.trinkets.gui.components.TrinketSlot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Handles interactions with TrinketSlot buttons in the GUI.
 * Manages the logic for equipping or removing trinkets from a TrinketSlot.
 */
public class TrinketInteractionHandler {

    /**
     * Handles the button click event for a TrinketSlot.
     * Determines whether the cursor is empty or contains an item and processes the click accordingly.
     *
     * @param player      The player who clicked the button.
     * @param trinketSlot The TrinketSlot that was clicked.
     */
    public static void handleButtonClick(Player player, TrinketSlot trinketSlot) {
        ItemStack itemOnCursor = player.getItemOnCursor();

        if (itemOnCursor.getType() == Material.AIR) {
            handleEmptyCursorClick(player, trinketSlot);
        } else {
            handleCursorItemClick(itemOnCursor, player, trinketSlot);
        }
    }

    /**
     * Handles the click event when the player's cursor is empty.
     * Moves the trinket from the TrinketSlot to the player's cursor and updates the player's stats.
     *
     * @param player      The player who clicked the button.
     * @param trinketSlot The TrinketSlot that was clicked.
     */
    private static void handleEmptyCursorClick(Player player, TrinketSlot trinketSlot) {
        ItemStack buttonItem = trinketSlot.getContainedTrinket();

        if (buttonItem == null) {
            return;
        }

        Trinket trinket = TrinketManager.getInstance().getTrinket(buttonItem);
        if (trinket != null) {
            updatePlayerStats(player, trinket.getStats(), false);
            player.setItemOnCursor(trinketSlot.pop());
        }
    }

    /**
     * Handles the click event when the player's cursor contains an item.
     * Moves the item from the cursor to the TrinketSlot and updates the player's stats.
     *
     * @param itemOnCursor The item currently on the player's cursor.
     * @param player       The player who clicked the button.
     * @param trinketSlot  The TrinketSlot that was clicked.
     */
    private static void handleCursorItemClick(ItemStack itemOnCursor, Player player, TrinketSlot trinketSlot) {
        Trinket trinket = TrinketManager.getInstance().getTrinket(itemOnCursor);
        if (trinket == null) {
            return;
        }

        if (!trinket.getType().equals(trinketSlot.getType())) {
            return;
        }

        updatePlayerStats(player, trinket.getStats(), true);

        player.setItemOnCursor(trinketSlot.pop());
        trinketSlot.push(itemOnCursor);
    }

    /**
     * Updates the player's stats based on the provided stats map.
     * Adds or removes stats based on the value of {@code addStats}.
     *
     * @param player   The player whose stats are to be updated.
     * @param stats    The stats to be applied or removed.
     * @param addStats True to add the stats, false to remove the stats.
     */
    public static void updatePlayerStats(Player player, Map<String, Integer> stats, boolean addStats) {
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            String statName = entry.getKey();
            int statValue = entry.getValue();

            if (addStats) {
                addStatModifier(player, statName, statValue);
            } else {
                removeStatModifier(player, statName, statValue);
            }
        }
    }

    /**
     * Adds a stat modifier to the player's stats.
     *
     * @param player    The player to whom the stat modifier is to be added.
     * @param statName  The name of the stat to be modified.
     * @param statValue The value of the stat to be added.
     */
    private static void addStatModifier(Player player, String statName, int statValue) {
        Stat stat = Stats.INSTANCE.get(statName);

        if (stat == null) {
            return;
        }

        StatModifier modifier = new StatModifier(UUID.randomUUID(), stat, statValue, ModifierOperation.ADD);
        EcoSkillsAPI.addStatModifier(player, modifier);
    }

    /**
     * Removes a stat modifier from the player's stats.
     *
     * @param player    The player from whom the stat modifier is to be removed.
     * @param statName  The name of the stat to be removed.
     * @param statValue The value of the stat to be removed.
     */
    private static void removeStatModifier(Player player, String statName, int statValue) {
        List<StatModifier> modifiers = EcoSkillsAPI.getStatModifiers(player);

        for (StatModifier mod : modifiers) {
            if (mod.getStat().getName().toLowerCase().contains(statName) && (int) mod.getModifier() == statValue) {
                EcoSkillsAPI.removeStatModifier(player, mod.getUuid());
                break;
            }
        }
    }
}
