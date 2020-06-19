package net.samagames.hub.cosmetics.gadgets.displayers;

import de.slikey.effectlib.effect.HelixEffect;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.tools.ProximityUtils;
import net.samagames.tools.SimpleBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

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
public class StargateDisplayer extends AbstractDisplayer {
    private final Location basePortalLocation;
    private final Location exitPortalLocation;
    private final Map<Location, SimpleBlock> portals;
    private HelixEffect helixEffect;
    private BukkitTask portalTask;

    public StargateDisplayer(Hub hub, Player player) {
        super(hub, player);

        this.basePortalLocation = this.baseLocation.add(3.0D, 0.0D, 0.0D).getBlock().getLocation();
        this.addBlocksToUse(this.createPortalFrame(this.basePortalLocation, false));

        this.portals = this.createPortal(this.basePortalLocation);
        this.addBlocksToUse(this.portals);

        this.exitPortalLocation = this.baseLocation.add((double) GadgetManager.RANDOM.nextInt(120) - 60, 0, (double) GadgetManager.RANDOM.nextInt(120) - 60);
        this.exitPortalLocation.setY(250);
        this.addBlocksToUse(this.createPortalFrame(this.exitPortalLocation, true));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void display() {
        for (Location block : this.blocksUsed.keySet()) {
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }

        Location newPlayerLocation = this.player.getLocation();
        newPlayerLocation.setYaw(-90.0F);
        newPlayerLocation.setPitch(0.0F);

        this.player.teleport(newPlayerLocation);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            this.basePortalLocation.getWorld().playSound(this.basePortalLocation, Sound.ENTITY_ENDERMEN_SCREAM, 1.0F, 6.0F);
            this.basePortalLocation.getWorld().createExplosion(basePortalLocation.getX(), basePortalLocation.getY(), basePortalLocation.getZ(), 10, false, false);

            for (Location block : this.portals.keySet()) {
                block.getBlock().setType(Material.PORTAL);
                block.getBlock().setData((byte) 2);
            }

            this.helixEffect = new HelixEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
            //this.helixEffect.particle = ParticleEffect.FIREWORKS_SPARK;
            this.helixEffect.radius = 10;
            this.helixEffect.setLocation(this.basePortalLocation.clone().add(0.5D, 0.25D, 1.5D));
            this.helixEffect.infinite();
            this.helixEffect.start();

            this.portalTask = this.hub.getServer().getScheduler().runTaskTimer(this.hub, () ->
            {
                Location blackHoleLocation = this.basePortalLocation.clone().add(0.5D, 2.0D, 0.5D);

                for (Entity entity : ProximityUtils.getNearbyEntities(blackHoleLocation, 12, EntityType.PLAYER)) {
                    Player player = (Player) entity;

                    if (this.hub.getPlayerManager().isBusy(player))
                        continue;

                    Vector entityVector = new Vector(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
                    Vector blackholeVector = new Vector(blackHoleLocation.getX(), blackHoleLocation.getY(), blackHoleLocation.getZ());
                    player.setVelocity(blackholeVector.subtract(entityVector).normalize().multiply(0.25F));

                    if (player.getLocation().distanceSquared(blackHoleLocation) <= 1.0D) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);

                        this.hub.getServer().getScheduler().runTaskLater(this.hub, () -> player.teleport(this.exitPortalLocation), 5L);
                    }
                }
            }, 1L, 1L);
        }, 20L * 5);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            this.basePortalLocation.getWorld().playSound(this.basePortalLocation, Sound.ENTITY_ENDERMEN_TELEPORT, 1.0F, 6.0F);
            this.basePortalLocation.getWorld().createExplosion(this.basePortalLocation.getX(), this.basePortalLocation.getY(), this.basePortalLocation.getZ(), 8, false, false);
            this.exitPortalLocation.getWorld().createExplosion(this.exitPortalLocation.getX(), this.exitPortalLocation.getY(), this.exitPortalLocation.getZ(), 8, false, false);

            this.end();
            this.restore();
            this.portalTask.cancel();
            this.helixEffect.cancel();
        }, 20L * 25);
    }

    @Override
    public void handleInteraction(Entity who, Entity with) {
    }

    private Map<Location, SimpleBlock> createPortalFrame(Location basePortalLocation, boolean exit) {
        Map<Location, SimpleBlock> blocks = new HashMap<>();

        if (exit) {
            blocks.put(basePortalLocation, new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(3.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(4.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(5.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(6.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().add(6.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().add(6.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().add(5.0D, 0.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().add(4.0D, 0.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().add(3.0D, 0.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().add(2.0D, 0.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().add(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
            blocks.put(basePortalLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, 3));
        } else {
            blocks.put(basePortalLocation, new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 1.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(0.0D, 2.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(0.0D, 3.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(0.0D, 4.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 5.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 6.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().add(0.0D, 6.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().add(0.0D, 6.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().add(0.0D, 5.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().add(0.0D, 4.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().add(0.0D, 3.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().add(0.0D, 2.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().add(0.0D, 1.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, 1));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().add(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        }

        return blocks;
    }

    private Map<Location, SimpleBlock> createPortal(Location basePortalLocation) {
        Map<Location, SimpleBlock> blocks = new HashMap<>();

        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 1.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 1.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 1.0D, 1.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 2.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 2.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 2.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 2.0D, 1.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 2.0D, 2.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 3.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 3.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 3.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 3.0D, 1.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 3.0D, 2.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 4.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 4.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 4.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 4.0D, 1.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 4.0D, 2.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 5.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 5.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 5.0D, 1.0D), new SimpleBlock(Material.AIR, 1));

        return blocks;
    }

    @Override
    public boolean isInteractionsEnabled() {
        return false;
    }

    @Override
    public boolean canUse() {
        return true;
    }
}
