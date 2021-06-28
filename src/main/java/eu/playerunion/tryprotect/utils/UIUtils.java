package eu.playerunion.tryprotect.utils;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class UIUtils
{
    public static String getFormattedOwnersList(@NotNull final ProtectedRegion region)
    {
        if (region.hasMembersOrOwners() && region.getOwners().getUniqueIds().size() > 0)
        {
            StringBuilder builder = new StringBuilder();

            String SEPARATOR = "";
            for (UUID player : region.getOwners().getUniqueIds())
            {
                Player user = Bukkit.getPlayer(player);

                if (user == null)
                {
                    continue;
                }

                builder.append("§f").append(SEPARATOR).append("§6").append(user.getName());
                SEPARATOR = ", ";
            }

            return builder.toString();
        }
        return "Nincs tulajdonos!";
    }

    public static String getFormattedMembersList(@NotNull final ProtectedRegion region)
    {
        if (region.hasMembersOrOwners() && region.getMembers().getUniqueIds().size() > 0)
        {
            StringBuilder builder = new StringBuilder();

            String SEPARATOR = "";
            for (UUID player : region.getMembers().getUniqueIds())
            {
                Player user = Bukkit.getPlayer(player);

                if (user == null)
                {
                    continue;
                }

                builder.append("§f").append(SEPARATOR).append("§6").append(user.getName());
                SEPARATOR = ", ";
            }

            return builder.toString();
        }
        return "Nincsenek tagok!";
    }

    public static String getFormattedFlagsList(@NotNull final ProtectedRegion region)
    {
        if (region.getFlags().size() > 0)
        {
            StringBuilder builder = new StringBuilder();

            String SEPARATOR = "";
            for (Map.Entry<Flag<?>, Object> flag : region.getFlags().entrySet())
            {
                builder.append(SEPARATOR).append("§6").append(flag.getKey().getName()).append(": §7§o").append(flag.getValue().toString());
                SEPARATOR = ", ";
            }

            return builder.toString();
        }
        return "Nincsenek flagek!";
    }
}
