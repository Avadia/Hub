package net.samagames.hub.interactions.sonicsquid;

import net.minecraft.server.v1_12_R1.WorldServer;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.interactions.AbstractInteraction;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
class SonicSquid extends AbstractInteraction implements Listener {
    private final Map<UUID, EntitySonicSquid> squids;
    private final BukkitTask checkTask;

    SonicSquid(Hub hub) {
        super(hub);

        this.hub.getServer().getPluginManager().registerEvents(this, this.hub);

        this.squids = new ConcurrentHashMap<>();

        this.checkTask = this.hub.getServer().getScheduler().runTaskTimer(this.hub, () -> this.squids.keySet().stream().filter(uuid -> !this.squids.get(uuid).isAlive()).forEach(this.squids::remove), 20L, 20L);
    }

    @Override
    public void onDisable() {
        this.checkTask.cancel();

        this.squids.values().forEach(EntitySonicSquid::die);
        this.squids.clear();

        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.hasPlayer(event.getPlayer()))
            return;

        if (event.getPlayer().getLocation().getBlock().getType() != Material.WATER && event.getPlayer().getLocation().getBlock().getType() != Material.STATIONARY_WATER)
            return;

        if (this.hub.getPlayerManager().isBusy(event.getPlayer()))
            return;

        if (PlayerManager.VIP_ZONE.isInArea(event.getPlayer().getLocation()))
            return;

        this.play(event.getPlayer());
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER)
            return;

        if (!this.hasPlayer((Player) event.getEntity()))
            return;

        stop((Player) event.getEntity());
    }

    @Override
    public void play(Player player) {
        if (!hasPlayer(player) && player.getGameMode() != GameMode.SPECTATOR) {
            WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
            EntitySonicSquid sonicSquidEntity = new EntitySonicSquid(world, player);
            world.addEntity(sonicSquidEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);

            sonicSquidEntity.getBukkitEntity().setPassenger(player);

            this.squids.put(player.getUniqueId(), sonicSquidEntity);
        }
    }

    @Override
    public void stop(Player player) {
        if (hasPlayer(player)) {
            this.squids.get(player.getUniqueId()).die();
            this.squids.remove(player.getUniqueId());
        }
    }

    @Override
    public boolean hasPlayer(Player player) {
        return this.squids.containsKey(player.getUniqueId());
    }
}
