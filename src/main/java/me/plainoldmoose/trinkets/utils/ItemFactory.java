package me.plainoldmoose.trinkets.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

/**
 * A utility class for creating and modifying {@link ItemStack} objects.
 * Provides methods to create items with specific properties and modify their attributes.
 */
public class ItemFactory {

    /**
     * Creates an {@link ItemStack} with the specified material and display name.
     *
     * @param material The material of the item.
     * @param name     The display name of the item.
     * @return The created {@link ItemStack} with the specified material and display name.
     */
    public static ItemStack createItemStack(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Creates an {@link ItemStack} representing the head of the player with the specified UUID.
     *
     * @param playerUUID The UUID of the player whose head is to be created.
     * @return An {@link ItemStack} representing the player's head.
     */
    public static ItemStack createPlayerHead(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);

        String playerName = player.getName();
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        skull.setItemMeta(skullMeta);

        return skull;
    }

    /**
     * Changes the display name of the given {@link ItemStack}.
     *
     * @param item     The {@link ItemStack} to modify.
     * @param newName  The new display name to set for the item.
     * @return The modified {@link ItemStack} with the new display name.
     */
    public static ItemStack changeItemStackName(ItemStack item, String newName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(newName);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Changes the lore of the given {@link ItemStack}.
     *
     * @param item     The {@link ItemStack} to modify.
     * @param lore     The new lore to set for the item.
     * @return The modified {@link ItemStack} with the new lore.
     */
    public static ItemStack changeItemStackLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
