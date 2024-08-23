package me.plainoldmoose.trinkets.gui;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.data.loaders.PlayerDataLoader;
import me.plainoldmoose.trinkets.data.trinket.SerializedTrinketSlot;
import me.plainoldmoose.trinkets.gui.builders.BackgroundBuilder;
import me.plainoldmoose.trinkets.gui.builders.IconBuilder;
import me.plainoldmoose.trinkets.gui.builders.PlayerPrefixBuilder;
import me.plainoldmoose.trinkets.gui.builders.TrinketSlotBuilder;
import me.plainoldmoose.trinkets.gui.components.Background;
import me.plainoldmoose.trinkets.gui.components.StatsIcon;
import me.plainoldmoose.trinkets.gui.components.TrinketSlot;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TrinketsGUI {
    private final int size = 54; // Inventory size
    // TODO - setup config for this
    private final String title = "               Trinkets"; // GUI title
    private Inventory inventory;

    /**
     * Displays the Trinkets GUI to the specified player. Sets up the inventory, background, buttons, and updates the GUI.
     * If the player already has a TrinketsGUI metadata, their current inventory is closed.
     *
     * @param player The player to whom the GUI will be displayed.
     */
    public void displayTo(Player player) {
        inventory = Bukkit.createInventory(player, this.size, this.title);
        BackgroundBuilder.createBackgroundTiles();
        TrinketSlotBuilder.createSlotButtons();
        loadTrinketSaveData(player);

        updateGUI(player);

        if (player.hasMetadata("TrinketsGUI")) {
            player.closeInventory();
        }

        player.setMetadata("TrinketsGUI", new FixedMetadataValue(Trinkets.getInstance(), this));
        player.openInventory(inventory);
    }

    private void renderBackgrounds() {
        for (Background background : BackgroundBuilder.getBackgroundList()) {
            inventory.setItem(background.getIndex(), background.getDisplayItem());
        }
    }

    // TODO - Extract this to builder
    private void renderStatIcons(Player player) {
        IconBuilder.createStatsIcons(player);

        for (StatsIcon icon : IconBuilder.getIconList()) {
            ItemStack iconItem = icon.getDisplayItem();
            List<String> stats = icon.getStatsList();

            String iconItemName = iconItem.getItemMeta().getDisplayName();
            // Replace placeholder with player name
            if (iconItemName.contains("%playername%")) {
                iconItemName = iconItemName.replace("%playername%", player.getName());

                // If adding player name, also add prefix
                if (PlayerPrefixBuilder.getInstance().getChat() != null) {
                    iconItemName = ConfigUtils.colorizeString(PlayerPrefixBuilder.getPlayerPrefix(player)) + iconItemName;
                }
            }

            // If icon is player head, replace with players skin
            if (iconItem.getType() == Material.PLAYER_HEAD) {
                icon.setDisplayItem(ItemFactory.createPlayerHead(player.getUniqueId()));
            }

            // Update item stack lore to include desired stats
            iconItem = icon.getDisplayItem();
            ItemFactory.changeItemStackLore(iconItem, stats);
            icon.setDisplayItem(iconItem);

            ItemFactory.changeItemStackName(icon.getDisplayItem(), iconItemName);
            inventory.setItem(icon.getIndex(), icon.getDisplayItem());
        }
    }


    private void renderButtons() {
        for (Map.Entry<Integer, TrinketSlot> entry : TrinketSlotBuilder.getTrinketSlotMap().entrySet()) {
            TrinketSlot trinketSlot = entry.getValue();
            if (trinketSlot.isEnabled()) {
                inventory.setItem(trinketSlot.getIndex(), trinketSlot.getContainedTrinket() != null ? trinketSlot.getContainedTrinket() : trinketSlot.getDisplayItem());
                if (trinketSlot.getContainedTrinket() != null) {
                    System.out.println(trinketSlot.getContainedTrinket().getItemMeta().getDisplayName());
                }
            }
        }
    }

    public void updateGUI(Player player) {
        renderBackgrounds();
        renderStatIcons(player);
        renderButtons();
    }

    private void loadTrinketSaveData(Player player) {
        // Retrieve serialized slots for the player
        Map<UUID, List<SerializedTrinketSlot>> serialisedSlots = PlayerDataLoader.getInstance().getSerialisedSlots();
        List<SerializedTrinketSlot> serializedTrinketSlotList = serialisedSlots.get(player.getUniqueId());

        if (serializedTrinketSlotList == null) {
            return;
        }

        // Iterate over the serialized slots and deserialize them, then add to the map
        for (SerializedTrinketSlot s : serializedTrinketSlotList) {
            // Reconstruct TrinketSlot and add to map
            TrinketSlot trinketSlot = TrinketSlot.deserialize(s.getMap());
            TrinketSlotBuilder.getTrinketSlotMap().put(trinketSlot.getIndex(), trinketSlot);
            player.sendMessage("Loaded slot >>>>> " +trinketSlot.getContainedTrinket().getItemMeta().getDisplayName());
        }
    }
}
