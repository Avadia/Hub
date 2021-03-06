package net.samagames.hub.common.receivers;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
public class EventListener implements IPacketsReceiver {
    private final Hub hub;
    private final ConcurrentMap<UUID, String> waiting;

    public EventListener(Hub hub) {
        this.hub = hub;
        this.waiting = new ConcurrentHashMap<>();
    }

    @Override
    public void receive(String channel, String packet) {
        if (channel.equals("eventChannel")) {
            String[] data = packet.split(":");

            String gameCodeName = data[0];
            String template = data[1];
            int coins = Integer.parseInt(data[2]);
            int pearls = Integer.parseInt(data[3]);

            FancyMessage message = new FancyMessage("[Evénement] ").color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD)
                    .then("Une animation a débuté " + (gameCodeName.equals("hub") ? "sur le " : "en ")).color(ChatColor.LIGHT_PURPLE)
                    .then(gameCodeName.equals("hub") ? "hub " + template : this.hub.getGameManager().getGameByIdentifier(gameCodeName).getName()).color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD)
                    .then(". Récompense" + (coins > 0 && pearls > 0 ? "s" : "") + " : ").color(ChatColor.LIGHT_PURPLE);

            if (coins > 0)
                message.then(coins + " pièce" + (coins > 1 ? "s" : "")).color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD);

            if (coins > 0 && pearls > 0)
                message.then(" et ").color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD);

            if (pearls > 0)
                message.then(pearls + " perle" + (pearls > 1 ? "s" : "")).color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD);

            message.then(". ").color(ChatColor.LIGHT_PURPLE);
            message.then("[Cliquez ici]").color(ChatColor.AQUA).style(ChatColor.BOLD).command(gameCodeName.equals("hub") ? "/hub " + template : "/join " + gameCodeName + " " + template).tooltip(ChatColor.GOLD + "» Clic pour rejoindre");

            this.hub.getServer().getOnlinePlayers().forEach(player ->
            {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0F, 1.0F);

                player.sendMessage("");
                message.send(player);
                player.sendMessage("");
            });
        } else if (channel.equals("servers") && packet.startsWith("heartbeat")) {
            String[] data = packet.split(" ");

            for (UUID animator : this.waiting.keySet()) {
                if (this.hub.getServer().getPlayer(animator) == null) {
                    this.waiting.remove(animator);
                    continue;
                }

                String gameName = this.waiting.get(animator);

                if (data[1].startsWith(gameName)) {
                    SamaGamesAPI.get().getPubSub().send(data[1], "moderator " + animator);
                    SamaGamesAPI.get().getPubSub().send(data[1], "teleport " + animator);

                    this.waiting.remove(animator);
                }
            }
        }
    }

    public void setWaiting(UUID animator, String gameName) {
        this.waiting.put(animator, gameName);
    }
}
