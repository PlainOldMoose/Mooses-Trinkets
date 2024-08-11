package me.plainoldmoose.trinkets.GUI;

import me.plainoldmoose.trinkets.Data.ConfigHandler;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.GUI.components.Background;
import me.plainoldmoose.trinkets.GUI.components.Button;
import me.plainoldmoose.trinkets.GUI.components.TrinketSlot;
import me.plainoldmoose.trinkets.GUI.fetchers.ChatServiceFetcher;
import me.plainoldmoose.trinkets.GUI.fetchers.PlayerStatsFetcher;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrinketsGUI {
    private final int size = 54; // Inventory size

    // TODO - setup config for this
    private final String title = "               Trinkets"; // GUI title
    private final HashMap<Integer, Button> buttonMap = new HashMap<>();

    public List<Background> getBackgroundList() {
        return backgroundList;
    }

    public HashMap<Integer, Button> getButtonMap() {
        return buttonMap;
    }

    private final List<Background> backgroundList = new ArrayList<>();
    private Inventory inventory;
    private final ChatServiceFetcher chatSetup = new ChatServiceFetcher();
    private final PlayerStatsFetcher playerStatsFetcher = new PlayerStatsFetcher();
    private final ConfigHandler configHandler = TrinketsData.getInstance().getConfigHandler();



    /**
     * Displays the Trinkets GUI to the specified player. Sets up the inventory, background, buttons, and updates the GUI.
     * If the player already has a TrinketsGUI metadata, their current inventory is closed.
     *
     * @param player The player to whom the GUI will be displayed.
     */
    public void displayTo(Player player) {
        loadTrinketSlots();

        if (!chatSetup.setupChat()) {
            return; // Cannot proceed without chat setup failed
        }

        inventory = Bukkit.createInventory(player, this.size, this.title);
        createBackgroundTiles();
        createSlotButtons();
        updateGUI(player);

        if (player.hasMetadata("TrinketsGUI")) {
            player.closeInventory();
        }

        player.setMetadata("TrinketsGUI", new FixedMetadataValue(Trinkets.getInstance(), this));
        player.openInventory(inventory);
    }

    /**
     * Updates the GUI for the specified player. Sets the background items, button items, and player skull.
     *
     * @param player The player whose GUI will be updated.
     */
    public void updateGUI(Player player) {
        for (Background background : backgroundList) {
            inventory.setItem(background.getSlot(), background.getItem());
        }

        for (Map.Entry<Integer, Button> entry : buttonMap.entrySet()) {
            Button button = entry.getValue();
            if (button.isEnabled()) {
                inventory.setItem(button.getSlot(), button.getContainedItem() != null ? button.getContainedItem() : button.getItem());
            }
        }

        createPlayerSkullIcon(player);
    }

    /**
     * Creates background tiles for the GUI based on configuration settings.
     */
    private void createBackgroundTiles() {
        Material backgroundMaterial = configHandler.getBackgroundMaterial();
        Material secondaryMaterial = configHandler.getSecondaryBackgroundMaterial();

        for (int row = 0; row < 6; row++) {
            int firstColumnIndex = row * 9;
            int lastColumnIndex = firstColumnIndex + 8;

            addBackgroundItem(firstColumnIndex, secondaryMaterial);
            addBackgroundItem(lastColumnIndex, secondaryMaterial);

            for (int col = 1; col < 8; col++) {
                int index = firstColumnIndex + col;
                addBackgroundItem(index, backgroundMaterial);
            }
        }
    }

    /**
     * Adds a background item at the specified index in the GUI.
     *
     * @param index The index in the inventory where the background item will be placed.
     * @param material The material of the background item.
     */
    private void addBackgroundItem(int index, Material material) {
        backgroundList.add(new Background(createItemStack(material, " "), index){});
    }


    /**
     * Creates buttons for each TrinketSlot and adds them to the button map.
     */
    private void createSlotButtons() {
        for (TrinketSlot slot : TrinketSlot.values()) {
            createButton(slot);
        }
    }

    /**
     * Creates a button for a given TrinketSlot and adds it to the button map.
     *
     * @param slot The TrinketSlot for which the button is created.
     */
    private void createButton(TrinketSlot slot) {
        Button button = new Button(slot.getSlot(), slot.getBackground()) {
            @Override
            public void onClick(Player player) {
                buttonOnClickHandler(player, slot.getSlot());
            }
        };
        button.setVisibility(slot.isEnabled());
        buttonMap.put(slot.getSlot(), button);
    }

    /**
     * Handles the click event for a button in the GUI.
     * If the player is holding an item, it is swapped with the button's item. Otherwise, the button's item is placed in the player's cursor.
     *
     * @param player The player who clicked the button.
     * @param slot The slot index of the button clicked.
     */
    private void buttonOnClickHandler(Player player, int slot) {
        Button button = buttonMap.get(slot);

        ItemStack itemOnCursor = player.getItemOnCursor();
        ItemStack buttonItem = button.getContainedItem();

        if (itemOnCursor.getType() != Material.AIR) {
            ItemStack itemToReturn = button.pop();
            button.push(itemOnCursor);
            player.setItemOnCursor(itemToReturn);
        } else {
            player.setItemOnCursor(button.pop());
        }
    }

    /**
     * Creates and sets the player's skull icon in the center of the GUI.
     *
     * @param player The player whose skull is to be displayed.
     */
    private void createPlayerSkullIcon(Player player) {
        ItemStack skull = playerStatsFetcher.createPlayerHead(player.getUniqueId());
        playerStatsFetcher.applyPlayerStats(skull, player);

        inventory.setItem(4, skull);
    }

    /**
     * Creates an ItemStack with the specified material and name.
     *
     * @param material The material of the item.
     * @param name The display name of the item.
     * @return The created ItemStack.
     */
    private ItemStack createItemStack(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Loads all TrinketSlots, initializing their state.
     */
    private void loadTrinketSlots() {
        for (TrinketSlot slot : TrinketSlot.values()) {
            slot.load();
        }
    }
}
