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
public class BlindGamesGame extends AbstractGame {
    public BlindGamesGame(Hub hub) {
        super(hub);
    }

    @Override
    public String getCodeName() {
        return "blindgames";
    }

    @Override
    public String getName() {
        return "BlindGames";
    }

    @Override
    public String getCategory() {
        return "Arcade";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.GOLD_RECORD, 1);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Divinez le plus de musiques pour gagner !"
        };
    }

    @Override
    public String[] getDevelopers() {
        return new String[]{
                "FarmVivi"
        };
    }

    @Override
    public String getWebsiteDescriptionURL() {
        return null;
    }

    @Override
    public int getSlotInMainMenu() {
        return 31;
    }

    @Override
    public ShopCategory getShopConfiguration() {
        return null;
    }

    @Override
    public Location getLobbySpawn() {
        return new Location(this.hub.getWorld(), 36.5D, 105.0D, 36.0D, -90.0F, 0.0F);
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
        return State.BETA;
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
