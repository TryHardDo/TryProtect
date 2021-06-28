package eu.playerunion.tryprotect.protection;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import eu.playerunion.tryprotect.TryProtect;
import org.jetbrains.annotations.NotNull;


public class TPQuery
{
    private ProtectedRegion selectedRegion;
    private RegionManager manager;

    private String authorizedId = null;

    public TPQuery(@NotNull final String pId) throws Exception
    {
        RegionContainer container = TryProtect.getWorldGuard().getPlatform().getRegionContainer();

        if (container == null)
        {
            throw new NullPointerException("Could not get region container.");
        }

        if (TryProtect.getProtectionObjectHashMap().containsKey(pId))
        {
            String wgId = TryProtect.getProtectionObjectHashMap().get(pId);

            container.getLoaded().forEach(regionManager -> {
                if (regionManager.hasRegion(wgId))
                {
                    authorizedId = wgId;
                    manager = regionManager;
                    selectedRegion = regionManager.getRegion(wgId);
                }
            });

        } else if (TryProtect.getProtectionObjectHashMap().containsValue(pId))
        {
            authorizedId = pId;
            container.getLoaded().forEach(regionManager -> {
                if (regionManager.hasRegion(pId))
                {
                    authorizedId = pId;
                    manager = regionManager;
                    selectedRegion = regionManager.getRegion(pId);
                }
            });
        } else
        {
            try
            {
                throw new Exception("Could not find region with this id: " + pId);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @NotNull
    public RegionManager getManager()
    {
        return manager;
    }

    @NotNull
    public String getAuthorizedId()
    {
        return authorizedId;
    }

    @NotNull
    public ProtectedRegion getRegion()
    {
        return selectedRegion;
    }
}
