package eu.playerunion.tryprotect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class MessageSender
{
    public static void sendPluginEnableMessage()
    {
        PluginDescriptionFile descriptionFile = TryProtect.getInstance().getDescription();
        String[] enableLines = new String[]{
                ChatColor.DARK_GRAY + "+",
                ChatColor.DARK_GRAY + "| " + ChatColor.RED + "TryProtect " + ChatColor.GRAY + descriptionFile.getVersion(),
                ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + "Plugin developed by: " + ChatColor.YELLOW + "TryHardDo (tf2levi)",
                ChatColor.DARK_GRAY + "| " + ChatColor.GRAY + "Running on: " + ChatColor.YELLOW + Bukkit.getServer().getVersion(),
                ChatColor.DARK_GRAY + "+"
        };
        Bukkit.getConsoleSender().sendMessage(enableLines);
    }

    public static void sendPluginDisableMessage()
    {
        String[] disableLines = new String[]{
                ChatColor.YELLOW + "Stopping services and schedulers. Good bye! :)"
        };
        Bukkit.getConsoleSender().sendMessage(disableLines);
    }

    public static void sendPlayerJoinEventMessage(Player targetUser)
    {

    }
}
