package net.samagames.hub.events;

import net.samagames.hub.Hub;
import net.samagames.hub.parkours.Parkour;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
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
public class ParkourListener implements Listener {
    private final Hub hub;
    private final List<UUID> cooldown;

    public ParkourListener(Hub hub) {
        this.hub = hub;
        this.cooldown = new ArrayList<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.PHYSICAL)) {
            if (event.getClickedBlock().getType().equals(Material.GOLD_PLATE) || event.getClickedBlock().getType().equals(Material.IRON_PLATE)) {
                Parkour parkour = this.hub.getParkourManager().getPlayerParkour(event.getPlayer().getUniqueId());

                if (parkour != null) {
                    Location location = event.getClickedBlock().getLocation();
                    location.getWorld().getNearbyEntities(location, 1.0D, 3.0D, 1.0D).forEach(entity -> {
                        if (entity.getType().equals(EntityType.ARMOR_STAND) && entity.isCustomNameVisible() && entity.getCustomName().contains("Checkpoint"))
                            parkour.checkpoint(event.getPlayer(), entity.getLocation().clone().add(0.0D, 1.0D, 0.0D));
                    });
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            Parkour parkour = this.hub.getParkourManager().getPlayerParkour(event.getPlayer().getUniqueId());

            if (parkour != null)
                parkour.removePlayer(event.getPlayer());
        });
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.cooldown.contains(event.getPlayer().getUniqueId()))
            return;

        this.cooldown.add(event.getPlayer().getUniqueId());

        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            this.cooldown.remove(event.getPlayer().getUniqueId());
            Parkour parkour = this.hub.getParkourManager().getPlayerParkour(event.getPlayer().getUniqueId());

            if (parkour != null && event.getTo().getBlockY() <= parkour.getMinimalHeight())
                parkour.failPlayer(event.getPlayer());
        });
    }
}
