package eu.playerunion.tryprotect.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import eu.playerunion.tryprotect.TryProtect;
import eu.playerunion.tryprotect.config.ConfigLoader;
import eu.playerunion.tryprotect.config.TPMessages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // TODO: Implement help list display logic.
            sender.sendMessage("Terépcsecső.");
        } else {
            // Single arguments which doesn't have any other sub arguments.
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    // Reload messages
                    TPMessages.loadMessages();
                    ConfigLoader.reloadConfig();
                    ConfigLoader.saveProtections();

                    // TODO Implement reload logic
                    sender.sendMessage(ConfigLoader.getPrefix() + TPMessages.RELOAD_SUCCESS.msg());
                    return true;
                } else if (args[0].equalsIgnoreCase("info")) {
                    PluginDescriptionFile descriptionFile = TryProtect.getInstance().getDescription();
                    String[] pluginInfo = new String[]{
                            ChatColor.AQUA + "" + ChatColor.BOLD + "♦ TryProtect ♦ " + ChatColor.WHITE + "Verzió: " + ChatColor.AQUA + descriptionFile.getVersion(),
                            ChatColor.WHITE + "● Plugint készítette: " + ChatColor.AQUA + "TryHardDo (tf2levi)",
                            ChatColor.WHITE + "● Legutolsó támogatott verzió: " + ChatColor.AQUA + descriptionFile.getAPIVersion()
                    };

                    sender.sendMessage(pluginInfo);
                } else if (args[0].equalsIgnoreCase("vector")) {
                    if (sender instanceof Player) {
                        Player p = ((Player) sender);
                        Location pLoc = p.getLocation();

                        sender.sendMessage("Jelenlegi pozíció: " + pLoc);

                        BlockVector3 wrapped = BukkitAdapter.asBlockVector(pLoc);

                        sender.sendMessage("Multidimenzós vektorobjektum: " + wrapped.toString());
                    } else {
                        sender.sendMessage(TPMessages.PLAYER_ONLY_COMMAND.msg());
                    }
                } else if (args[0].equalsIgnoreCase("testzone")) {
                    if (sender instanceof Player) {
                        Player target = ((Player) sender);

                        sender.sendMessage("Terület létrehozva! 10x10x10");
                    } else {
                        sender.sendMessage(TPMessages.PLAYER_ONLY_COMMAND.msg());
                    }
                } else if (args[0].equalsIgnoreCase("cache")) {
                    sender.sendMessage(TryProtect.getProtectionObjectHashMap().toString());
                }
            }
        }
        return true;
    }
}
