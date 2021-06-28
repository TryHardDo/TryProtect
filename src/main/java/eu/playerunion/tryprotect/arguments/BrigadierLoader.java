package eu.playerunion.tryprotect.arguments;

import com.mojang.brigadier.tree.LiteralCommandNode;
import eu.playerunion.tryprotect.TryProtect;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.file.CommodoreFileFormat;
import org.bukkit.command.PluginCommand;

import java.io.IOException;

public class BrigadierLoader {
    public void registerArguments(Commodore commodore, PluginCommand command) throws IOException {
        LiteralCommandNode<?> mainCommand = CommodoreFileFormat.parse(TryProtect.getInstance().getResource("main.commodore"));
        commodore.register(command, mainCommand);
    }
}
