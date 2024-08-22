package me.plainoldmoose.trinkets.gui.builders;

import com.willfp.ecoskills.api.EcoSkillsAPI;
import com.willfp.ecoskills.stats.Stats;
import me.plainoldmoose.trinkets.data.loaders.config.GUILoaders.IconConfigLoader;
import me.plainoldmoose.trinkets.data.loaders.eco.SkillsHandler;
import me.plainoldmoose.trinkets.gui.components.StatsIcon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides functionality for fetching and manipulating player statistics,
 * including creating player heads and applying statistics to item metadata.
 */
public class IconBuilder {
    private static List<StatsIcon> iconList = new ArrayList<>();

    public static void createStatsIcons(Player player) {
        // Load the icon list from the IconHandler
        iconList = IconConfigLoader.getInstance().getIconList();

        for (StatsIcon icon : iconList) {
            // Fetch the player stats for the current icon
            List<String> fetchedStats = fetchFormattedPlayerStats(player, icon.getRawStatNames());

            // Set the fetched stats list in the icon
            icon.setListOfStats(fetchedStats);
        }
    }

    /**
     * Fetches and returns a list of player statistics as strings.
     * The statistics are retrieved from the player's profile and formatted
     * according to configured skill names and values.
     *
     * @param player      The player whose statistics are to be fetched.
     * @param listOfStats the list of statistics to be fetched.
     * @return A list of strings representing the player's statistics.
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
                continue; // Use continue to skip empty skill names instead of break
            }

            String displayName = formatNames.get(skill);
            if (displayName != null && displayName.contains("#")) {
                displayName = convertToBukkitColor(displayName);
            }

            int value = EcoSkillsAPI.getStatLevel(player, Stats.INSTANCE.get(skill));
            stats.add(displayName + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + value);
        }
        return stats;
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

    public static List<StatsIcon> getIconList() {
        return iconList;
    }
}
