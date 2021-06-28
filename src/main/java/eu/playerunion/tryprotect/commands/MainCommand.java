package eu.playerunion.tryprotect.commands;

import eu.playerunion.tryprotect.TryProtect;
import eu.playerunion.tryprotect.config.ConfigLoader;
import eu.playerunion.tryprotect.config.TPMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class MainCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            // TODO: Implement help list display logic.
            sender.sendMessage("Terépcsecső.");
        } else
        {
            // Single arguments which doesn't have any other sub arguments.
            if (args.length == 1)
            {
                if (args[0].equalsIgnoreCase("reload"))
                {

                    TPMessages.loadMessages();
                    ConfigLoader.reloadConfig();
                    ConfigLoader.saveProtections();

                    sender.sendMessage(ConfigLoader.getPrefix() + TPMessages.RELOAD_SUCCESS.msg());
                    return true;
                } else if (args[0].equalsIgnoreCase("info"))
                {
                    PluginDescriptionFile descriptionFile = TryProtect.getInstance().getDescription();

                    String[] pluginInfo = new String[]{
                            ChatColor.AQUA + "" + ChatColor.BOLD + "♦ TryProtect ♦ " + ChatColor.WHITE + "Verzió: " + ChatColor.AQUA + descriptionFile.getVersion(),
                            ChatColor.WHITE + "● Plugint készítette: " + ChatColor.AQUA + "TryHardDo (tf2levi)",
                            ChatColor.WHITE + "● Legutolsó támogatott verzió: " + ChatColor.AQUA + descriptionFile.getAPIVersion()
                    };

                    sender.sendMessage(pluginInfo);
                } else
                {
                    sender.sendMessage(TPMessages.INVALID_ARGS.msg());
                }
            }
        }
        return true;
    }
}
