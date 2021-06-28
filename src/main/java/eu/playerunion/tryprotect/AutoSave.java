package eu.playerunion.tryprotect;

import eu.playerunion.tryprotect.config.ConfigLoader;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoSave implements Runnable
{
    private final Logger autoSaveLogger = Logger.getLogger("TProtection - AutoSave");

    @Override
    public void run()
    {
        this.log("Auto saving configurations and protections.");
        ConfigLoader.saveProtections();
        this.log("Save completed!");
    }

    private void log(String message)
    {
        this.autoSaveLogger.log(Level.INFO, message);
    }
}
