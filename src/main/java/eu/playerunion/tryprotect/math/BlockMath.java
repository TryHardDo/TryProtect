package eu.playerunion.tryprotect.math;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class BlockMath {
    public static Location[] calculateCorners(@NotNull final Location signLoc, int x, int y, int z) {
        Vector protectionSize = new Vector((x/2), (y/2), (z/2));

        if (signLoc.getWorld() == null) {
            return null;
        }

        Location topCornerLocation = signLoc.toVector().add(protectionSize).toLocation(signLoc.getWorld());
        Location bottomCornerLocation = signLoc.toVector().subtract(protectionSize).toLocation(signLoc.getWorld());

        return new Location[]{topCornerLocation, bottomCornerLocation};
    }
}
