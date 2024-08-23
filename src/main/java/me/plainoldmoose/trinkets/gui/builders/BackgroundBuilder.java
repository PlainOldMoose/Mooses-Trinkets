package me.plainoldmoose.trinkets.gui.builders;

import me.plainoldmoose.trinkets.data.loaders.config.GUILoaders.BackgroundItemConfigLoader;
import me.plainoldmoose.trinkets.gui.components.Background;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs background tiles for the GUI based on configuration settings.
 * This class is responsible for creating and managing the background items used in the GUI.
 */
public class BackgroundBuilder {
    private static final List<Background> backgroundList = new ArrayList<>();

    /**
     * Creates background tiles for the GUI using the configured materials.
     * It sets up the background items for each row and column according to the defined layout.
     */
    public static void createBackgroundTiles() {
        Material backgroundMaterial = BackgroundItemConfigLoader.getInstance().getBackgroundMaterial();
        Material secondaryMaterial = BackgroundItemConfigLoader.getInstance().getSecondaryBackgroundMaterial();

        for (int row = 0; row < 6; row++) {
            int firstColumnIndex = row * 9;
            int lastColumnIndex = firstColumnIndex + 8;

            // Add background items to the first and last columns
            addBackgroundItem(firstColumnIndex, secondaryMaterial);
            addBackgroundItem(lastColumnIndex, secondaryMaterial);

            // Add background items to the middle columns
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
    private static void addBackgroundItem(int index, Material material) {
        backgroundList.add(new Background(ItemFactory.createItemStack(material, " "), index) {
        });
    }

    /**
     * Gets the list of background items.
     *
     * @return A list of {@link Background} objects representing the background tiles.
     */
    public static List<Background> getBackgroundList() {
        return backgroundList;
    }
}
