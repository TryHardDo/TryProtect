package eu.playerunion.tryprotect.ui;

import com.google.common.annotations.Beta;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Beta
public class InventoryWrapper
{
    private final Inventory inventory;
    private final InventoryType type;
    private final String name;
    private final UUID invId;

    public InventoryWrapper(@NotNull final String name, @NotNull final InventoryType type)
    {
        this.inventory = Bukkit.createInventory(null, type, name);
        this.type = type;
        this.name = name;
        this.invId = UUID.randomUUID();
    }

    public InventoryWrapper(final int size, @NotNull final String name)
    {
        this.inventory = Bukkit.createInventory(null, size, name);
        this.type = null;
        this.name = name;
        this.invId = UUID.randomUUID();
    }

    @Nullable
    public InventoryType getType()
    {
        return type;
    }

    @NotNull
    public Inventory getInventory()
    {
        return inventory;
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @NotNull
    public UUID getInvId()
    {
        return invId;
    }
}
