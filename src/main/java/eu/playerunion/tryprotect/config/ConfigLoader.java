package eu.playerunion.tryprotect.config;

import eu.playerunion.tryprotect.TryProtect;
import eu.playerunion.tryprotect.utils.IOUtils;
import org.bukkit.ChatColor;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigLoader
{
    private static final File protections = new File(TryProtect.getInstance().getDataFolder(), "protections.json");

    public static String getPrefix()
    {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(TryProtect.getInstance().getConfig().getString("prefix")));
    }

    public static void reloadConfig()
    {
        TryProtect.getInstance().reloadConfig();
    }

    public static void saveProtections()
    {
        TryProtect.log("Saving protections.yml...", Level.INFO);
        JSONObject clear = new JSONObject();

        IOUtils.writeJSONFile(clear, protections);

        if (TryProtect.getProtectionObjectHashMap().size() == 0)
        {
            TryProtect.log("Nothing have to save. Skipping...", Level.INFO);
            return;
        }

        JSONObject cache = new JSONObject(TryProtect.getProtectionObjectHashMap());

        IOUtils.writeJSONFile(cache, protections);

        TryProtect.log("Saved protections.yml!", Level.INFO);
    }

    public static void loadProtections()
    {
        if (TryProtect.getProtectionObjectHashMap().size() > 0)
        {
            TryProtect.log("Protection cache is not empty... Clearing cache.", Level.INFO);
            TryProtect.getProtectionObjectHashMap().clear();
        }

        TryProtect.log("Loading protections from disk...", Level.INFO);

        if (protections.length() == 0)
        {
            IOUtils.writeJSONFile(new JSONObject(), protections);
        }

        JSONObject loadedProtections = IOUtils.readJSONFile(protections);

        if (loadedProtections.length() == 0)
        {
            TryProtect.log("Protections.json is empty. Skipping loading...", Level.INFO);
            return;
        }

        HashMap<String, String> cache = new HashMap<>();

        Iterator<String> iter = loadedProtections.keys();

        while (iter.hasNext())
        {
            String key = iter.next();

            try
            {
                String value = (String) loadedProtections.get(key);
                cache.put(key, value);
            } catch (ClassCastException e)
            {
                TryProtect.log("There was an error while tried to load protections.json", Level.SEVERE);
                e.printStackTrace();
            }

        }

        TryProtect.getProtectionObjectHashMap().putAll(cache);
        TryProtect.log("Loaded protections.json!", Level.INFO);
    }
}
