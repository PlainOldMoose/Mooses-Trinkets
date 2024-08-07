package me.plainoldmoose.trinkets.GUI;

import com.willfp.eco.core.data.PlayerProfile;
import com.willfp.eco.core.data.keys.PersistentDataKey;
import com.willfp.eco.core.data.keys.PersistentDataKeyType;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.Trinkets;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Class representing the Trinkets GUI.
 * Provides methods to create and display the trinkets inventory GUI to players.
 */
public class TrinketsGUI {
    final int size = 54; // Inventory size
    final String title = "               Trinkets"; // GUI title
    final HashMap<Integer, Button> buttonMap = new HashMap<Integer, Button>(); // List of buttons in the GUI
    final List<Background> backgroundList = new ArrayList<>(); // List of background tiles in the GUI
    private static Chat chat = null; // Vault chat instance for prefix handling
    private Inventory inventory; // Inventory instance for the GUI

    private int HEAD_TRINKET_SLOT;
    private ItemStack HEAD_TRINKET_BACKGROUND;
    private boolean HEAD_TRINKET_ENABLED;

    private int NECK_TRINKET_SLOT;
    private ItemStack NECK_TRINKET_BACKGROUND;
    private boolean NECK_TRINKET_ENABLED;

    private int LEFT_ARM_TRINKET_SLOT;
    private ItemStack LEFT_ARM_TRINKET_BACKGROUND;
    private boolean LEFT_ARM_TRINKET_ENABLED;

    private int RIGHT_ARM_TRINKET_SLOT;
    private ItemStack RIGHT_ARM_TRINKET_BACKGROUND;
    private boolean RIGHT_ARM_TRINKET_ENABLED;

    private int LEG_TRINKET_SLOT;
    private ItemStack LEG_TRINKET_BACKGROUND;
    private boolean LEG_TRINKET_ENABLED;

    private int FEET_TRINKET_SLOT;
    private ItemStack FEET_TRINKET_BACKGROUND;
    private boolean FEET_TRINKET_ENABLED;

    /**
     * Sets up Vault chat for handling player prefixes.
     *
     * @return true if setup was successful, false otherwise.
     */
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            System.out.println("RSP NOT FOUND!");
        }
        chat = rsp.getProvider();
        return chat != null;
    }

    /**
     * Gets the prefix of the player from Vault chat.
     *
     * @param player Player whose prefix is to be retrieved.
     * @return Prefix of the player.
     */
    public String getPlayerPrefix(Player player) {
        if (!this.chat.getPlayerPrefix(player).isEmpty()) {
            return this.chat.getPlayerPrefix(player);
        }
        return "";
    }

    /**
     * Displays the Trinkets GUI to the specified player.
     *
     * @param player Player to display the GUI to.
     */
    public void displayTo(Player player) {
        loadTrinketSlots();

        if (!setupChat()) {
            return; // Cannot proceed without chat setup failed
        }
        inventory = Bukkit.createInventory(player, this.size, this.title);
        createBackgroundTiles(); // Create the background tiles
        createSlotButtons(); // Create the slot buttons
        updateGUI(player); // Update the GUI with items

        // Ensure no leftover metadata interferes
        if (player.hasMetadata("TrinketsGUI")) {
            player.closeInventory();
        }

        // Set metadata to indicate the Trinkets GUI is open and display it
        player.setMetadata("TrinketsGUI", new FixedMetadataValue(Trinkets.getInstance(), this));
        player.openInventory(inventory);
    }

    /**
     * Updates the GUI by setting items for buttons and background tiles.
     */
    public void updateGUI(Player player) {
        // Set items for background tiles
        for (Background background : backgroundList) {
            inventory.setItem(background.getSlot(), background.getItem());
        }

        for (Map.Entry<Integer, Button> entry : buttonMap.entrySet()) {
            Button button = entry.getValue();
            if (button.isEnabled()) {
                if (button.getContainedItem() != null) {
                    inventory.setItem(button.getSlot(), button.getContainedItem());
                } else {
                    inventory.setItem(button.getSlot(), button.getItem());
                }
            }
        }

        createPlayerSkullIcon(player); // Create and set the player's skull icon
    }


    // TODO - implement fetching all relevant skill data
    public String fetchPlayerStats(Player player) {
        PlayerProfile profile = PlayerProfile.load(player.getUniqueId());
        NamespacedKey key = new NamespacedKey("ecoskills", "mining");
        PersistentDataKey dataKey = new PersistentDataKey(key, PersistentDataKeyType.STRING, "");

        return ChatColor.GOLD + "Mining: " + profile.read(dataKey).toString();
    }

    /**
     * Creates background tiles for the GUI.
     */
    public void createBackgroundTiles() {
        for (int i = 0; i < this.size; i++) {
            backgroundList.add(new Background(createItemStack(Material.GRAY_STAINED_GLASS_PANE, " "), i) {
            });
        }
    }

    /**
     * Creates the slot buttons for the GUI.
     */
    public void createSlotButtons() {
        createButton(HEAD_TRINKET_SLOT, HEAD_TRINKET_BACKGROUND, this::trinketOnClickHandler, HEAD_TRINKET_ENABLED);
        createButton(NECK_TRINKET_SLOT, NECK_TRINKET_BACKGROUND, this::trinketOnClickHandler, NECK_TRINKET_ENABLED);
        createButton(LEFT_ARM_TRINKET_SLOT, LEFT_ARM_TRINKET_BACKGROUND, this::trinketOnClickHandler, LEFT_ARM_TRINKET_ENABLED);
        createButton(RIGHT_ARM_TRINKET_SLOT, RIGHT_ARM_TRINKET_BACKGROUND, this::trinketOnClickHandler, RIGHT_ARM_TRINKET_ENABLED);
        createButton(LEG_TRINKET_SLOT, LEG_TRINKET_BACKGROUND, this::trinketOnClickHandler, LEG_TRINKET_ENABLED);
        createButton(FEET_TRINKET_SLOT, FEET_TRINKET_BACKGROUND, this::trinketOnClickHandler, FEET_TRINKET_ENABLED);
    }

    /**
     * Creates a button and adds it to the button list.
     *
     * @param slot          Slot number for the button.
     * @param item          Item for the button.
     * @param onClickAction Action to be performed when the button is clicked.
     */
    private void createButton(int slot, ItemStack item, BiConsumer<Player, Integer> onClickAction, boolean enabled) {
        Button button = new Button(slot, item) {
            @Override
            public void onClick(Player player) {
                onClickAction.accept(player, slot);
            }
        };

        button.setVisibility(enabled);
        buttonMap.put(slot, button);
    }

    private void trinketOnClickHandler(Player player, int slot) {
        Button button = buttonMap.get(slot);

        ItemStack itemOnCursor = player.getItemOnCursor();
        ItemStack buttonItem = button.getContainedItem();

        if (!(itemOnCursor.getType() == Material.AIR)) {
            ItemStack itemToReturn = button.pop();
            button.push(itemOnCursor);
            player.setItemOnCursor(itemToReturn);

            return;
        }

        player.setItemOnCursor(button.pop());
    }

//    // TODO - replace this with data loading, i.e. should have default in config.yml or if config provided load from there instead
//    private static ItemStack createDefaultBackground(Material material) {
//        ItemStack background = new ItemStack(material);
//        ItemMeta meta = background.getItemMeta();
//        if (meta != null) {
//            meta.setDisplayName(" "); // Set to a blank space
//            background.setItemMeta(meta);
//        }
//        return background;
//    }

    /**
     * Gets the list of buttons in the GUI.
     *
     * @return List of Button objects.
     */
    public final HashMap<Integer, Button> getButtonMap() {
        return buttonMap;
    }

    /**
     * Gets the list of background tiles in the GUI.
     *
     * @return List of Background objects.
     */
    public final List<Background> getBackgrounds() {
        return backgroundList;
    }

    /**
     * Checks if the given ItemStack has correct MetaData to be enabled.
     *
     * @param item The ItemStack to check.
     * @return True if the trinket slot is enabled, false otherwise.
     */
    private boolean isTrinketSlotEnabled(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey enabledKey = new NamespacedKey(Trinkets.getInstance(), "enabled");

        return dataContainer.has(enabledKey, PersistentDataType.INTEGER) && dataContainer.get(enabledKey, PersistentDataType.INTEGER) == 1;
    }

    /**
     * Retrieves the trinket slot index from the given ItemStack.
     *
     * @param item The ItemStack to retrieve the slot index from.
     * @return The trinket slot index.
     */
    private int getTrinketSlotIndex(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey slotKeyNamespace = new NamespacedKey(Trinkets.getInstance(), "slot");

        return dataContainer.get(slotKeyNamespace, PersistentDataType.INTEGER);
    }

    /**
     * Loads the configuration for all trinket slots.
     */
    private void loadTrinketSlots() {
        loadTrinketSlotConfig("HEAD");
        loadTrinketSlotConfig("NECK");
        loadTrinketSlotConfig("LEFT_ARM");
        loadTrinketSlotConfig("RIGHT_ARM");
        loadTrinketSlotConfig("LEG");
        loadTrinketSlotConfig("FEET");
    }

    /**
     * Loads the configuration for a specific trinket slot and updates relevant fields.
     *
     * @param slotKey The key of the trinket slot to load.
     */
    private void loadTrinketSlotConfig(String slotKey) {
        ItemStack background = TrinketsData.getInstance().getTrinketSlotMap().get(slotKey);
        int slot = getTrinketSlotIndex(background);
        boolean isEnabled = isTrinketSlotEnabled(background);

        switch (slotKey) {
            case "HEAD":
                HEAD_TRINKET_SLOT = slot;
                HEAD_TRINKET_BACKGROUND = background;
                HEAD_TRINKET_ENABLED = isEnabled;
                break;
            case "NECK":
                NECK_TRINKET_SLOT = slot;
                NECK_TRINKET_BACKGROUND = background;
                NECK_TRINKET_ENABLED = isEnabled;
                break;
            case "LEFT_ARM":
                LEFT_ARM_TRINKET_SLOT = slot;
                LEFT_ARM_TRINKET_BACKGROUND = background;
                LEFT_ARM_TRINKET_ENABLED = isEnabled;
                break;
            case "RIGHT_ARM":
                RIGHT_ARM_TRINKET_SLOT = slot;
                RIGHT_ARM_TRINKET_BACKGROUND = background;
                RIGHT_ARM_TRINKET_ENABLED = isEnabled;
                break;
            case "LEG":
                LEG_TRINKET_SLOT = slot;
                LEG_TRINKET_BACKGROUND = background;
                LEG_TRINKET_ENABLED = isEnabled;
                break;
            case "FEET":
                FEET_TRINKET_SLOT = slot;
                FEET_TRINKET_BACKGROUND = background;
                FEET_TRINKET_ENABLED = isEnabled;
                break;
            default:
                break;
        }
    }

    /**
     * Creates and sets the player's skull icon in the GUI.
     *
     * @param player Player whose skull icon is to be created.
     */
    public void createPlayerSkullIcon(Player player) {
        UUID PlayerUUID = player.getPlayer().getUniqueId();
        ItemStack skull = createPlayerHead(PlayerUUID);

        ItemMeta meta = skull.getItemMeta();
        meta.setLore(Arrays.asList(fetchPlayerStats(player)));
        meta.setDisplayName("§4§l" + getPlayerPrefix(player) + player.getName() + "'s trinkets");
        skull.setItemMeta(meta);

        inventory.setItem(4, skull);
    }

    /**
     * Creates a player head item stack.
     *
     * @param PlayerUUID UUID of the player.
     * @return ItemStack representing the player's head.
     */
    public ItemStack createPlayerHead(UUID PlayerUUID) {
        String playerName = Bukkit.getPlayer(PlayerUUID).getName();
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        skull.setItemMeta(skullMeta);

        return skull;
    }

    /**
     * Creates an item stack with the specified material and name.
     *
     * @param material Material of the item stack.
     * @param name     Display name of the item stack.
     * @return ItemStack with the specified material and name.
     */
    public ItemStack createItemStack(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }
}