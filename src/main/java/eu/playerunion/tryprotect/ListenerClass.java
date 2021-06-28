package eu.playerunion.tryprotect;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import eu.playerunion.tryprotect.config.TPMessages;
import eu.playerunion.tryprotect.protection.TPQuery;
import eu.playerunion.tryprotect.utils.ProtectionUtils;
import eu.playerunion.tryprotect.utils.UIUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;

public class ListenerClass implements Listener {

    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent e) {
        Player joiner = e.getPlayer();

        if (joiner.isOp() || joiner.hasPermission("tprotect.receivejoininfo")) {
            MessageSender.sendPlayerJoinEventMessage(e.getPlayer());
        }
    }

    // Ellenőrzi hogy levédőtáblát ütnek e ki, illetve ha igen van e joga az illetőnek ehhez.
    @EventHandler
    public void onBlockBreak(@NotNull final BlockBreakEvent e) {
        if (!(e.getBlock().getState() instanceof Sign)) {
            return;
        }

        Sign brokenSign = (Sign) e.getBlock().getState();

        if (!ProtectionUtils.isProtectionSign(brokenSign)) {
            return;
        }

        Player breaker = e.getPlayer();

        if (breaker.isOp() || breaker.hasPermission("tprotect.breaksign.others") ||
                ProtectionUtils.isOwnProtection(breaker, brokenSign.getLine(2), brokenSign.getLocation())) {

            String protectionId = brokenSign.getLine(2);

            ProtectionUtils.removeProtection(breaker, protectionId);
            return;
        }

        breaker.sendMessage(TPMessages.NO_PERM_BREAK_SIGN.msg());
        e.setCancelled(true);
    }

    @EventHandler
    public void onSignGrief(@NotNull final BlockBreakEvent e) {
        Block brokenBlock = e.getBlock();
        List<BlockFace> sniffSides = new ArrayList<>(Arrays.asList(BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST));

        for (BlockFace face : sniffSides) {
            Block relativeBlock = brokenBlock.getRelative(face);

            if (!(brokenBlock.getRelative(face).getBlockData() instanceof Directional)) {
                if (face.equals(BlockFace.UP)) {
                    if (!relativeBlock.getType().name().contains("SIGN")) {
                        continue;
                    }

                    if (ProtectionUtils.isProtectionSign(((Sign) relativeBlock.getState()))) {
                        e.setCancelled(true);
                        return;
                    }
                }
                continue;
            }
            Directional directional = ((Directional) relativeBlock.getBlockData());

            if (!relativeBlock.getType().name().contains("SIGN")) {
                continue;
            }

            if (directional.getFacing().equals(face)) {
                if (ProtectionUtils.isProtectionSign(((Sign) relativeBlock.getState()))) {

                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(@NotNull final PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock() == null) {
            return;
        }

        if (!(e.getClickedBlock().getState() instanceof Sign)) {
            return;
        }

        Sign clickedSign = (Sign) e.getClickedBlock().getState();

        String[] clickedSignLines = clickedSign.getLines();

        if (!ProtectionUtils.isProtectionSign(clickedSign)) {
            return;
        }

        try {
            TPQuery query = new TPQuery(clickedSignLines[2]);
            ProtectedRegion queried = query.getRegion();

            String[] info = new String[] {
                    "§6#############################################",
                    "§6● §fLevédés: §6§l" + queried.getId(),
                    "§6#############################################",
                    "",
                    "§6▲▼ §fLevédési pont (§61§f): §c§l" + queried.getMinimumPoint().toString(),
                    "§6▲▼ §fLevédési pont (§62§f): §c§l" + queried.getMaximumPoint().toString(),
                    "§6● §fTulajdonosok: " + UIUtils.getFormattedOwnersList(queried),
                    "§6● §fTagok: " + UIUtils.getFormattedMembersList(queried),
                    "§6● §fFlagek: " + UIUtils.getFormattedFlagsList(queried)
            };

            e.getPlayer().sendMessage(info);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void onSignChange(@NotNull final SignChangeEvent e) {
        String[] lines = e.getLines();
        if (lines[0].isEmpty()) {
            return;
        }

        if (!lines[0].equalsIgnoreCase("[tprotect]")) {
            return;
        }

        Player changer = e.getPlayer();

        if (!changer.hasPermission("tprotect.create")) {
            changer.sendMessage(TPMessages.NO_PERM_TO_CREATE_ZONE.msg());
            return;
        }

        String size = lines[1];

        if (size.isEmpty()) {
            changer.sendMessage(TPMessages.MISSING_SIZE.msg());
            return;
        }

        String[] splitter = size.split(";");

        if (splitter.length != 3) {
            changer.sendMessage(TPMessages.INVALID_SIZE_FORMAT.msg());
            return;
        }

        int x = Integer.parseInt(splitter[0]);
        int y = Integer.parseInt(splitter[1]);
        int z = Integer.parseInt(splitter[2]);

        if (x < 10 || y < 10 || z < 2) {
            changer.sendMessage(TPMessages.MIN_SIZE_NOT_PROVIDED.msg());
            return;
        }

        String protectionId = ProtectionUtils.generateProtectionId();
        Location protectionSignLocation = e.getBlock().getLocation();

        // BETA
        if (!ProtectionUtils.createProtection(changer, protectionId, protectionSignLocation, x, y, z)) {
            changer.sendMessage(TPMessages.PROTECTION_NOT_CREATED.msg());
            return;
        }

        changer.playSound(changer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);

        e.setLine(0, ChatColor.GOLD + "-> [Levédés] <-");
        e.setLine(1, ChatColor.BOLD + changer.getName());
        e.setLine(2, protectionId);
        e.setLine(3, "Katt rám!");
    }
}
