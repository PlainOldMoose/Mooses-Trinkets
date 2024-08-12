package me.plainoldmoose.trinkets.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class ItemFactory {

    /**
     * Creates an ItemStack with the specified material and name.
     *
     * @param material The material of the item.
     * @param name     The display name of the item.
     * @return The created ItemStack.
     */
    public static ItemStack createItemStack(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Creates an item stack representing the head of the player with the given UUID.
     *
     * @param playerUUID The UUID of the player whose head is to be created.
     * @return An ItemStack representing the player's head.
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

    public static ItemStack changeItemStackName(ItemStack item, String newName) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(newName);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack changeItemStackLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
