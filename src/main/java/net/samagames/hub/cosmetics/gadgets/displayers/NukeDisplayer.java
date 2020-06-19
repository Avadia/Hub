package net.samagames.hub.cosmetics.gadgets.displayers;

import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.SphereEffect;
import de.slikey.effectlib.effect.TornadoEffect;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.utils.FireworkUtils;
import net.samagames.tools.ColorUtils;
import net.samagames.tools.SimpleBlock;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_12_R1.boss.CraftBossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

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
public class NukeDisplayer extends AbstractDisplayer {
    private static final String TAG = ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD;

    private CraftBossBar meowBossBar;
    private BukkitTask loopFirst;
    private BukkitTask loopSecond;

    @SuppressWarnings("deprecation")
    public NukeDisplayer(Hub hub, Player player) {
        super(hub, player);

        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.PURPLE.getWoolData()));
        this.addBlockToUse(this.baseLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.PURPLE.getWoolData()));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone(), new SimpleBlock(Material.PURPUR_BLOCK));
        this.addBlockToUse(this.baseLocation.clone().add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.PURPLE.getWoolData()));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, DyeColor.PURPLE.getWoolData()));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        this.addBlockToUse(this.baseLocation.clone().subtract(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));

        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 0.0D).add(0.0D, 1.0D, 2.0D), new SimpleBlock(Material.REDSTONE_TORCH_ON, 5));
        this.addBlockToUse(this.baseLocation.clone().add(2.0D, 1.0D, 2.0D), new SimpleBlock(Material.REDSTONE_TORCH_ON, 5));
        this.addBlockToUse(this.baseLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(2.0D, 1.0D, 0.0D), new SimpleBlock(Material.REDSTONE_TORCH_ON, 5));
        this.addBlockToUse(this.baseLocation.clone().subtract(2.0D, 0.0D, 2.0D).add(0.0D, 1.0D, 0.0D), new SimpleBlock(Material.REDSTONE_TORCH_ON, 5));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void display() {
        for (Location block : this.blocksUsed.keySet()) {
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }

        this.player.teleport(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 2.0D, 0.5D));

        this.hub.getServer().broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "Meow" + ChatColor.DARK_RED + ChatColor.BOLD + "] " + ChatColor.RED + ChatColor.BOLD + "Non ! " + this.player.getName() + " a lancé une bombe atomique à chat sur le monde ! Tous aux abris !");

        this.meowBossBar = new CraftBossBar(ChatColor.RED + "Meow", BarColor.PURPLE, BarStyle.SEGMENTED_10);
        this.hub.getServer().getOnlinePlayers().forEach(this.meowBossBar::addPlayer);

        SphereEffect sphereEffect = new SphereEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        //sphereEffect.particle = ParticleEffect.FLAME;
        sphereEffect.radius = 0.15F;
        sphereEffect.setLocation(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 5.5D, 0.5D));
        sphereEffect.infinite();
        sphereEffect.start();

        LineEffect cornerOneEffect = new LineEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        //cornerOneEffect.particle = ParticleEffect.ENCHANTMENT_TABLE;
        cornerOneEffect.setLocation(this.baseLocation.getBlock().getLocation().clone().subtract(2.0D, 0.0D, 0.0D).add(0.5D, 1.75D, 2.5D));
        cornerOneEffect.setTargetLocation(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 5.5D, 0.5D));
        cornerOneEffect.infinite();
        cornerOneEffect.start();

        LineEffect cornerTwoEffect = new LineEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        //cornerTwoEffect.particle = ParticleEffect.ENCHANTMENT_TABLE;
        cornerTwoEffect.setLocation(this.baseLocation.getBlock().getLocation().clone().add(2.5D, 1.75D, 2.5D));
        cornerTwoEffect.setTargetLocation(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 5.5D, 0.5D));
        cornerTwoEffect.infinite();
        cornerTwoEffect.start();

        LineEffect cornerThreeEffect = new LineEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        //cornerThreeEffect.particle = ParticleEffect.ENCHANTMENT_TABLE;
        cornerThreeEffect.setLocation(this.baseLocation.getBlock().getLocation().clone().subtract(0.0D, 0.0D, 2.0D).add(2.5D, 1.75D, 0.5D));
        cornerThreeEffect.setTargetLocation(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 5.5D, 0.5D));
        cornerThreeEffect.infinite();
        cornerThreeEffect.start();

        LineEffect cornerFourEffect = new LineEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        //cornerFourEffect.particle = ParticleEffect.ENCHANTMENT_TABLE;
        cornerFourEffect.setLocation(this.baseLocation.getBlock().getLocation().clone().subtract(2.0D, 0.0D, 2.0D).add(0.5D, 1.75D, 0.5D));
        cornerFourEffect.setTargetLocation(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 5.5D, 0.5D));
        cornerFourEffect.infinite();
        cornerFourEffect.start();

        this.loopFirst = this.hub.getServer().getScheduler().runTaskTimerAsynchronously(this.hub, new Runnable() {
            int ticks = 0;
            int timer = 10;
            float radius = 0.15F;

            @Override
            public void run() {
                if (this.ticks == 20) {
                    this.ticks = 0;
                    this.timer--;

                    if (this.timer == 0) {
                        hub.getServer().broadcastMessage(TAG + "GRAOUUW !");

                        sphereEffect.cancel();
                        cornerOneEffect.cancel();
                        cornerTwoEffect.cancel();
                        cornerThreeEffect.cancel();
                        cornerFourEffect.cancel();

                        timeToSendCatInTheHairLikeTheHandsInTheFamousSing();
                    } else if (this.timer <= 5) {
                        hub.getServer().broadcastMessage(TAG + this.timer);
                    }

                    meowBossBar.setProgress((100.0D - (this.timer * 10)) / 100);

                    for (Player player : hub.getServer().getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
                } else {
                    sphereEffect.radius = this.radius += 0.01F;
                    this.ticks += 2;
                }
            }
        }, 2L, 2L);
    }

    @Override
    public void handleInteraction(Entity who, Entity with) {
    }

    private void timeToSendCatInTheHairLikeTheHandsInTheFamousSing() {
        this.loopFirst.cancel();
        this.meowBossBar.setProgress(1.0D);

        TornadoEffect tornadoEffect = new TornadoEffect(this.hub.getCosmeticManager().getParticleManager().getEffectManager());
        tornadoEffect.setLocation(this.baseLocation.getBlock().getLocation().clone().add(0.5D, 1.5D, 0.5D));
        tornadoEffect.showCloud = false;
        tornadoEffect.period = 40;
        tornadoEffect.yOffset = 0.0D;
        tornadoEffect.tornadoHeight = 15.0F;
        tornadoEffect.maxTornadoRadius = 2.5F;
        //tornadoEffect.tornadoParticle = ParticleEffect.FIREWORKS_SPARK;
        tornadoEffect.infinite();
        tornadoEffect.start();

        this.loopSecond = this.hub.getServer().getScheduler().runTaskTimer(this.hub, new Runnable() {
            int loops = 0;

            @Override
            public void run() {
                this.loops++;

                if (this.loops == 500) {
                    baseLocation.getWorld().createExplosion(baseLocation.getX(), baseLocation.getY(), baseLocation.getZ(), 10, false, false);
                    meowBossBar.removeAll();

                    tornadoEffect.cancel();

                    restore();
                    end();
                    callback();
                } else if (this.loops == 1) {
                    player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 5.0F, false, false);
                }

                Ocelot ocelot = baseLocation.getWorld().spawn(baseLocation.getBlock().getLocation().clone().add(0.5D, 3.0D, 0.5D), Ocelot.class);
                ocelot.setCatType(Ocelot.Type.values()[GadgetManager.RANDOM.nextInt(Ocelot.Type.values().length)]);
                ocelot.setVelocity(new Vector(GadgetManager.RANDOM.nextInt(8) - 4, 5, GadgetManager.RANDOM.nextInt(8) - 4));
                ocelot.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Meow");
                ocelot.setCustomNameVisible(true);

                meowBossBar.setProgress((100.0D - (this.loops * 100.0 / 500.0)) / 100);

                if (GadgetManager.RANDOM.nextInt(5) == 3)
                    for (Player player : hub.getServer().getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);

                hub.getServer().getScheduler().runTaskLater(hub, () ->
                {
                    Color a = ColorUtils.getColor(GadgetManager.RANDOM.nextInt(17) + 1);
                    Color b = ColorUtils.getColor(GadgetManager.RANDOM.nextInt(17) + 1);

                    FireworkEffect fw = FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BURST).withColor(a).withFade(b).build();
                    FireworkUtils.launchfw(hub, ocelot.getLocation(), fw);

                    ocelot.setHealth(0);
                    ocelot.remove();
                }, 20L * 5);
            }
        }, 2L, 2L);
    }

    @Override
    public boolean isInteractionsEnabled() {
        return false;
    }

    public boolean canUse() {
        return true;
    }

    private void callback() {
        this.loopSecond.cancel();
    }
}
