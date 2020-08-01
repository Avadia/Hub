package net.samagames.hub.games;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroangeas.packets.PacketCallBack;
import net.samagames.hub.common.hydroangeas.packets.hubinfos.GameInfoToHubPacket;
import net.samagames.hub.common.hydroangeas.packets.queues.QueueInfosUpdatePacket;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.signs.GameSign;
import net.samagames.hub.games.types.*;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.chat.ActionBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

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
public class GameManager extends AbstractManager {
    private final Map<String, AbstractGame> games;
    private final CopyOnWriteArrayList<UUID> playerHided;

    public GameManager(Hub hub) {
        super(hub);

        this.games = new HashMap<>();
        this.playerHided = new CopyOnWriteArrayList<>();

        this.registerGame(new BackEndGame(hub, "beta_vip", "VIP", LocationUtils.str2loc(hub.getConfig().getString("vip-zone")), null, false));

        this.registerGame(new UppervoidGame(hub));
        this.registerGame(new QuakeGame(hub));
        this.registerGame(new DimensionsGame(hub));
        this.registerGame(new ChunkWarsGame(hub));
        this.registerGame(new BuildingJumpGame(hub));

        // -----

        UHCZoneGame uhcZoneGame = new UHCZoneGame(hub);

        this.registerGame(uhcZoneGame);
        this.registerGame(new BackEndGame(hub, "uhc", "UHC", uhcZoneGame.getLobbySpawn(), null, false, playerStats -> playerStats.getUHCOriginalStatistics().getPlayedGames() == 0));
        this.registerGame(new BackEndGame(hub, "uhcrun", "UHCRun", uhcZoneGame.getLobbySpawn(), null, false, playerStats -> playerStats.getUHCRunStatistics().getPlayedGames() == 0));
        this.registerGame(new BackEndGame(hub, "doublerunner", "DoubleRunner", uhcZoneGame.getLobbySpawn(), null, false, playerStats -> playerStats.getDoubleRunnerStatistics().getPlayedGames() == 0));
        this.registerGame(new BackEndGame(hub, "uhcrandom", "UHCRandom", uhcZoneGame.getLobbySpawn(), null, false, playerStats -> playerStats.getUHCRandomStatistics().getPlayedGames() == 0));
        this.registerGame(new BackEndGame(hub, "randomrun", "RandomRun", uhcZoneGame.getLobbySpawn(), null, false, playerStats -> playerStats.getRandomRunStatistics().getPlayedGames() == 0));
        this.registerGame(new BackEndGame(hub, "ultraflagkeeper", "Run4Flag", uhcZoneGame.getLobbySpawn(), null, false, playerStats -> playerStats.getUltraFlagKeeperStatistics().getPlayedGames() == 0));

        // -----

        this.registerGame(new OneTwoThreeSunGame(hub));

        this.registerGame(new BackEndGame(hub, "witherparty", "WitherParty", this.hub.getPlayerManager().getSpawn(), null, false));
        this.registerGame(new BackEndGame(hub, "hangovergames", "HangoverGames", this.hub.getPlayerManager().getSpawn(), null, false));
        this.registerGame(new BackEndGame(hub, "pacman", "PacMan", this.hub.getPlayerManager().getSpawn(), null, false));
        this.registerGame(new BackEndGame(hub, "flyring", "FlyRing", this.hub.getPlayerManager().getSpawn(), null, false));
        this.registerGame(new BackEndGame(hub, "casino", "Casino", this.hub.getPlayerManager().getSpawn(), null, false));
        this.registerGame(new BackEndGame(hub, "agarmc", "AgarMC", this.hub.getPlayerManager().getSpawn(), null, true));
        this.registerGame(new BackEndGame(hub, "timberman", "TimberMan", this.hub.getPlayerManager().getSpawn(), null, false));
        this.registerGame(new BackEndGame(hub, "bomberman", "BomberMan", this.hub.getPlayerManager().getSpawn(), null, true));
        this.registerGame(new BackEndGame(hub, "bowling", "Bowling", this.hub.getPlayerManager().getSpawn(), null, true));
        this.registerGame(new BackEndGame(hub, "burnthatchicken", "BurnThatChicken", this.hub.getPlayerManager().getSpawn(), null, false));
        this.registerGame(new BackEndGame(hub, "plagiat", "Plagiat", this.hub.getPlayerManager().getSpawn(), null, false));
        this.registerGame(new BackEndGame(hub, "werewolf", "LoupGarou", this.hub.getPlayerManager().getSpawn(), null, false));

        // -----

        this.registerGame(new BackEndGame(hub, "event", "Événement", this.hub.getPlayerManager().getSpawn(), null, false));

        hub.getHydroangeasManager().getPacketReceiver().registerCallBack(new PacketCallBack<GameInfoToHubPacket>(GameInfoToHubPacket.class) {
            @Override
            public void call(GameInfoToHubPacket packet) {
                for (AbstractGame game : games.values()) {
                    for (List<GameSign> list : game.getSigns().values()) {
                        list.stream().filter(sign -> sign.getTemplate().equalsIgnoreCase(packet.getTemplateID())).forEach(sign ->
                        {
                            sign.setPlayerPerGame(packet.getPlayerMaxForMap());
                            sign.setPlayerWaitFor(packet.getPlayerWaitFor());
                            sign.setTotalPlayerOnServers(packet.getTotalPlayerOnServers());
                            sign.update();
                        });
                    }
                }
            }
        });

        hub.getHydroangeasManager().getPacketReceiver().registerCallBack(new PacketCallBack<QueueInfosUpdatePacket>(QueueInfosUpdatePacket.class) {
            @Override
            public void call(QueueInfosUpdatePacket packet) {
                try {
                    Player player = hub.getServer().getPlayer(packet.getPlayer().getUUID());

                    if (!packet.isSuccess() && (packet.getErrorMessage() != null && !packet.getErrorMessage().isEmpty())) {
                        player.sendRawMessage(packet.getErrorMessage());
                        return;
                    }

                    if (player != null) {
                        if (packet.getType().equals(QueueInfosUpdatePacket.Type.ADD)) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 1.5F);

                            ActionBarAPI.removeMessage(player);
                            ActionBarAPI.sendMessage(player, ChatColor.GREEN + "Ajouté à la file d'attente de " + ChatColor.GOLD + packet.getGame() + ChatColor.GREEN + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.GREEN + " !");

                            if (getGameByIdentifier(packet.getGame()).hasResourcesPack()) {
                                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                                player.sendMessage(ChatColor.AQUA + "Ce jeu fait l'usage d'un pack de ressources. Vérifiez vos paramètres afin qu'ils soient correctement activés.");
                                player.sendMessage(ChatColor.AQUA + "Si vous n'utilisez pas le pack de ressources avant le début de la partie, vous serez renvoyé au Hub.");
                                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                            }
                        } else if (packet.getType().equals(QueueInfosUpdatePacket.Type.REMOVE)) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 0.8F);

                            ActionBarAPI.removeMessage(player);
                            ActionBarAPI.sendMessage(player, ChatColor.RED + "Retiré de la file d'attente de " + ChatColor.GOLD + packet.getGame() + ChatColor.RED + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.RED + " !");
                        } else if (packet.getType().equals(QueueInfosUpdatePacket.Type.INFO)) {
                            if (!SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isWaitingLineNotification())
                                return;

                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 10.0F, 2.0F);

                            if (packet.isStarting())
                                ActionBarAPI.sendPermanentMessage(player, ChatColor.GREEN + "Votre serveur démarre ! (Temps estimé : " + ChatColor.AQUA + TimeUnit.MILLISECONDS.toSeconds(packet.getTimeToStart()) + " secondes" + ChatColor.GREEN + ")");
                            else
                                ActionBarAPI.sendPermanentMessage(player, ChatColor.YELLOW + "Il manque " + ChatColor.RED + packet.getRemainingPlayer() + ChatColor.YELLOW + " joueur" + (packet.getRemainingPlayer() > 1 ? "s" : "") + " pour démarrer votre serveur.");
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });

        this.hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, () ->
        {
            List<UUID> toHide = new ArrayList<>();

            for (AbstractGame game : this.getGames().values())
                for (List<GameSign> list : game.getSigns().values())
                    for (GameSign sign : list)
                        this.hub.getWorld().getNearbyEntities(sign.getSign().getLocation(), 1.0D, 1.0D, 1.0D).stream().filter(entity -> entity instanceof Player && !toHide.contains(entity.getUniqueId())).forEach(entity -> toHide.add(entity.getUniqueId()));

            for (UUID playerUUID : toHide) {
                Player player = this.hub.getServer().getPlayer(playerUUID);

                if (player == null)
                    continue;

                for (Player pPlayer : this.hub.getServer().getOnlinePlayers())
                    this.hub.getServer().getScheduler().runTask(hub, () -> pPlayer.hidePlayer(this.hub, player));
            }

            this.playerHided.addAll(toHide);

            for (UUID playerUUID : this.playerHided) {
                Player player = this.hub.getServer().getPlayer(playerUUID);

                if (player == null) {
                    this.playerHided.remove(playerUUID);
                    continue;
                }

                if (!toHide.contains(playerUUID)) {
                    this.playerHided.remove(playerUUID);

                    for (Player other : this.hub.getServer().getOnlinePlayers())
                        this.hub.getServer().getScheduler().runTask(hub, () -> other.showPlayer(this.hub, player));
                }
            }
        }, 20L * 2, 20L * 2);
    }

    @Override
    public void onDisable() {
        this.games.values().forEach(AbstractGame::clearSigns);
    }

    @Override
    public void onLogin(Player player) { /* Not needed **/}

    @Override
    public void onLogout(Player player) {/* Not needed **/}

    private void registerGame(AbstractGame game) {
        if (!this.games.containsKey(game.getCodeName())) {
            this.games.put(game.getCodeName(), game);
            this.log(Level.INFO, "Registered game '" + game.getCodeName() + "'");
        }
    }

    public AbstractGame getGameByIdentifier(String identifier) {
        return this.games.getOrDefault(identifier.toLowerCase(), null);
    }

    public Map<String, AbstractGame> getGames() {
        return this.games;
    }
}
