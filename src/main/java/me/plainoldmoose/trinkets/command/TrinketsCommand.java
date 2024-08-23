package me.plainoldmoose.trinkets.command;

import me.plainoldmoose.trinkets.data.trinket.Trinket;
import me.plainoldmoose.trinkets.data.trinket.TrinketManager;
import me.plainoldmoose.trinkets.data.TrinketsConfigHandler;
import me.plainoldmoose.trinkets.data.loaders.config.MessageConfigLoader;
import me.plainoldmoose.trinkets.gui.TrinketsGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the /trinkets command for players.
 * This command allows players to interact with trinkets by listing them, giving specific trinkets,
 * or opening the trinkets GUI.
 */
public class TrinketsCommand implements CommandExecutor, TabCompleter {
    private static final TrinketsCommand instance = new TrinketsCommand();

    private String ONLY_PLAYERS_MESSAGE;
    private String INVALID_TRINKET_ID_MESSAGE;
    private String TRINKET_NOT_FOUND_MESSAGE;
    private String UNKNOWN_COMMAND_MESSAGE;
    private String RELOADED_MESSAGE;
    private String NO_TRINKETS_MESSAGE;
    private String PREFIX;

    /**
     * Updates the command messages from the configuration.
     */
    public void update() {
        HashMap<String, String> messagesMap = MessageConfigLoader.getInstance().getMessagesMap();

        PREFIX = messagesMap.get("prefix");
        ONLY_PLAYERS_MESSAGE = "Only players can use this command!";
        INVALID_TRINKET_ID_MESSAGE = messagesMap.get("invalid_trinket");
        TRINKET_NOT_FOUND_MESSAGE = messagesMap.get("trinket_not_found");
        UNKNOWN_COMMAND_MESSAGE = messagesMap.get("unknown_command");
        RELOADED_MESSAGE = messagesMap.get("reload");
        NO_TRINKETS_MESSAGE = messagesMap.get("no_trinkets");
    }

    /**
     * Executes the given command.
     *
     * @param sender  The source of the command
     * @param command The command that was executed
     * @param label   The alias of the command which was used
     * @param args    The arguments passed with the command
     * @return true if the command was successfully executed, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYERS_MESSAGE);
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            try {
                new TrinketsGUI().displayTo(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(player);
                return true;
            case "list":
                handleList(player);
                return true;
            case "give":
                handleGive(player, args);
                return true;
            case "reset":
                handleReset(player);
                return true;
            default:
                player.sendMessage(PREFIX + UNKNOWN_COMMAND_MESSAGE);
                return true;
        }
    }

    private void handleReset(Player player) {
//        Map<UUID, List<ItemStack>> equippedTrinkets = DataHandler.getInstance().getEquippedTrinkets();
//        for (Map.Entry<UUID, List<ItemStack>> entry : equippedTrinkets.entrySet()) {
//            UUID playerUUID = entry.getKey();
//            List<ItemStack> trinketItems = entry.getValue();
//
//            TrinketManager manager = Trinkets.getInstance().getManager();
//
//            for (ItemStack ti : trinketItems) {
//                Trinket t = manager.getTrinket(ti);
//                TrinketInteractionHandler.resetPlayerStats(player, t.getStats());
//            }
//        }
//        // TODO - add message for resetting
//        player.sendMessage(PREFIX + RELOADED_MESSAGE);
    }

    /**
     * Handles the /trinkets reload command.
     *
     * @param player The player executing the command
     */
    private void handleReload(Player player) {
        TrinketsConfigHandler.getInstance().reloadConfig();
        player.sendMessage(PREFIX + RELOADED_MESSAGE);
    }

    /**
     * Handles the /trinkets list command.
     *
     * @param player The player executing the command
     */
    private void handleList(Player player) {
        List<Trinket> trinketList = TrinketManager.getInstance().getTrinketList();
        if (trinketList.isEmpty()) {
            player.sendMessage(PREFIX + NO_TRINKETS_MESSAGE);
            return;
        }

        trinketList.forEach(trinket -> player.sendMessage(trinket.getTrinketItem().toString()));
    }

    /**
     * Handles the /trinkets give command.
     *
     * @param player The player executing the command
     * @param args   The arguments passed with the command
     */
    private void handleGive(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(PREFIX + INVALID_TRINKET_ID_MESSAGE);
            return;
        }

        TrinketManager manager = TrinketManager.getInstance();

        ItemStack trinket = manager.getTrinketItemStack(args[1]);

        if (trinket == null) {
            player.sendMessage(PREFIX + TRINKET_NOT_FOUND_MESSAGE);
            return;
        }

        player.getInventory().addItem(trinket);
    }

    /**
     * Checks if the given string is a valid integer.
     *
     * @param s The string to check
     * @return true if the string is a valid integer, false otherwise
     */
    private boolean isInteger(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the singleton instance of the TrinketsCommand class.
     *
     * @return The singleton instance of the TrinketsCommand class
     */
    public TrinketsCommand getInstance() {
        return instance;
    }

    /**
     * Provides tab completion for the wardrobe command.
     *
     * @param sender  The source of the command
     * @param command The command that was executed
     * @param label   The alias of the command which was used
     * @param args    The arguments passed to the command
     * @return A list of possible completions for the last argument, or null for all players
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("trinkets.admin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            completions.add("reload");
            completions.add("reset");
            completions.add("give");
            completions.add("cringe");

            return completions.stream()
                    .filter(option -> option.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            // Tab completion for the second argument (player names for reset)
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            // Tab completion for the second argument (player names for reset)
            return TrinketManager.getInstance().getTrinketNameList();
        }

        return new ArrayList<>(); // Return an empty list if no completions are found
    }
}


