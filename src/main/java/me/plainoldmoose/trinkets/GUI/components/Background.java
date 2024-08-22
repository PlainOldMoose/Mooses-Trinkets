package me.plainoldmoose.trinkets.GUI.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a background item in a GUI with a specific slot.
 */
public class Background extends Button {
    @Override
    public void onClick(Player player) {}

    public Background(ItemStack displayItem, int index) {
        super(displayItem, index);
    }
}
