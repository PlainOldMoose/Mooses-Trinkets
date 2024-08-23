package me.plainoldmoose.trinkets.gui.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a background item in a GUI.
 * This class extends the {@link Button} class but does not define any click behavior.
 * It is used to display non-interactive background items in the GUI.
 */
public class Background extends Button {
    /**
     * Constructs a new Background instance.
     *
     * @param displayItem The {@link ItemStack} to be displayed as the background item.
     * @param index The slot index in the inventory where the background item is placed.
     */
    public Background(ItemStack displayItem, int index) {
        super(displayItem, index);
    }

    /**
     * No action is taken when this background item is clicked.
     * This method is overridden from the {@link Button} class to provide no functionality.
     *
     * @param player The {@link Player} who clicked the background item.
     */
    @Override
    public void onClick(Player player) {
        // No action for background items
    }
}
