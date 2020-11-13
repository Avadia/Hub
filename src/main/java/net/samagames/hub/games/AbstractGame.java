package net.samagames.hub.games;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.stats.IPlayerStats;
import net.samagames.hub.Hub;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.signs.GameSign;
import net.samagames.hub.games.signs.TitleSign;
import net.samagames.hub.utils.RestrictedVersion;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public abstract class AbstractGame {
    protected final Hub hub;
    private final Map<String, List<GameSign>> signs;
    private TitleSign titleSign;

    public AbstractGame(Hub hub) {
        this.hub = hub;

        this.signs = new HashMap<String, List<GameSign>>() {
            @Override
            public void clear() {
                this.values().forEach(list -> list.forEach(GameSign::onDelete));
                super.clear();
            }
        };
    }

    public abstract String getCodeName();

    public abstract String getName();

    public abstract String getCategory();

    public abstract ItemStack getIcon();

    public abstract String[] getDescription();

    public abstract String[] getDevelopers();

    public abstract String getWebsiteDescriptionURL();

    public abstract int getSlotInMainMenu();

    public abstract ShopCategory getShopConfiguration();

    public abstract Location getLobbySpawn();

    public abstract Location getWebsiteDescriptionSkull();

    public abstract List<HubLeaderboard> getLeaderBoards();

    public abstract State getState();

    public abstract boolean hasResourcesPack();

    public abstract boolean isPlayerFirstGame(IPlayerStats playerStats);

    public abstract boolean isGroup();

    public void setSignForGame(Sign sign) {
        if (this.titleSign != null)
            this.titleSign.onDelete();
        this.titleSign = new TitleSign(this.hub, this, sign);
    }

    public void addSignForMap(String map, ChatColor color, String template, RestrictedVersion restrictedVersion, Sign sign) {
        GameSign gameSign = new GameSign(this.hub, this, map, color, template, restrictedVersion, sign);

        Jedis jedis = SamaGamesAPI.get().getBungeeResource();

        if (jedis != null && jedis.exists("hub:maintenance:" + this.getCodeName() + ":" + template))
            gameSign.setMaintenance(Boolean.parseBoolean(jedis.get("hub:maintenance:" + this.getCodeName() + ":" + template)));
        if (jedis != null && jedis.exists("hub:soon:" + this.getCodeName() + ":" + template))
            gameSign.setSoon(Boolean.parseBoolean(jedis.get("hub:soon:" + this.getCodeName() + ":" + template)));

        if (jedis != null)
            jedis.close();

        if (this.signs.containsKey(map)) {
            this.signs.get(map).add(gameSign);
        } else {
            List<GameSign> list = new ArrayList<>();
            list.add(gameSign);

            this.signs.put(map, list);
        }
    }

    public void clearSigns() {
        this.signs.clear();
    }

    public void showRulesWarning(Player player) {
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        new FancyMessage("Il s'agit de votre première partie sur ce jeu ! Nous vous conseillons d'aller d'abord lire les règles en").color(ChatColor.GREEN)
                .then("cliquant ici").color(ChatColor.GREEN).style(ChatColor.BOLD).link(this.getWebsiteDescriptionURL())
                .then(" pour accéder aux règles du jeu.").style(ChatColor.GREEN)
                .send(player);

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    public List<GameSign> getGameSignsByMap(String map) {
        return this.signs.getOrDefault(map, null);
    }

    public List<GameSign> getGameSignsByTemplate(String template) {
        for (List<GameSign> list : this.signs.values())
            if (list.get(0).getTemplate().equals(template))
                return list;

        return null;
    }

    public int getOnlinePlayers() {
        int players = 0;

        for (List<GameSign> list : this.signs.values())
            players += list.get(0).getTotalPlayerOnServers();

        return players;
    }

    public Map<String, List<GameSign>> getSigns() {
        return this.signs;
    }

    public boolean hasShop() {
        return this.getShopConfiguration() != null;
    }

    public enum State {OPENED, NEW, POPULAR, BETA, SOON, LOCKED}
}
