package me.plainoldmoose.trinkets.GUI.components;

import me.plainoldmoose.trinkets.GUI.interactions.TrinketInteractionHandler;
import me.plainoldmoose.trinkets.trinket.TrinketType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class TrinketSlot extends Button {
    private ItemStack containedTrinket;
    private TrinketType type;

    public TrinketSlot(ItemStack displayItem, TrinketType type, int inventoryIndex) {
        super(displayItem, inventoryIndex);
        this.type = type;
    }

    @Override
    public void onClick(Player player) {
        TrinketInteractionHandler.handleButtonClick(player, this);
    }

    public void push(ItemStack trinket) {
        containedTrinket = trinket;
    }

    public ItemStack pop() {
        if (containedTrinket == null) {
            return new ItemStack(Material.AIR);
        }
        ItemStack item = containedTrinket;
        containedTrinket = null;
        return item;
    }

    public ItemStack getContainedTrinket() {
        return containedTrinket;
    }

    public TrinketType getType() {
        return type;
    }
}
