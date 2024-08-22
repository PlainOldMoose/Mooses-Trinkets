package me.plainoldmoose.trinkets.GUI.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StatsIcon extends Button {
    private List<String> rawStatNames = new ArrayList<>();

    public StatsIcon(ItemStack displayItem, int index, List<String> rawStatNames) {
        super(displayItem, index);
        this.rawStatNames = rawStatNames;
    }

    public List<String> getStatsList() {
        return this.getDisplayItem().getItemMeta().getLore();
    }

    public void setListOfStats(List<String> statsList) {
        ItemMeta meta = this.getDisplayItem().getItemMeta();
        meta.setLore(statsList);
        this.getDisplayItem().setItemMeta(meta);
    }

    public List<String> getRawStatNames() {
        return rawStatNames;
    }

    @Override
    public void onClick(Player player) {

    }
}
