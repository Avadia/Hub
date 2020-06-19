package net.samagames.hub.games.leaderboards;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.GamesNames;
import net.samagames.api.stats.Leaderboard;
import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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
public class HubLeaderboard {
    protected Hub hub;
    protected Location sign;
    protected List<HubLeaderBoardStand> stands;
    private GamesNames game;
    private String gameName;
    private String displayName;
    private String statName;

    HubLeaderboard() {
    }

    public HubLeaderboard(Hub hub, GamesNames game, String gameName, String displayName, String statName, Location sign, List<HubLeaderBoardStand> stands) {
        this.hub = hub;
        this.game = game;
        this.gameName = gameName;
        this.displayName = displayName;
        this.statName = statName;
        this.sign = sign;
        this.stands = stands;
    }

    @SuppressWarnings("deprecation")
    public void refresh() {
        final Leaderboard leaderboard = SamaGamesAPI.get().getStatsManager().getLeaderboard(this.getGame(), this.getStatName());
        if (leaderboard == null)
            return;
        this.hub.getServer().getScheduler().runTask(this.hub, () ->
        {
            Block block = this.sign.getBlock();
            if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) block.getState();
                sign.setLine(0, "*-*-*-*-*");
                sign.setLine(1, this.getGameName());
                sign.setLine(2, this.getDisplayName());
                sign.setLine(3, "*-*-*-*-*");
                sign.update();
            }
            int i;
            for (i = 0; i < 3 && i < this.stands.size(); i++) {
                Leaderboard.PlayerStatData stat = i == 0 ? leaderboard.getFirst() : i == 1 ? leaderboard.getSecond() : leaderboard.getThird();
                Block block2 = this.stands.get(i).sign.getBlock();
                if (block2.getType() == Material.SIGN_POST || block2.getType() == Material.WALL_SIGN) {
                    Sign sign = (Sign) block2.getState();
                    sign.setLine(0, "*-*-*-*-*");
                    sign.setLine(1, stat == null ? "" : stat.getName());
                    sign.setLine(2, stat == null ? "" : String.valueOf(stat.getScore()));
                    sign.setLine(3, "*-*-*-*-*");
                    sign.update();
                }
                ArmorStand armorStand = (ArmorStand) this.stands.get(i).hologram.getWorld().getNearbyEntities(this.stands.get(i).hologram, 0.3D, 2D, 0.3D).stream().filter(entity -> entity instanceof ArmorStand).findFirst().orElse(null);
                if (armorStand != null) {
                    armorStand.setCustomNameVisible(stat != null);
                    armorStand.setCustomName(stat == null ? "" : (i == 0 ? ChatColor.AQUA + "1er" : i == 1 ? ChatColor.GOLD + "2e" : ChatColor.GRAY + "3e") + " - " + stat.getName());
                    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
                    SkullMeta meta = (SkullMeta) skull.getItemMeta();
                    meta.setOwner(stat == null ? "Aurelien_Sama" : stat.getName());
                    skull.setItemMeta(meta);
                    armorStand.setHelmet(skull);
                }
            }
        });
    }

    protected String getDisplayName() {
        return this.displayName;
    }

    protected GamesNames getGame() {
        return this.game;
    }

    protected String getStatName() {
        return this.statName;
    }

    protected String getGameName() {
        return this.gameName;
    }

    public static class HubLeaderBoardStand {
        private final Location sign;
        private final Location hologram;

        public HubLeaderBoardStand(Location sign, Location hologram) {
            this.sign = sign;
            this.hologram = hologram;
        }
    }
}
