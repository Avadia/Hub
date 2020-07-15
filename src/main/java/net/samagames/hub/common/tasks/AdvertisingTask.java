package net.samagames.hub.common.tasks;

import fr.farmvivi.api.commons.Servers;
import net.samagames.hub.Hub;
import net.samagames.tools.bossbar.BossBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
class AdvertisingTask extends AbstractTask {
    private static final List<String> LINES;

    static {
        LINES = new ArrayList<>();
        if (!Servers.DEFAULT.getWebsite().equals(""))
            LINES.add(ChatColor.YELLOW + "Toutes les informations sur " + ChatColor.RED + Servers.DEFAULT.getWebsite() + ChatColor.YELLOW + " !");
        if (!Servers.DEFAULT.getTwitter().equals(""))
            LINES.add(ChatColor.YELLOW + Servers.DEFAULT.getDisplayName() + " est sur Twitter : " + ChatColor.AQUA + "@" + Servers.DEFAULT.getTwitter() + ChatColor.YELLOW + " !");
        if (!Servers.DEFAULT.getYoutube().equals(""))
            LINES.add(ChatColor.YELLOW + Servers.DEFAULT.getDisplayName() + " est sur YouTube : " + ChatColor.RED + "@" + Servers.DEFAULT.getYoutube() + ChatColor.YELLOW + " !");
        if (!Servers.DEFAULT.getTeamspeak().equals(""))
            LINES.add(ChatColor.YELLOW + "Venez discuter sur TeamSpeak : " + ChatColor.GREEN + Servers.DEFAULT.getTeamspeak() + ChatColor.YELLOW + " !");
        if (!Servers.DEFAULT.getDiscord().equals(""))
            LINES.add(ChatColor.YELLOW + "Venez discuter sur Discord : " + ChatColor.BLUE + Servers.DEFAULT.getDiscord() + ChatColor.YELLOW + " !");
    }

    private final BossBar bossBar;
    private int i;

    AdvertisingTask(Hub hub) {
        super(hub);

        this.bossBar = BossBarAPI.getBar(ChatColor.YELLOW + Servers.DEFAULT.getDisplayName(), BarColor.RED, BarStyle.SOLID, 0.0D).getValue();
        this.bossBar.setProgress(0.0D);

        this.hub.getServer().getOnlinePlayers().forEach(this.bossBar::addPlayer);

        this.i = 0;

        this.task = this.hub.getServer().getScheduler().runTaskTimer(hub, this, 20L * 5, 20L * 5);
    }

    @Override
    public void run() {
        this.bossBar.setTitle(LINES.get(this.i));

        this.i++;

        if (this.i == LINES.size())
            this.i = 0;
    }

    public void addPlayer(Player player) {
        this.bossBar.addPlayer(player);
    }

    public void removePlayer(Player player) {
        this.bossBar.removePlayer(player);
    }
}
