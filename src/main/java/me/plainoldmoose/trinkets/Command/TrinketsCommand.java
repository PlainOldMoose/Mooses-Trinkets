package me.plainoldmoose.trinkets.Command;


import me.plainoldmoose.trinkets.GUI.TrinketsGUI;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Handles the /trinkets command for players.
 * This command allows players to interact with trinkets by listing them, giving specific trinkets,
 * or opening the trinkets GUI.
 */
public class TrinketsCommand implements CommandExecutor {

    /**
     * Executes the given command.
     *
     * @param sender  The source of the command
     * @param command The command that was executed
     * @param s       The alias of the command which was used
     * @param args    The arguments passed with the command
     * @return true if the command was successfully executed, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!"); // Inform that only players can use the command
            return true;
        }

        Player player = (Player) sender; // Cast the sender to a player

        // No arguments, display the Trinkets GUI
        if (args.length == 0) {
            new TrinketsGUI().displayTo(player);
            return true;
        }

        // List trinkets
        if (args[0].equalsIgnoreCase("list")) {
            List<me.plainoldmoose.moosestrinkets.Data.Trinket> trinketList = Trinkets.getInstance().getManager().getTrinketList();
            if (trinketList.isEmpty()) {
                player.sendMessage("There are no trinkets!");
                return true;
            }

            // Send each trinket's information to the player
            for (me.plainoldmoose.moosestrinkets.Data.Trinket t : trinketList) {
                player.sendMessage(t.getTrinketItem().toString());
            }
            return true;
        }

        // Give a specific trinket to the player
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2 || !isInteger(args[1])) {
                player.sendMessage("Enter a valid trinket ID!");
                return true;
            }

            me.plainoldmoose.moosestrinkets.Data.TrinketManager manager = Trinkets.getInstance().getManager();

            int trinketID = Integer.parseInt(args[1]);
            ItemStack trinket = manager.getTrinketByID(trinketID);

            if (trinket == null) {
                player.sendMessage("This trinket does not exist!");
                return true;
            }

            player.getInventory().addItem(trinket);
            return true;
        }

        // If command not recognized
        player.sendMessage("Unknown command!");
        return true;
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
}