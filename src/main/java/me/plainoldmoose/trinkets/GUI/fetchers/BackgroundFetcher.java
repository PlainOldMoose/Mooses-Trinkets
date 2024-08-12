package me.plainoldmoose.trinkets.GUI.fetchers;

import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.Data.handlers.ConfigHandler;
import me.plainoldmoose.trinkets.GUI.components.Background;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class BackgroundFetcher {
    private final ConfigHandler configHandler = TrinketsData.getInstance().getConfigHandler();

    public static List<Background> getBackgroundList() {
        return backgroundList;
    }

    private static final List<Background> backgroundList = new ArrayList<>();

    /**
     * Creates background tiles for the GUI based on configuration settings.
     */
    public void createBackgroundTiles() {
        Material backgroundMaterial = configHandler.getBackgroundMaterial();
        Material secondaryMaterial = configHandler.getSecondaryBackgroundMaterial();

        for (int row = 0; row < 6; row++) {
            int firstColumnIndex = row * 9;
            int lastColumnIndex = firstColumnIndex + 8;

            addBackgroundItem(firstColumnIndex, secondaryMaterial);
            addBackgroundItem(lastColumnIndex, secondaryMaterial);

            for (int col = 1; col < 8; col++) {
                int index = firstColumnIndex + col;
                addBackgroundItem(index, backgroundMaterial);
            }
        }
    }

    /**
     * Adds a background item at the specified index in the GUI.
     *
     * @param index    The index in the inventory where the background item will be placed.
     * @param material The material of the background item.
     */
    private void addBackgroundItem(int index, Material material) {
        backgroundList.add(new Background(ItemFactory.createItemStack(material, " "), index) {
        });
    }
}
