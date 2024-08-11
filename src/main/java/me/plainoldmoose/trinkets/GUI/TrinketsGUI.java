package me.plainoldmoose.trinkets.GUI;

import com.willfp.eco.core.data.PlayerProfile;
import com.willfp.eco.core.data.keys.PersistentDataKey;
import com.willfp.eco.core.data.keys.PersistentDataKeyType;
import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.Data.handlers.ConfigHandler;
import me.plainoldmoose.trinkets.Data.handlers.Keys;
import me.plainoldmoose.trinkets.GUI.components.Background;
import me.plainoldmoose.trinkets.GUI.components.Button;
import me.plainoldmoose.trinkets.GUI.components.StatsIcon;
import me.plainoldmoose.trinkets.GUI.components.TrinketSlot;
import me.plainoldmoose.trinkets.GUI.fetchers.ChatServiceFetcher;
import me.plainoldmoose.trinkets.GUI.fetchers.PlayerStatsFetcher;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;

public class TrinketsGUI {
    private final int size = 54; // Inventory size
    // TODO - setup config for this
    private final String title = "               Trinkets"; // GUI title
    private Inventory inventory;

    private final HashMap<Integer, Button> buttonMap = new HashMap<>();
    private final List<Background> backgroundList = new ArrayList<>();
    private List<StatsIcon> iconList = new ArrayList<>();

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
        createStatsIcons(player);
        createSlotButtons();
        updateGUI(player);

        if (player.hasMetadata("TrinketsGUI")) {
            player.closeInventory();
        }

        player.setMetadata("TrinketsGUI", new FixedMetadataValue(Trinkets.getInstance(), this));
        player.openInventory(inventory);

        for (StatsIcon icon : iconList) {
            List<String> stats = icon.getStatsList();
        }
    }

    private ItemStack changeItemStackName(ItemStack item, String newName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(newName);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack changeItemStackLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private void renderBackgrounds() {
        for (Background background : backgroundList) {
            inventory.setItem(background.getSlot(), background.getItem());
        }
    }

    private void renderStatIcons(Player player) {
        for (StatsIcon icon : iconList) {
            String iconItemName = icon.getItem().getItemMeta().getDisplayName();
            List<String> stats = icon.getStatsList();


            // This is some fancy shenanigans because ItemStack is bad and I hate bukkit.
            if (iconItemName.contains("%playername%")) {
                if (icon.getItem().getType() == Material.PLAYER_HEAD) {
                    icon.setItem(createPlayerHead(player.getUniqueId()));
                }

                iconItemName = iconItemName.replace("%playername%", player.getDisplayName());
                icon.setItem(changeItemStackName(icon.getItem(), iconItemName));
                icon.setItem(changeItemStackLore(icon.getItem(), stats));
            }

            inventory.setItem(icon.getSlot(), icon.getItem());
        }
    }

    private void renderButtons() {
        for (Map.Entry<Integer, Button> entry : buttonMap.entrySet()) {
            Button button = entry.getValue();
            if (button.isEnabled()) {
                inventory.setItem(button.getSlot(), button.getContainedItem() != null ? button.getContainedItem() : button.getItem());
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
        createStatsIcons(player);
        renderStatIcons(player);
        renderButtons();
    }

    private void createStatsIcons(Player player) {
        // Load the icon list from the IconHandler
        iconList = TrinketsData.getInstance().getIconHandler().getIconList();

        for (StatsIcon icon : iconList) {
            // Fetch the player stats for the current icon
            List<String> fetchedStats = playerStatsFetcher.fetchPlayerStats(player, icon.getRawStatNames());

            // Set the fetched stats list in the icon
            icon.setListOfStats(fetchedStats);
        }
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
     * @param index    The index in the inventory where the background item will be placed.
     * @param material The material of the background item.
     */
    private void addBackgroundItem(int index, Material material) {
        backgroundList.add(new Background(createItemStack(material, " "), index) {
        });
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
     * @param slot   The slot index of the button clicked.
     */
    private void buttonOnClickHandler(Player player, int slot) {

        // TODO - implement removing item too
        ItemStack item = player.getItemOnCursor();
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer itemContainer = player.getItemOnCursor().getItemMeta().getPersistentDataContainer();

        if (!itemContainer.has(Keys.TRINKET)) {
            return;
        }

        PlayerProfile profile = PlayerProfile.load(player.getUniqueId());
        TrinketManager manager = Trinkets.getInstance().getManager();

        // TODO - find better way to retrieve trinket from itemstack.
        Trinket t = manager.getTrinketByDisplayName(meta.getDisplayName());

        Map<String, Integer> statsMap = t.getStats();

        for (Map.Entry e : statsMap.entrySet()) {
            String stat = (String) e.getKey();
            int value = (int) e.getValue();

            NamespacedKey key = new NamespacedKey("ecoskills", stat);
            PersistentDataKey<Integer> intKey = new PersistentDataKey<>(key, PersistentDataKeyType.INT, 0);

            Integer intValue = profile.read(intKey);
            intValue += value;

            profile.write(intKey, intValue);

            player.sendMessage("Updated > " + intValue);

//        Button button = buttonMap.get(slot);
//
//        ItemStack itemOnCursor = player.getItemOnCursor();
//        ItemStack buttonItem = button.getContainedItem();
//
//        if (itemOnCursor.getType() != Material.AIR) {
//            ItemStack itemToReturn = button.pop();
//            button.push(itemOnCursor);
//            player.setItemOnCursor(itemToReturn);
//        } else {
//            player.setItemOnCursor(button.pop());
//        }
//
//        TrinketsData.getInstance().loadConfig();
        }
    }

    /**
     * Creates an ItemStack with the specified material and name.
     *
     * @param material The material of the item.
     * @param name     The display name of the item.
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

    public List<Background> getBackgroundList() {
        return backgroundList;
    }

    public HashMap<Integer, Button> getButtonMap() {
        return buttonMap;
    }

    /**
     * Creates an item stack representing the head of the player with the given UUID.
     *
     * @param playerUUID The UUID of the player whose head is to be created.
     * @return An ItemStack representing the player's head.
     */
    public ItemStack createPlayerHead(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);

        String playerName = player.getName();
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        skull.setItemMeta(skullMeta);

        return skull;
    }

}
