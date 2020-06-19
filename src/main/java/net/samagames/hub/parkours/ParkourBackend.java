package net.samagames.hub.parkours;

import net.samagames.hub.Hub;
import net.samagames.hub.parkours.types.WhitelistBasedParkour;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
class ParkourBackend {
    private BukkitTask checkingTask;
    private BukkitTask nearingTask;

    ParkourBackend(Hub hub, Parkour parkour) {
        this.startCheckingTask(hub, parkour);
        this.startNearingTask(hub, parkour);
    }

    void stop() {
        this.checkingTask.cancel();
        this.nearingTask.cancel();
    }

    private void startCheckingTask(Hub hub, Parkour parkour) {
        this.checkingTask = hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, () ->
        {
            for (UUID uuid : parkour.getPlayersIn().keySet()) {
                Player player = hub.getServer().getPlayer(uuid);

                if (player == null || !player.isOnline()) {
                    parkour.removePlayer(Objects.requireNonNull(player));
                } else {
                    Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

                    if (parkour instanceof WhitelistBasedParkour && !((WhitelistBasedParkour) parkour).isWhitelisted(block.getType()) && block.getType().isSolid())
                        parkour.failPlayer(player);
                }
            }
        }, 20L, 20L);
    }

    private void startNearingTask(Hub hub, Parkour parkour) {
        this.nearingTask = hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, new Runnable() {
            private final List<UUID> playersCooldown = new ArrayList<>();

            @Override
            public void run() {
                parkour.getStartTooltip().getNearbyEntities(0.5D, 0.5D, 0.5D).stream().filter(entity -> entity.getType() == EntityType.PLAYER).forEach(player ->
                {
                    if (!parkour.isParkouring(player.getUniqueId()) && !hub.getPlayerManager().isBusy((Player) player))
                        parkour.addPlayer((Player) player);
                });

                parkour.getStartTooltip().getNearbyEntities(8.0D, 8.0D, 8.0D).stream().filter(entity -> entity.getType() == EntityType.PLAYER).filter(player -> !parkour.isParkouring(player.getUniqueId())).filter(player -> !this.playersCooldown.contains(player.getUniqueId())).forEach(player ->
                {
                    player.sendMessage(parkour.getTag() + ChatColor.WHITE + "Vous approchez " + parkour.getPrefix() + " " + ChatColor.AQUA + parkour.getParkourName() + ChatColor.WHITE + "...");
                    player.sendMessage(parkour.getTag() + ChatColor.WHITE + "Difficulté : " + parkour.getStars());

                    this.playersCooldown.add(player.getUniqueId());

                    hub.getServer().getScheduler().runTaskLaterAsynchronously(hub, () -> this.playersCooldown.remove(player.getUniqueId()), 20L * 20);
                });

                parkour.getEndTooltip().getNearbyEntities(0.5D, 0.5D, 0.5D).stream().filter(entity -> entity.getType() == EntityType.PLAYER).forEach(player ->
                {
                    if (parkour.isParkouring(player.getUniqueId()))
                        parkour.winPlayer((Player) player);
                });
            }
        }, 2L, 2L);
    }
}
