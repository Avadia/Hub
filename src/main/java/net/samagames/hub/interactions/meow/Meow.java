package net.samagames.hub.interactions.meow;

import net.minecraft.server.v1_12_R1.WorldServer;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.achievements.exceptions.AchivementNotFoundException;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.interactions.AbstractInteraction;
import net.samagames.tools.holograms.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
class Meow extends AbstractInteraction {
    protected static final String TAG = ChatColor.GOLD + "[" + ChatColor.YELLOW + "Meow" + ChatColor.GOLD + "] " + ChatColor.RESET;
    private static final String MEOW_NAME = ChatColor.GOLD + "" + ChatColor.BOLD + "Meow";

    private final Map<UUID, Hologram> holograms;
    private final EntityMeow meowEntity;
    private final Random random;
    private BukkitTask thankYouTask;
    private boolean lock;

    Meow(Hub hub, Location location) {
        super(hub);

        this.holograms = new HashMap<>();

        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();

        this.meowEntity = new EntityMeow(world);
        this.meowEntity.setPosition(location.getX(), location.getY(), location.getZ());
        world.addEntity(this.meowEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        location.getChunk().load(true);

        hub.getServer().getScheduler().runTaskLater(hub, () -> this.meowEntity.postInit(location.getYaw(), location.getPitch()), 20L);

        this.random = new Random();
        this.thankYouTask = null;
        this.lock = false;
    }

    @Override
    public void onDisable() {
        if (this.thankYouTask != null)
            this.thankYouTask.cancel();

        this.meowEntity.die();
    }

    public void onLogin(Player player) {
        if (this.holograms.containsKey(player.getUniqueId()))
            this.onLogout(player);

        int fishes = 0;

        for (Bonus bonus : this.hub.getInteractionManager().getMeowManager().getBonus())
            if (bonus.isAbleFor(player))
                fishes++;

        Hologram hologram;

        if (fishes > 0)
            hologram = new Hologram(MEOW_NAME, ChatColor.GOLD + "" + fishes + ChatColor.YELLOW + " poisson" + (fishes > 1 ? "s" : "") + " disponible" + (fishes > 1 ? "s" : ""));
        else
            hologram = new Hologram(MEOW_NAME);

        hologram.generateLines(this.meowEntity.getBukkitEntity().getLocation().clone().add(0.0D, 0.75D, 0.0D));
        hologram.addReceiver(player);

        this.holograms.put(player.getUniqueId(), hologram);
    }

    public void onLogout(Player player) {
        if (this.holograms.containsKey(player.getUniqueId())) {
            Hologram hologram = this.holograms.get(player.getUniqueId());
            hologram.removeLinesForPlayers();
            hologram.removeReceiver(player);

            this.holograms.remove(player.getUniqueId());
        }
    }

    @Override
    public void play(Player player) {
        this.hub.getGuiManager().openGui(player, new GuiMeow(this.hub, this));
        player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);

        try {
            if (!SamaGamesAPI.get().getAchievementManager().isUnlocked(player.getUniqueId(), 19))
                SamaGamesAPI.get().getAchievementManager().getAchievementByID(19).unlock(player.getUniqueId());
        } catch (AchivementNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(Player player) { /* Not needed **/}

    public void update(Player player) {
        int fishes = 0;

        for (Bonus bonus : this.hub.getInteractionManager().getMeowManager().getBonus())
            if (bonus.isAbleFor(player))
                fishes++;

        Hologram hologram = this.holograms.get(player.getUniqueId());

        if (fishes > 0)
            hologram.change(MEOW_NAME, ChatColor.GOLD + "" + fishes + ChatColor.YELLOW + " poisson" + (fishes > 1 ? "s" : "") + " disponible" + (fishes > 1 ? "s" : ""));
        else
            hologram.change(MEOW_NAME);

        hologram.sendLines(player);
    }

    public void playThankYou() {
        if (this.lock)
            return;

        this.meowEntity.getBukkitEntity().getWorld().playSound(this.meowEntity.getBukkitEntity().getLocation(), Sound.ENTITY_CAT_AMBIENT, 1.0F, 1.5F);

        this.thankYouTask = new BukkitRunnable() {
            int times = 0;

            @Override
            public void run() {
                Item fish = meowEntity.getBukkitEntity().getWorld().dropItem(meowEntity.getBukkitEntity().getLocation(), new ItemStack(Material.RAW_FISH, 1));
                fish.setVelocity(new Vector(random.nextFloat() * 2 - 1, 1.25F, random.nextFloat() * 2 - 1));

                try {
                    GadgetManager.AGE_FIELD.set(((CraftItem) fish).getHandle(), 5860);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                meowEntity.getBukkitEntity().getWorld().playSound(meowEntity.getBukkitEntity().getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.1F);

                this.times++;

                if (this.times == 40) {
                    lock = false;
                    this.cancel();
                }
            }
        }.runTaskTimer(this.hub, 2L, 2L);
    }

    public EntityMeow getMeowEntity() {
        return this.meowEntity;
    }

    @Override
    public boolean hasPlayer(Player player) {
        return this.hub.getGuiManager().getPlayerGui(player) instanceof GuiMeow;
    }
}
