package me.plainoldmoose.trinkets.gui.components;

import me.plainoldmoose.trinkets.data.trinket.TrinketType;
import me.plainoldmoose.trinkets.gui.interactions.TrinketInteractionHandler;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class TrinketSlot extends Button implements ConfigurationSerializable {
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


    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("containedTrinket", containedTrinket); // Ensure containedTrinket is serializable
        map.put("displayItem", this.getDisplayItem()); // Ensure this.getDisplayItem() is serializable
        map.put("index", this.getIndex());
        map.put("type", type.serialize()); // Ensure type is serializable, or convert it to a serializable format
        return map;
    }

    public static TrinketSlot deserialize(Map<String, Object> map) {
        ItemStack containedTrinket = (ItemStack) map.get("containedTrinket");
        TrinketType type = TrinketType.deserialize((Map<String, Object>) map.get("type")); // Ensure that type is correctly cast
        int index = (int) map.get("index");
        ItemStack displayItem = (ItemStack) map.get("displayItem"); // Use the correct key here

        TrinketSlot newSlot = new TrinketSlot(displayItem, type, index);
        newSlot.push(containedTrinket);
        return newSlot;
    }

}
