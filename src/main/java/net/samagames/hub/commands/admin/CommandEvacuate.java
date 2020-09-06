package net.samagames.hub.commands.admin;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.network.IJoinHandler;
import net.samagames.api.network.JoinResponse;
import net.samagames.api.network.ResponseType;
import net.samagames.hub.Hub;
import net.samagames.hub.commands.AbstractCommand;
import net.samagames.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class CommandEvacuate extends AbstractCommand {
    private boolean lock;
    private int timer;

    public CommandEvacuate(Hub hub) {
        super(hub);

        this.lock = false;
        this.timer = 60;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            if (!this.hasPermission((Player) sender)) {
                sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
                return true;
            }
        }

        if (this.lock)
            return true;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /evacuate <destination>");
            return true;
        }

        this.lock = true;

        this.hub.getServer().getScheduler().runTaskTimerAsynchronously(this.hub, () ->
        {
            if (this.timer == 60 || this.timer == 30 || this.timer == 10 || (this.timer <= 5 && this.timer > 0))
                this.hub.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Votre hub va redémarrer dans " + ChatColor.AQUA + ChatColor.BOLD + this.timer + " seconde" + (this.timer > 1 ? "s" : ""));

            this.hub.getServer().getOnlinePlayers().stream().filter(p -> this.timer > 0).forEach(p ->
            {
                if (this.timer <= 5)
                    p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_DEATH, 0.8F, 1.8F);
                else if (this.timer <= 30)
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 0.8F, 1.0F);

                Titles.sendTitle(p, 0, 22, 0, ChatColor.RED + "Attention !", ChatColor.GOLD + "Votre hub va redémarrer dans " + ChatColor.AQUA + this.timer + " seconde" + (this.timer > 1 ? "s" : ""));
            });

            if (this.timer == 0) {
                SamaGamesAPI.get().getJoinManager().registerHandler(new IJoinHandler() {
                    @Override
                    public JoinResponse requestJoin(UUID player, JoinResponse response) {
                        JoinResponse joinResponse = new JoinResponse();
                        joinResponse.disallow("Hub en cours de fermeture...");
                        joinResponse.disallow(ResponseType.DENY_CANT_RECEIVE);
                        return joinResponse;
                    }

                    @Override
                    public JoinResponse requestPartyJoin(UUID party, UUID player, JoinResponse response) {
                        JoinResponse joinResponse = new JoinResponse();
                        joinResponse.disallow("Hub en cours de fermeture...");
                        joinResponse.disallow(ResponseType.DENY_CANT_RECEIVE);
                        return joinResponse;
                    }

                    @Override
                    public void onLogin(UUID player, String username) {

                    }

                    @Override
                    public void finishJoin(Player player) {

                    }

                    @Override
                    public void onModerationJoin(Player player) {

                    }

                    @Override
                    public void onLogout(Player player) {

                    }
                }, 1000);

                this.hub.getServer().broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Fermeture du hub...");

                for (Player p : this.hub.getServer().getOnlinePlayers())
                    p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.9F, 1.0F);
            } else if (this.timer == -1) {
                for (Player p : this.hub.getServer().getOnlinePlayers())
                    SamaGamesAPI.get().getPlayerManager().connectToServer(p.getUniqueId(), args[0]);
            }

            this.timer--;
        }, 20L, 20L);

        return true;
    }

    @Override
    public boolean doAction(Player player, Command command, String s, String[] args) {
        return true;
    }
}
