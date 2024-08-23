package me.plainoldmoose.trinkets.data.trinket;

import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.NamespacedKey;

/**
 * Provides predefined NamespacedKeys used in the Trinkets plugin.
 * This class contains constants for various NamespacedKeys that are used
 * to uniquely identify and reference trinkets within the plugin.
 */
public class Key {
    /**
     * The NamespacedKey for identifying trinkets.
     * This key is used to reference trinket-related data and operations within the plugin.
     */
    public static final NamespacedKey TRINKET = new NamespacedKey(Trinkets.getInstance(), "Trinket");
}
