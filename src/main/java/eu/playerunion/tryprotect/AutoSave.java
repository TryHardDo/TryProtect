package eu.playerunion.tryprotect;

import eu.playerunion.tryprotect.config.ConfigLoader;
import eu.playerunion.tryprotect.utils.ProtectionUtils;

import java.util.logging.Logger;

public class AutoSave implements Runnable
{
    private final Logger autoSaveLogger = Logger.getLogger("TProtection - AutoSave");

    @Override
    public void run()
    {
        this.autoSaveLogger.info("Saving and validating protections.");

        if (ConfigLoader.validateBeforeSave())
        {
            ProtectionUtils.validateCache(autoSaveLogger);
        }

        ConfigLoader.saveProtections();
        this.autoSaveLogger.info("Validation completed! Everything is saved...");
    }
}
