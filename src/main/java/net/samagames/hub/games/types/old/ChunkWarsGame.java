package net.samagames.hub.games.types.old;

import net.samagames.api.stats.IPlayerStats;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.leaderboards.HubLeaderboard;
import net.samagames.hub.games.shops.ShopCategory;
import net.samagames.hub.games.shops.ShopDependsItem;
import net.samagames.hub.games.shops.ShopItem;
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
public class ChunkWarsGame extends AbstractGame {
    public ChunkWarsGame(Hub hub) {
        super(hub);
    }

    @Override
    public String getCodeName() {
        return "chunkwars";
    }

    @Override
    public String getName() {
        return "ChunkWars";
    }

    @Override
    public String getCategory() {
        return "PvP";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ENDER_PORTAL_FRAME, 1);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public String[] getDevelopers() {
        return new String[]{
                "IamBlueSlime",
                "LordFinn",
                "6infinity8"
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
        try {
            ShopCategory shopCategory = new ShopCategory(this.hub, this, 202, 0);

            // -----------------------------------------

            // ShopCategory kitCategory = new ShopCategory(this.hub, this, 203, 21);

            // -----------------------------------------

            ShopCategory cosmeticCategory = new ShopCategory(this.hub, this, 203, 23);

            // ---------------

            int[] nacelleIds = new int[]{208, 209, 210, 211, 212, 213, 214, 215, 216};

            ShopCategory nacelleCosmeticCategory = new ShopCategory(this.hub, this, 204, 19);
            ShopItem littleNacelleCosmetic = new ShopItem(this.hub, "Nacelle", nacelleIds[0], 19, nacelleIds).defaultItem();
            ShopDependsItem mediumNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[1], 20, nacelleIds, littleNacelleCosmetic);
            ShopDependsItem bigNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[2], 21, nacelleIds, mediumNacelleCosmetic);
            ShopDependsItem medievalNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[3], 14, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem terraNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[4], 23, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem nisbhajaNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[5], 32, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem propellerNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[6], 16, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem baroqueNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[7], 25, nacelleIds, bigNacelleCosmetic);
            ShopDependsItem skyNacelleCosmetic = new ShopDependsItem(this.hub, "Nacelle", nacelleIds[8], 34, nacelleIds, bigNacelleCosmetic);

            nacelleCosmeticCategory.addContent(littleNacelleCosmetic);
            nacelleCosmeticCategory.addContent(mediumNacelleCosmetic);
            nacelleCosmeticCategory.addContent(bigNacelleCosmetic);
            nacelleCosmeticCategory.addContent(medievalNacelleCosmetic);
            nacelleCosmeticCategory.addContent(terraNacelleCosmetic);
            nacelleCosmeticCategory.addContent(nisbhajaNacelleCosmetic);
            nacelleCosmeticCategory.addContent(propellerNacelleCosmetic);
            nacelleCosmeticCategory.addContent(baroqueNacelleCosmetic);
            nacelleCosmeticCategory.addContent(skyNacelleCosmetic);

            // ---------------

            int[] woolColorIds = new int[]{218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234};
            int[] glassColorIds = new int[]{236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252};

            ShopCategory colorCosmeticCategory = new ShopCategory(this.hub, this, 205, 21);

            ShopCategory woolColorCosmeticCategory = new ShopCategory(this.hub, this, 217, 21);
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[0], 10, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[1], 11, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[2], 12, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[3], 13, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[4], 14, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[5], 15, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[6], 16, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[7], 19, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[8], 20, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[9], 21, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[10], 22, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[11], 23, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[12], 24, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[13], 25, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[14], 30, woolColorIds).defaultItem());
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de laine", woolColorIds[15], 32, woolColorIds));
            woolColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur... Couleur ?", woolColorIds[16], 31, woolColorIds));

            ShopCategory glassColorCosmeticCategory = new ShopCategory(this.hub, this, 235, 23);
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[0], 10, glassColorIds).defaultItem());
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[1], 11, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[2], 12, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[3], 13, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[4], 14, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[5], 15, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[6], 16, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[7], 19, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[8], 20, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[9], 21, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[10], 22, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[11], 23, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[12], 24, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[13], 25, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[14], 30, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur de verre", glassColorIds[15], 32, glassColorIds));
            glassColorCosmeticCategory.addContent(new ShopItem(this.hub, "Couleur... Couleur ?", glassColorIds[16], 31, glassColorIds));

            colorCosmeticCategory.addContent(woolColorCosmeticCategory);
            colorCosmeticCategory.addContent(glassColorCosmeticCategory);

            // ---------------

            int[] patternIds = new int[]{253, 254, 255, 256, 257, 258, 259, 260};

            ShopCategory patternCosmeticCategory = new ShopCategory(this.hub, this, 206, 23);
            patternCosmeticCategory.addContent(new ShopItem(this.hub, "Motif", patternIds[0], 12, patternIds).defaultItem());
            patternCosmeticCategory.addContent(new ShopItem(this.hub, "Motif", patternIds[1], 13, patternIds));
            patternCosmeticCategory.addContent(new ShopItem(this.hub, "Motif", patternIds[2], 14, patternIds));
            patternCosmeticCategory.addContent(new ShopItem(this.hub, "Motif", patternIds[3], 23, patternIds));
            patternCosmeticCategory.addContent(new ShopItem(this.hub, "Motif", patternIds[4], 32, patternIds));
            patternCosmeticCategory.addContent(new ShopItem(this.hub, "Motif", patternIds[5], 31, patternIds));
            patternCosmeticCategory.addContent(new ShopItem(this.hub, "Motif", patternIds[6], 30, patternIds));
            patternCosmeticCategory.addContent(new ShopItem(this.hub, "Motif", patternIds[7], 21, patternIds));

            // ---------------

            int[] decorationIds = new int[]{261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 333, 334, 335};

            ShopCategory decorationCosmeticCategory = new ShopCategory(this.hub, this, 207, 25);
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[0], 10, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[1], 11, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[2], 12, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[3], 19, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[4], 20, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[5], 21, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[6], 28, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[7], 14, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[8], 15, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[9], 23, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[10], 24, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[11], 25, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[12], 29, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[13], 30, decorationIds));
            decorationCosmeticCategory.addContent(new ShopItem(this.hub, "Décoration", decorationIds[14], 16, decorationIds));

            // ---------------

            cosmeticCategory.addContent(nacelleCosmeticCategory);
            cosmeticCategory.addContent(colorCosmeticCategory);
            cosmeticCategory.addContent(patternCosmeticCategory);
            cosmeticCategory.addContent(decorationCosmeticCategory);

            // -----------------------------------------

            shopCategory.addContent(cosmeticCategory);

            return shopCategory;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public Location getLobbySpawn() {
        return new Location(this.hub.getWorld(), -55.5D, 96.0D, 43.5D, 50.0F, 0.0F);
    }

    @Override
    public Location getWebsiteDescriptionSkull() {
        return new Location(this.hub.getWorld(), -63.0, 97.0D, 52.0D, 0.0F, 0.0F);
    }

    @Override
    public List<HubLeaderboard> getLeaderBoards() {
        return null;
    }

    @Override
    public State getState() {
        return State.LOCKED;
    }

    @Override
    public boolean hasResourcesPack() {
        return false;
    }

    @Override
    public boolean isPlayerFirstGame(IPlayerStats playerStats) {
        return playerStats.getChunkWarsStatistics().getPlayedGames() == 0;
    }

    @Override
    public boolean isGroup() {
        return false;
    }
}
