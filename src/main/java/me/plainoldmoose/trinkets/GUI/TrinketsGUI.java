package me.plainoldmoose.trinkets.GUI;

import me.plainoldmoose.trinkets.Trinkets;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;
import java.util.function.Consumer;

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

    Inventory inventory; // Inventory instance for the GUI

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
     * Displays the Trinkets GUI to the specified player.
     *
     * @param player Player to display the GUI to.
     */
    public void displayTo(Player player) {
        setupChat(); // Ensure chat is set up for prefix handling
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
            inventory.setItem(button.getSlot(), button.getItem());
        }

        createPlayerSkullIcon(player); // Create and set the player's skull icon
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
        createButton(13, Material.RED_STAINED_GLASS_PANE, "Head Slot", this::headSlotAction);
        createButton(22, Material.ORANGE_STAINED_GLASS_PANE, "Necklace Slot", player -> player.sendMessage("Neckslot"));
        createButton(21, Material.GREEN_STAINED_GLASS_PANE, "Left arm Slot", player -> player.sendMessage("Left slot"));
        createButton(23, Material.CYAN_STAINED_GLASS_PANE, "Right arm slot", player -> player.sendMessage("Right arm Slot"));
        createButton(31, Material.PINK_STAINED_GLASS_PANE, "Leg slot", player -> player.sendMessage("Leg Slot"));
        createButton(40, Material.YELLOW_STAINED_GLASS_PANE, "Feet slot", player -> player.sendMessage("Feet Slot"));
    }

    /**
     * Creates a button and adds it to the button list.
     *
     * @param slot          Slot number for the button.
     * @param material      Material of the button.
     * @param displayName   Display name of the button.
     * @param onClickAction Action to be performed when the button is clicked.
     */
    private void createButton(int slot, Material material, String displayName, Consumer<Player> onClickAction) {
        Button button = new Button(slot, createItemStack(material, displayName)) {
            @Override
            public void onClick(Player player) {
                onClickAction.accept(player);
            }
        };
        buttonMap.put(slot, button);
    }

    /**
     * Action performed when the head slot button is clicked.
     *
     * @param player Player who clicked the button.
     */
    private void headSlotAction(Player player) {
        player.sendMessage("Headslot");
    }

//    private void checkCursorItemType(ItemStack item, Trinket trinketSlot) {
//        if
//    }

    private void insertItemIntoSlot(ItemStack item, Button button) {
        inventory.setItem(button.getSlot(), item);
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