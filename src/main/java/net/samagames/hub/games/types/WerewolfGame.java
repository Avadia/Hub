package net.samagames.hub.games.types;

import net.samagames.api.stats.IPlayerStats;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
public class WerewolfGame extends AbstractGame {
    public WerewolfGame(Hub hub) {
        super(hub);
    }

    @Override
    public String getCodeName() {
        return "werewolf";
    }

    @Override
    public String getName() {
        return "Loup Garou";
    }

    @Override
    public String getCategory() {
        return "Tour par tour";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.BONE, 1);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Inspiré du jeu de société",
                "'Les Loups-garous de Thiercelieux',",
                "chaque joueur incarne un villageois",
                "ou un loup-garou."
        };
    }

    @Override
    public String[] getDevelopers() {
        return new String[]{
                "Leomelki",
                "TheOptimisticFactory",
                "FarmVivi"
        };
    }

    @Override
    public String getWebsiteDescriptionURL() {
        return null;
    }

    @Override
    public int getSlotInMainMenu() {
        return 13;
    }

    @Override
    public ShopCategory getShopConfiguration() {
        return null;
    }

    @Override
    public Location getLobbySpawn() {
        return new Location(this.hub.getWorld(), -59.5D, 96.0D, 43.5D, 90.0F, 0.0F);
    }

    @Override
    public Location getWebsiteDescriptionSkull() {
        return null;
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards() {
        return null;
    }

    @Override
    public State getState() {
        return State.NEW;
    }

    @Override
    public boolean hasResourcesPack() {
        return false;
    }

    @Override
    public boolean isPlayerFirstGame(IPlayerStats playerStats) {
        return false;
    }

    @Override
    public boolean isGroup() {
        return false;
    }
}
