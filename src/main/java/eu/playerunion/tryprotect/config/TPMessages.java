package eu.playerunion.tryprotect.config;

import eu.playerunion.tryprotect.TryProtect;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;

public enum TPMessages {
    NO_PERM_GENERAL("NO_PERM_GENERAL", ChatColor.RED + "Ehhez nincs jogod."),
    NO_PERM_TO_CREATE_ZONE("NO_PERM_TO_CREATE_ZONE", ChatColor.RED + "Nincs jogod levédést létrehozni."),
    MISSING_SIZE("MISSING_SIZE", ChatColor.RED + "Hiányzik a méret! " + ChatColor.YELLOW + "X x Y x Z"),
    INVALID_SIZE_FORMAT("INVALID_SIZE_FORMAT", ChatColor.RED + "Érvénytelen méretformátum!"),
    MIN_SIZE_NOT_PROVIDED("MIN_SIZE_NOT_PROVIDED", ChatColor.RED + "A területnek minimum 10x10x2-es méretet meg kell haladnia."),
    PROTECTION_CREATED("PROTECTION_CREATED", ChatColor.GREEN + "Levédés sikeresen létrehozva!"),
    PROTECTION_NOT_CREATED("PROTECTION_NOT_CREATED", ChatColor.RED + "Nem sikerült létrehozni a levédést."),
    RELOAD_SUCCESS("RELOAD_SUCCESS", ChatColor.GREEN + "Sikeres konfiguráció újratöltés."),
    PLAYER_ONLY_COMMAND("PLAYER_ONLY_COMMAND", ChatColor.RED + "Ezt a parancsot csak játékosok használhatják!"),
    NO_PERM_BREAK_SIGN("NO_PERM_BREAK_SIGN", ChatColor.RED + "Nincs jogod kiütni ezt a levédőtáblát."),
    PROTECTION_OVERLAP("PROTECTION_OVERLAP", ChatColor.RED + "A levédeni kívánt terület belelóg egy másik levédésbe."),
    REMOVE_SUCCESS("REMOVE_SUCCESS", ChatColor.GREEN + "Sikeresen törölted a %id% levédést.");

    private static final File messages = new File(TryProtect.getInstance().getDataFolder(), "messages.yml");
    private static final HashMap<String, String> keyToMsg = new LinkedHashMap<>();
    private final String key;
    private final String msg;

    TPMessages(String path, String defaultValue) {
        this.key = path;
        this.msg = defaultValue;
    }

    public static void loadMessages() {
        TryProtect.log("Loading messages.yml file...", Level.INFO);

        keyToMsg.clear();

        YamlConfiguration yml = new YamlConfiguration();

        if (!messages.exists()) {
            try {
                if (messages.createNewFile()) {
                    TryProtect.log("Messages.yml doesn't exists. Creating one...", Level.INFO);
                } else {
                    TryProtect.log("Messages.yml is already exists. Skipping file creation...", Level.INFO);
                }
            } catch (IOException e) {
                TryProtect.log("Could not load messages.yml!", Level.SEVERE);
                e.printStackTrace();
            }
        }

        try {
            yml.load(messages);

            for (TPMessages msgs : TPMessages.values()) {
                if (yml.getString(msgs.key) == null) {
                    yml.set(msgs.key, msgs.msg.replace("§", "&"));
                } else {
                    keyToMsg.put(msgs.key, yml.getString(msgs.key));
                }
            }
            try {
                yml.save(messages);
                TryProtect.log("Messages loaded and ready to use.", Level.INFO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

            for (TPMessages msgs : TPMessages.values()) {
                keyToMsg.put(msgs.key, msgs.msg);
            }
        }

    }

    public String msg() {
        String message = keyToMsg.get(key);
        return ChatColor.translateAlternateColorCodes('&', (message == null) ? msg : message);
    }
}
