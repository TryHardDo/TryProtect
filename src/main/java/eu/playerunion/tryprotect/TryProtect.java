package eu.playerunion.tryprotect;

import com.sk89q.worldguard.WorldGuard;
import eu.playerunion.tryprotect.arguments.BrigadierLoader;
import eu.playerunion.tryprotect.commands.MainCommand;
import eu.playerunion.tryprotect.config.ConfigLoader;
import eu.playerunion.tryprotect.config.TPMessages;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TryProtect extends JavaPlugin
{
    private static final HashMap<String, String> protectionObjectHashMap = new LinkedHashMap<>();
    private static TryProtect instance;
    private static WorldGuard worldGuard;
    private static Logger logger;

    // Static methods
    public static TryProtect getInstance()
    {
        return instance;
    }

    public static WorldGuard getWorldGuard()
    {
        return worldGuard;
    }

    public static HashMap<String, String> getProtectionObjectHashMap()
    {
        return protectionObjectHashMap;
    }

    public static void log(String message, Level level)
    {
        logger.log(level, message);
    }

    // JavaPlugin implementation
    @Override
    public void onLoad()
    {
        instance = this;
        logger = getLogger();
        worldGuard = WorldGuard.getInstance();
    }

    @Override
    public void onEnable()
    {
        MessageSender.sendPluginEnableMessage();
        this.init();
    }

    @Override
    public void onDisable()
    {
        ConfigLoader.saveProtections();
        Bukkit.getScheduler().cancelTasks(this);
        MessageSender.sendPluginDisableMessage();
    }

    // Helper methods
    private void init()
    {
        this.saveDefaultConfig();
        ConfigLoader.loadProtections();
        TPMessages.loadMessages();

        Bukkit.getPluginManager().registerEvents(new ListenerClass(), this);

        if (ConfigLoader.doesAutoSaveEnabled())
        {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AutoSave(), ConfigLoader.getAutoSaveInterval() * 20L, ConfigLoader.getAutoSaveInterval() * 20L);
        }

        if (CommodoreProvider.isSupported())
        {
            Commodore commodore = CommodoreProvider.getCommodore(this);
            log("Brigadier's argument manager is SUPPORTED! Registering argument map...", Level.INFO);

            BrigadierLoader loader = new BrigadierLoader();
            try
            {
                PluginCommand mainCommand = this.getCommand("tryprotect");
                Objects.requireNonNull(mainCommand).setExecutor(new MainCommand());
                loader.registerArguments(commodore, mainCommand);

                log("Brigadier command map loaded!", Level.INFO);
            } catch (IOException e)
            {
                log("Could not lod Brigadier literal command map.", Level.SEVERE);
                e.printStackTrace();
            }
        } else
        {
            log("Brigadier's argument manager is NOT SUPPORTED! Skipping argument registration...", Level.WARNING);
        }
    }
}
