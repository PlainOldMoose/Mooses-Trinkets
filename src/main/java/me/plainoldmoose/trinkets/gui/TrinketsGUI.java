package me.plainoldmoose.trinkets.gui;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.data.loaders.PlayerDataLoader;
import me.plainoldmoose.trinkets.data.trinket.Trinket;
import me.plainoldmoose.trinkets.data.trinket.TrinketManager;
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

    private void renderStatIcons(Player player) {
        IconBuilder.createStatsIcons(player);

        for (StatsIcon icon : IconBuilder.getIconList()) {
            ItemStack iconItem = icon.getDisplayItem();
            List<String> stats = icon.getStatsList();

            // Update item stack lore to include desired stats
            ItemFactory.changeItemStackLore(iconItem, stats);
            icon.setDisplayItem(iconItem);

            String iconItemName = iconItem.getItemMeta().getDisplayName();
            player.sendMessage("Name before > " + iconItemName);

            // If icon is player head, replace with players skin
            if (iconItem.getType() == Material.PLAYER_HEAD) {
                icon.setDisplayItem(ItemFactory.createPlayerHead(player.getUniqueId()));
            }

            if (iconItemName.contains("%playername%")) {
                if (PlayerPrefixBuilder.getInstance().getChat() != null) {
                    iconItemName = ConfigUtils.colorizeString(PlayerPrefixBuilder.getInstance().getPlayerPrefix(player)) + iconItemName;
                }
            }

            ItemFactory.changeItemStackName(icon.getDisplayItem(), iconItemName);
            player.sendMessage("Name after > " + iconItem.getItemMeta().getDisplayName());
            inventory.setItem(icon.getIndex(), icon.getDisplayItem());
        }
    }


    private void renderButtons() {
        for (Map.Entry<Integer, TrinketSlot> entry : TrinketSlotBuilder.getTrinketSlotMap().entrySet()) {
            TrinketSlot trinketSlot = entry.getValue();
            if (trinketSlot.isEnabled()) {
                inventory.setItem(trinketSlot.getIndex(), trinketSlot.getContainedTrinket() != null ? trinketSlot.getContainedTrinket() : trinketSlot.getDisplayItem());
            }
        }
    }

    /**
     * Updates the GUI for the specified player. Sets the background items, button items, and player skull.
     *
     * @param player The player whose GUI will be updated.
     */
    public void updateGUI(Player player) {
        renderBackgrounds();
        renderStatIcons(player);
        renderButtons();
    }

    // TODO - re-implement this to fix duplication
    private void loadTrinketSaveData(Player player) {
        Map<UUID, List<ItemStack>> savedTrinkets = PlayerDataLoader.getInstance().getEquippedTrinkets();
        ;
        List<ItemStack> playerTrinkets = savedTrinkets.get(player.getUniqueId());

        if (playerTrinkets == null) {
            return;
        }

        for (TrinketSlot b : TrinketSlotBuilder.getTrinketSlotMap().values()) {
            for (ItemStack t : playerTrinkets) {
                Trinket trinket = TrinketManager.getInstance().getTrinket(t);
                if (b.getType().equals(trinket.getType())) {
                    b.push(t);
                }
            }
        }
    }
}
