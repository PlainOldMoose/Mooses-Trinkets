package me.plainoldmoose.trinkets.gui.builders;

import com.willfp.ecoskills.api.EcoSkillsAPI;
import com.willfp.ecoskills.stats.Stats;
import me.plainoldmoose.trinkets.data.loaders.config.gui.IconConfigLoader;
import me.plainoldmoose.trinkets.data.loaders.eco.SkillsHandler;
import me.plainoldmoose.trinkets.gui.components.StatsIcon;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the creation and customization of statistical icons for the GUI.
 * This class manages the creation of icons that display player statistics,
 * including fetching player stats, applying them to item metadata, and updating icon visuals.
 */
public class IconBuilder {
    private static List<StatsIcon> iconList = new ArrayList<>();

    /**
     * Creates and customizes statistical icons for the given player.
     * The icons are fetched from the configuration and updated with player-specific data,
     * including player stats, names, and item appearance.
     *
     * @param player The player whose statistics are to be included in the icons.
     */
    public static void createStatsIcons(Player player) {
        // Load the icon list from the IconConfigLoader
        iconList = IconConfigLoader.getInstance().getIconList();

        for (StatsIcon icon : iconList) {
            // Fetch and set player stats for the current icon
            List<String> fetchedStats = fetchFormattedPlayerStats(player, icon.getRawStatNames());
            icon.setListOfStats(fetchedStats);

            ItemStack iconItem = icon.getDisplayItem();
            List<String> stats = icon.getStatsList();

            String iconItemName = iconItem.getItemMeta().getDisplayName();
            // Replace placeholder with player name
            if (iconItemName.contains("%playername%")) {
                iconItemName = iconItemName.replace("%playername%", player.getName());

                // Add player prefix if available
                if (PlayerPrefixBuilder.getInstance().getChat() != null) {
                    iconItemName = ConfigUtils.colorizeString(PlayerPrefixBuilder.getPlayerPrefix(player)) + iconItemName;
                }
            }

            // Replace player head with the player's skin if applicable
            if (iconItem.getType() == Material.PLAYER_HEAD) {
                icon.setDisplayItem(ItemFactory.createPlayerHead(player.getUniqueId()));
            }

            // Update the icon item stack with the new lore and name
            iconItem = icon.getDisplayItem();
            ItemFactory.changeItemStackLore(iconItem, stats);
            icon.setDisplayItem(iconItem);

            ItemFactory.changeItemStackName(icon.getDisplayItem(), iconItemName);
        }
    }

    /**
     * Fetches and formats a list of player statistics as strings.
     * The statistics are retrieved from the player's profile and formatted according to the configured skill names and values.
     *
     * @param player      The player whose statistics are to be fetched.
     * @param listOfStats The list of statistics to be fetched.
     * @return A list of strings representing the formatted player statistics.
     */
    public static List<String> fetchFormattedPlayerStats(Player player, List<String> listOfStats) {
        List<String> stats = new ArrayList<>();
        stats.add(" ");

        Map<String, String> formatNames = SkillsHandler.getInstance().getSkillNameFormat();

        if (listOfStats == null) {
            return stats;
        }

        for (String skill : listOfStats) {
            if (skill.isBlank()) {
                continue; // Skip empty skill names
            }

            String displayName = formatNames.get(skill);
            if (displayName != null && displayName.contains("#")) {
                displayName = convertToBukkitColor(displayName);
            }

//            Stat stat = Stats.INSTANCE.get(skill);
//
//            if (stat == null) {
//                Bukkit.getLogger().severe("[Mooses-Trinkets] There was a problem loading stats from eco");
//                return new ArrayList<>();
//            }

            int value = EcoSkillsAPI.getStatLevel(player, Stats.INSTANCE.get(skill));
            stats.add(displayName + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + value);
        }
        return stats;
    }

    /**
     * Converts a color code from hexadecimal format to Bukkit color code format.
     * This method transforms hexadecimal color codes into the Bukkit format used in chat and item names.
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

    /**
     * Gets the list of statistical icons.
     *
     * @return A list of {@link StatsIcon} objects representing the available statistical icons.
     */
    public static List<StatsIcon> getIconList() {
        return iconList;
    }
}
