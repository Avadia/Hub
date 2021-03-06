package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.tools.SimpleBlock;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

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
public class MagicCakeDisplayer extends AbstractDisplayer {
    private final Location centerLoc;
    private BukkitTask loopTask;

    public MagicCakeDisplayer(Hub hub, Player player) {
        super(hub, player);

        this.baseLocation = player.getLocation().getBlock().getLocation().clone().add(0.0D, 1.0D, 0.0D);
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, -1.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 2));
        this.addBlockToUse(this.baseLocation, new SimpleBlock(Material.CAKE_BLOCK));

        this.centerLoc = this.baseLocation.clone().add(0.5D, 0.5D, 0.5D);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void display() {
        for (Location block : this.blocksUsed.keySet()) {
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }

        FireworkEffect effect = FireworkEffect.builder().flicker(false).trail(false).with(FireworkEffect.Type.BURST).withColor(Color.RED).withFade(Color.PURPLE).withFlicker().build();

        for (int i = 0; i <= 2; i++)
            FireworkUtils.launchfw(this.hub, this.centerLoc, effect);

        this.getNearbyPlayers(this.centerLoc, 5).stream().filter(entity -> entity instanceof Player).forEach(entity -> entity.setVelocity(new Vector(0, 2, 0)));

        this.loopTask = this.hub.getServer().getScheduler().runTaskTimer(this.hub, new Runnable() {
            int times = 0;

            @Override
            public void run() {
                for (int i = 0; i <= 5; i++)
                    //noinspection deprecation
                    centerLoc.getWorld().playEffect(centerLoc.clone().add((GadgetManager.RANDOM.nextDouble() * 5) - 2.5, (GadgetManager.RANDOM.nextDouble() * 4) - 1, (GadgetManager.RANDOM.nextDouble() * 5) - 2.5), Effect.FLYING_GLYPH, 5);

                for (Entity entity : getNearbyPlayers(centerLoc, 5)) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;

                        double pw = 8;
                        double xa = centerLoc.getX();
                        double ya = centerLoc.getZ();
                        double xb = player.getLocation().getX();
                        double yb = player.getLocation().getZ();
                        double m = (ya - yb) / (xa - xb);
                        double p = ya - m * xa;
                        double alpha = Math.atan(m * xa + p);
                        double xc1 = xb + pw * Math.cos(alpha);
                        double xc2 = xb - pw * Math.cos(alpha);
                        double yc;
                        double xc;

                        if (Math.abs(xa - xc1) > Math.abs(xa - xc2)) {
                            yc = m * xc1 + p;
                            xc = xc1;
                        } else {
                            yc = m * xc2 + p;
                            xc = xc2;
                        }

                        double a = xc - player.getLocation().getX();
                        double b = yc - player.getLocation().getZ();

                        if (b > 10)
                            b = 10;
                        else if (b < -10)
                            b = -10;

                        if (a != 0.0D && b != 0.0D) {
                            Vector v = new Vector(a / 10, (player.getLocation().getY() - centerLoc.getY()) / 10 + 1, b / 10);
                            player.setVelocity(v);
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1F, 2F);

                            int effect = GadgetManager.RANDOM.nextInt(3);

                            switch (effect) {
                                case 0:
                                    break;
                                case 1:
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 0));
                                    break;
                                case 2:
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 0));
                                    break;
                                case 3:
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 120, 4));
                                    break;
                            }
                        }
                    }
                }

                if (this.times == 10 || this.times == 20 || this.times == 30 || this.times == 40 || this.times == 50 || this.times == 60) {
                    baseLocation.getWorld().getBlockAt(baseLocation).setData((byte) (baseLocation.getWorld().getBlockAt(baseLocation).getData() + 1));
                    centerLoc.getWorld().playSound(centerLoc, Sound.ENTITY_PLAYER_BURP, 2, 1);
                } else if (this.times == 70) {
                    baseLocation.getWorld().getBlockAt(baseLocation).setType(Material.AIR);
                    baseLocation.getWorld().playSound(baseLocation, Sound.ENTITY_PLAYER_BURP, 2, 1);
                } else if (this.times >= 80) {
                    FireworkEffect.Builder builder = FireworkEffect.builder();
                    FireworkEffect effect = builder.flicker(false).trail(false).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.BLUE).withFade(Color.PURPLE).withFlicker().build();
                    FireworkUtils.launchfw(hub, baseLocation, effect);

                    restore();
                    end();
                    callback();
                }

                this.times++;
            }
        }, 5L, 5L);
    }

    @Override
    public void handleInteraction(Entity who, Entity with) {
    }

    @SuppressWarnings("SameParameterValue")
    private List<Entity> getNearbyPlayers(Location where, int range) {
        return where.getWorld().getEntities().stream().filter(entity -> this.distance(where, entity.getLocation()) <= range && entity instanceof Player).filter(p -> p.getUniqueId().equals(this.player.getUniqueId()) || this.canInteractWith((Player) p)).collect(Collectors.toList());
    }

    private double distance(Location loc1, Location loc2) {
        return Math.sqrt((loc1.getX() - loc2.getX()) * (loc1.getX() - loc2.getX()) + (loc1.getY() - loc2.getY()) * (loc1.getY() - loc2.getY()) + (loc1.getZ() - loc2.getZ()) * (loc1.getZ() - loc2.getZ()));
    }

    @Override
    public boolean isInteractionsEnabled() {
        return false;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    private void callback() {
        this.loopTask.cancel();
    }
}
