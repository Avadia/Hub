package net.samagames.hub.common.tasks;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.exceptions.DataNotFoundException;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.ProximityUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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
public class TaskManager extends AbstractManager {
    private final CirclesTask circlesTask;
    private final BirthdayTask birthdayTask;
    private final PlayersAwayFromKeyboardTask playersAwayFromKeyboardTask;
    private final AdvertisingTask advertisingTask;

    private final BukkitTask secretChamberProximityTask;
    private final BukkitTask pantheonProximityTask;
    private final BukkitTask confessionalProximityTask;
    private final BukkitTask bathProximityTask;
    private final BukkitTask serverRoomProximityTask;
    private final BukkitTask webDeveloperArtProximityTask;

    public TaskManager(Hub hub) {
        super(hub);

        this.circlesTask = new CirclesTask(hub);
        this.birthdayTask = new BirthdayTask(hub);
        this.playersAwayFromKeyboardTask = new PlayersAwayFromKeyboardTask(hub);
        this.advertisingTask = new AdvertisingTask(hub);

        ArmorStand secretChamberDetectionEntity = hub.getWorld().spawn(new Location(hub.getWorld(), 88.5, 20.0D, 44.5D), ArmorStand.class);
        this.prepareProximityDetection(secretChamberDetectionEntity, "secret_chamber_proximity");
        this.secretChamberProximityTask = ProximityUtils.onNearbyOf(hub, secretChamberDetectionEntity, 5.0D, 5.0D, 5.0D, Player.class, player ->
                this.hub.getServer().getScheduler().runTask(hub, () -> {
                    try {
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(7).unlock(player.getUniqueId());
                    } catch (DataNotFoundException e) {
                        e.printStackTrace();
                    }
                }));

        ArmorStand pantheonDetectionEntity = hub.getWorld().spawn(new Location(hub.getWorld(), -174.5D, 82.0D, 27.5D), ArmorStand.class);
        this.prepareProximityDetection(pantheonDetectionEntity, "pantheon_proximity");
        this.pantheonProximityTask = ProximityUtils.onNearbyOf(hub, pantheonDetectionEntity, 3.0D, 5.0D, 3.0D, Player.class, player ->
                this.hub.getServer().getScheduler().runTask(hub, () -> {
                    try {
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(10).unlock(player.getUniqueId());
                    } catch (DataNotFoundException e) {
                        e.printStackTrace();
                    }
                }));

        ArmorStand confessionalDetectionEntity = hub.getWorld().spawn(new Location(hub.getWorld(), -159.5, 69.0D, 42.5D), ArmorStand.class);
        this.prepareProximityDetection(confessionalDetectionEntity, "confessional_proximity");
        this.confessionalProximityTask = ProximityUtils.onNearbyOf(hub, confessionalDetectionEntity, 0.5D, 1.0D, 0.5D, Player.class, player ->
                this.hub.getServer().getScheduler().runTask(hub, () -> {
                    try {
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(11).unlock(player.getUniqueId());
                    } catch (DataNotFoundException e) {
                        e.printStackTrace();
                    }
                }));

        ArmorStand bathDetectionEntity = hub.getWorld().spawn(new Location(hub.getWorld(), -148.5, 70.0D, 26.5D), ArmorStand.class);
        this.prepareProximityDetection(bathDetectionEntity, "bath_proximity");
        this.bathProximityTask = ProximityUtils.onNearbyOf(hub, bathDetectionEntity, 5.0D, 3.0D, 5.0D, Player.class, player ->
                this.hub.getServer().getScheduler().runTask(hub, () -> {
                    try {
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(12).unlock(player.getUniqueId());
                    } catch (DataNotFoundException e) {
                        e.printStackTrace();
                    }
                }));

        ArmorStand serverRoomDetectionEntity = hub.getWorld().spawn(new Location(hub.getWorld(), -142.5, 40.0D, 78.5D), ArmorStand.class);
        this.prepareProximityDetection(serverRoomDetectionEntity, "server_room_proximity");
        this.serverRoomProximityTask = ProximityUtils.onNearbyOf(hub, serverRoomDetectionEntity, 1.5D, 1.5D, 1.5D, Player.class, player ->
                this.hub.getServer().getScheduler().runTask(hub, () -> {
                    try {
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(51).unlock(player.getUniqueId());
                    } catch (DataNotFoundException e) {
                        e.printStackTrace();
                    }
                }));

        ArmorStand webDeveloperArtDetectionEntity = hub.getWorld().spawn(new Location(hub.getWorld(), -178.5, 28.0D, 40.5D), ArmorStand.class);
        this.prepareProximityDetection(webDeveloperArtDetectionEntity, "web_dev_art_proximity");
        this.webDeveloperArtProximityTask = ProximityUtils.onNearbyOf(hub, webDeveloperArtDetectionEntity, 3.0D, 3.0D, 3.0D, Player.class, player ->
                this.hub.getServer().getScheduler().runTask(hub, () -> {
                    try {
                        SamaGamesAPI.get().getAchievementManager().getAchievementByID(54).unlock(player.getUniqueId());
                    } catch (DataNotFoundException e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public void onDisable() {
        this.circlesTask.cancel();
        this.birthdayTask.cancel();
        this.playersAwayFromKeyboardTask.cancel();
        this.advertisingTask.cancel();

        this.secretChamberProximityTask.cancel();
        this.pantheonProximityTask.cancel();
        this.confessionalProximityTask.cancel();
        this.bathProximityTask.cancel();
        this.serverRoomProximityTask.cancel();
        this.webDeveloperArtProximityTask.cancel();
    }

    @Override
    public void onLogin(Player player) {
        this.advertisingTask.addPlayer(player);
    }

    @Override
    public void onLogout(Player player) {
        this.advertisingTask.removePlayer(player);
    }

    public CirclesTask getCirclesTask() {
        return this.circlesTask;
    }

    public PlayersAwayFromKeyboardTask getPlayersAwayFromKeyboardTask() {
        return this.playersAwayFromKeyboardTask;
    }

    private void prepareProximityDetection(ArmorStand armorStand, String customName) {
        armorStand.setCustomName(customName);
        armorStand.setCustomNameVisible(false);
        armorStand.getNearbyEntities(2.0, 2.0, 2.0).stream().filter(entity -> entity.getType() == EntityType.ARMOR_STAND).filter(entity -> entity.getCustomName().equals(armorStand.getCustomName())).forEach(Entity::remove);
    }
}
