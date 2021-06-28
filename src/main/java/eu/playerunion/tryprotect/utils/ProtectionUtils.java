package eu.playerunion.tryprotect.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import eu.playerunion.tryprotect.TryProtect;
import eu.playerunion.tryprotect.config.TPMessages;
import eu.playerunion.tryprotect.math.BlockMath;
import eu.playerunion.tryprotect.protection.TPQuery;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class ProtectionUtils
{
    public static String generateProtectionId()
    {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static boolean isProtectionSign(@NotNull final Sign sign)
    {
        String[] lines = sign.getLines();
        return lines[0].contains("[Levédés]") &&
                TryProtect.getProtectionObjectHashMap().containsKey(lines[2].replaceAll("&", ""));
    }

    public static void removeProtection(@NotNull final Player breaker, @NotNull final String protectionId)
    {
        try
        {
            TPQuery query = new TPQuery(protectionId);
            query.getManager().removeRegion(query.getRegion().getId());
            TryProtect.getProtectionObjectHashMap().remove(protectionId);

            breaker.playSound(breaker.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);

            breaker.sendMessage(TPMessages.REMOVE_SUCCESS.msg().replaceAll("%id%", protectionId));
        } catch (Exception exception)
        {
            TryProtect.log("Could not remove protection ID: " + protectionId, Level.SEVERE);
            exception.printStackTrace();
        }
    }

    public static boolean createProtection(@NotNull Player owner, @NotNull String protectionId, @NotNull final Location signLoc, final int sizeX, final int sizeY, final int sizeZ)
    {
        if (signLoc.getWorld() == null)
        {
            return false;
        }

        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(signLoc.getWorld()));
        Location[] points = BlockMath.calculateCorners(signLoc, sizeX, sizeY, sizeZ);

        if (points == null)
        {
            return false;
        }

        ProtectedRegion newProtection = new ProtectedCuboidRegion(protectionId, BukkitAdapter.asBlockVector(points[0]), BukkitAdapter.asBlockVector(points[1]));

        if (manager != null)
        {
            if (newProtection.getIntersectingRegions(manager.getRegions().values()).size() > 0)
            {
                owner.sendMessage(TPMessages.PROTECTION_OVERLAP.msg());
                return false;
            }

            newProtection.getOwners().addPlayer(owner.getUniqueId());
            newProtection.setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
            newProtection.setFlag(Flags.PVP, StateFlag.State.DENY);
            newProtection.setFlag(Flags.GREET_MESSAGE, "§fBeléptél §6§l" + owner.getName() + " §flevédett területére.");
            newProtection.setFlag(Flags.FAREWELL_MESSAGE, "§fElhagytad §6§l" + owner.getName() + " §flevédett területét.");

            manager.addRegion(newProtection);
            TryProtect.getProtectionObjectHashMap().put(protectionId, newProtection.getId());
            owner.sendMessage(TPMessages.PROTECTION_CREATED.msg());
        }
        return true;
    }

    public static boolean isOwnProtection(@NotNull final Player player, @NotNull final String protectionID, @NotNull final Location signLocation) throws IllegalStateException
    {
        if (!TryProtect.getProtectionObjectHashMap().containsKey(protectionID) || signLocation.getWorld() == null)
        {
            throw new IllegalStateException("Could not get protection cache or the provided world is NULL.");
        }

        World wrappedWorld = BukkitAdapter.adapt(signLocation.getWorld());
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(wrappedWorld);

        if (manager == null)
        {
            return false;
        }

        String cachedProtectionId = TryProtect.getProtectionObjectHashMap().get(protectionID);

        if (!manager.hasRegion(cachedProtectionId))
        {
            return false;
        }

        ProtectedRegion region = manager.getRegion(cachedProtectionId);

        if (region == null)
        {
            return false;
        }

        LocalPlayer worldGuardWrapped = WorldGuardPlugin.inst().wrapPlayer(player);
        BlockVector3 wrappedVector = BukkitAdapter.asBlockVector(signLocation);

        if (!region.contains(wrappedVector))
        {
            TryProtect.getProtectionObjectHashMap().remove(protectionID);
            manager.removeRegion(region.getId());

            throw new IllegalStateException("Protection sign doesn't part of its own protected zone at: " + signLocation + " Protection marked as INVALID. Removing...");
        }

        return region.isOwner(worldGuardWrapped);
    }
}
