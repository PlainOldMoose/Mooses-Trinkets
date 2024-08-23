package me.plainoldmoose.trinkets.gui.interactions;

import com.willfp.ecoskills.api.EcoSkillsAPI;
import com.willfp.ecoskills.api.modifiers.ModifierOperation;
import com.willfp.ecoskills.api.modifiers.StatModifier;
import com.willfp.ecoskills.stats.Stats;
import me.plainoldmoose.trinkets.data.trinket.Trinket;
import me.plainoldmoose.trinkets.data.trinket.TrinketManager;
import me.plainoldmoose.trinkets.gui.components.TrinketSlot;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class TrinketInteractionHandler {
    public static void handleButtonClick(Player player, TrinketSlot trinketSlot) {
        ItemStack itemOnCursor = player.getItemOnCursor();

        if (itemOnCursor.getType() == Material.AIR) {
            handleEmptyCursorClick(player, trinketSlot);
        } else {
            handleCursorItemClick(itemOnCursor, player, trinketSlot);
        }
    }

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
     *
     * @param stats    the stats to be applied
     * @param addStats true to add the stats, false to remove the stats
     */
    public static void updatePlayerStats(Player player, Map<String, Integer> stats, boolean addStats) {
        // TODO - Fix this stupid fucking interaction (I hate auxilor)
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            String statName = entry.getKey();
            int statValue = entry.getValue();
            player.sendMessage("Stat: " + statName + " Value: " + statValue);

            double ecoValue = EcoSkillsAPI.getStatLevel(player, Stats.INSTANCE.get(statName));
            double adjustment = addStats ? ecoValue + statValue : ecoValue - statValue;

            player.sendMessage("Setting " + statName + " to " + statValue);
            StatModifier modifier = new StatModifier(player.getUniqueId(), Stats.INSTANCE.get(statName), statValue, ModifierOperation.ADD);
            EcoSkillsAPI.addStatModifier(player, modifier);
        }

        for (StatModifier sm : EcoSkillsAPI.getStatModifiers(player)) {
            player.sendMessage("Found > " + sm.getStat().getName());
        }
    }
}
