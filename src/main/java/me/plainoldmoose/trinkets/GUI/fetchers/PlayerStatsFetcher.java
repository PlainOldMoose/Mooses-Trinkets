package me.plainoldmoose.trinkets.GUI.fetchers;

import com.willfp.eco.core.data.PlayerProfile;
import com.willfp.eco.core.data.keys.PersistentDataKey;
import com.willfp.eco.core.data.keys.PersistentDataKeyType;
import me.plainoldmoose.trinkets.Data.SkillsHandler;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

/**
 * Provides functionality for fetching and manipulating player statistics,
 * including creating player heads and applying statistics to item metadata.
 */
public class PlayerStatsFetcher {
    private static final Logger LOGGER = Bukkit.getLogger();

    /**
     * Fetches and returns a list of player statistics as strings.
     * The statistics are retrieved from the player's profile and formatted
     * according to configured skill names and values.
     *
     * @param player The player whose statistics are to be fetched.
     * @return A list of strings representing the player's statistics.
     */
    public List<String> fetchPlayerStats(Player player) {
        List<String> stats = new ArrayList<>();
        stats.add(" ");
        PlayerProfile profile = PlayerProfile.load(player.getUniqueId());

        if (profile == null) {
            stats.add(ChatColor.RED + "Profile not found!");
            return stats;
        }

        SkillsHandler skillsHandler = TrinketsData.getInstance().getSkillsHandler();

        List<String> order = skillsHandler.getSkillFileNames();
        Map<String, String> formatNames = skillsHandler.getSkillNameFormat();

        for (String skill : order) {
            String displayName = formatNames.get(skill);

            if (displayName != null && displayName.contains("#")) {
                displayName = convertToBukkitColor(displayName);
            }

            NamespacedKey key = new NamespacedKey("ecoskills", skill);
            PersistentDataKey<Integer> intKey = new PersistentDataKey<>(key, PersistentDataKeyType.INT, 0);
            Integer intValue = profile.read(intKey);

            if (intValue != null) {
                stats.add(displayName + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + intValue);
            }
        }

        return stats;
    }

    /**
     * Creates an item stack representing the head of the player with the given UUID.
     *
     * @param playerUUID The UUID of the player whose head is to be created.
     * @return An ItemStack representing the player's head.
     */
    public ItemStack createPlayerHead(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) {
            LOGGER.warning("Player with UUID " + playerUUID + " is not online.");
            return new ItemStack(Material.AIR);
        }

        String playerName = player.getName();
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
            skull.setItemMeta(skullMeta);
        } else {
            LOGGER.severe("Failed to create SkullMeta for player head.");
        }

        return skull;
    }

    /**
     * Applies player statistics to the given item stack's metadata.
     * This includes setting the lore to the player's statistics and the display name
     * to include the player's prefix and name.
     *
     * @param skull  The item stack representing the player's head.
     * @param player The player whose statistics are to be applied.
     */
    public void applyPlayerStats(ItemStack skull, Player player) {
        ItemMeta meta = skull.getItemMeta();
        if (meta != null) {
            meta.setLore(fetchPlayerStats(player));
            meta.setDisplayName("§f" + new ChatServiceFetcher().getPlayerPrefix(player) + player.getName() + "'s Trinkets");
            skull.setItemMeta(meta);
        } else {
            LOGGER.severe("Failed to retrieve ItemMeta for applying player stats.");
        }
    }

    /**
     * This is a chatGPT method, it works, don't touch it.
     * Converts a color code from hexadecimal format to Bukkit color code format.
     * The hexadecimal color code is converted to the Bukkit color format used in chat and item names.
     *
     * @param text The text containing hexadecimal color codes.
     * @return The text with hexadecimal color codes converted to Bukkit color codes.
     */
    public static String convertToBukkitColor(String text) {
        String hexColorPattern = "&#([a-fA-F0-9]{6})";
        Pattern pattern = Pattern.compile(hexColorPattern);
        Matcher matcher = pattern.matcher(text);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group(1);
            String bukkitColor = "§x§" + hexCode.charAt(0) + "§" + hexCode.charAt(1) +
                    "§" + hexCode.charAt(2) + "§" + hexCode.charAt(3) +
                    "§" + hexCode.charAt(4) + "§" + hexCode.charAt(5);
            matcher.appendReplacement(result, bukkitColor);
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
