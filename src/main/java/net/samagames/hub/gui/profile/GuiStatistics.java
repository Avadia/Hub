package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.stats.IPlayerStats;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

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
class GuiStatistics extends AbstractGui {
    private static final int[] BASE_SLTOS = {10, 11, 12, 13, 14, 15, 16};

    private final Player playerToView;

    private int lines = 0;
    private int slot = 0;

    GuiStatistics(Hub hub, Player playerToView) {
        super(hub);

        this.playerToView = playerToView;
    }

    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 45, "Statistiques");

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.update(player);
            this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
        });
    }

    @Override
    public void update(Player player) {
        IPlayerStats playerStats = SamaGamesAPI.get().getStatsManager().getPlayerStats(this.playerToView.getUniqueId());

        this.setGameStatisticsSlotData("Hub", new ItemStack(Material.COMPASS, 1), Arrays.asList(
                Pair.of("Woots reçus", playerStats.getJukeBoxStatistics()::getWoots),
                Pair.of("Woots donnés", playerStats.getJukeBoxStatistics()::getWootsGiven),
                Pair.of("Mehs reçus", playerStats.getJukeBoxStatistics()::getMehs)
        ));

        this.setGameStatisticsSlotData("UHC", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUHCOriginalStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUHCOriginalStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUHCOriginalStatistics()::getKills),
                Pair.of("Morts", playerStats.getUHCOriginalStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("UHCRun", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUHCRunStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUHCRunStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUHCRunStatistics()::getKills),
                Pair.of("Morts", playerStats.getUHCRunStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("DoubleRunner", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getDoubleRunnerStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getDoubleRunnerStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getDoubleRunnerStatistics()::getKills),
                Pair.of("Morts", playerStats.getDoubleRunnerStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("UHCRandom", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUHCRandomStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUHCRandomStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUHCRandomStatistics()::getKills),
                Pair.of("Morts", playerStats.getUHCRandomStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("RandomRun", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getRandomRunStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getRandomRunStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getRandomRunStatistics()::getKills),
                Pair.of("Morts", playerStats.getRandomRunStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("Run4Flag", new ItemStack(Material.GOLDEN_APPLE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUltraFlagKeeperStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUltraFlagKeeperStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUltraFlagKeeperStatistics()::getKills),
                Pair.of("Morts", playerStats.getUltraFlagKeeperStatistics()::getDeaths),
                Pair.of("Drapeaux capturés", playerStats.getUltraFlagKeeperStatistics()::getFlagsCaptured),
                Pair.of("Drapeaux retournés", playerStats.getUltraFlagKeeperStatistics()::getFlagsReturned)
        ));

        this.setGameStatisticsSlotData("Quake", new ItemStack(Material.DIAMOND_HOE, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getQuakeStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getQuakeStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getQuakeStatistics()::getKills),
                Pair.of("Morts", playerStats.getQuakeStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("Uppervoid", new ItemStack(Material.STICK, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getUppervoidStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getUppervoidStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getUppervoidStatistics()::getKills),
                Pair.of("TNT lancées", playerStats.getUppervoidStatistics()::getTntLaunched),
                Pair.of("Grenades lancées", playerStats.getUppervoidStatistics()::getGrenades),
                Pair.of("Blocs cassés", playerStats.getUppervoidStatistics()::getBlocks)
        ));

        this.setGameStatisticsSlotData("Dimensions", new ItemStack(Material.EYE_OF_ENDER, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getDimensionsStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getDimensionsStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getDimensionsStatistics()::getKills),
                Pair.of("Morts", playerStats.getDimensionsStatistics()::getDeaths)
        ));

        this.setGameStatisticsSlotData("ChunkWars", new ItemStack(Material.ENDER_PORTAL_FRAME, 1), Arrays.asList(
                Pair.of("Parties jouées", playerStats.getChunkWarsStatistics()::getPlayedGames),
                Pair.of("Parties gagnées", playerStats.getChunkWarsStatistics()::getWins),
                Pair.of("Meurtres", playerStats.getChunkWarsStatistics()::getKills),
                Pair.of("Morts", playerStats.getChunkWarsStatistics()::getDeaths)
        ));

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        switch (action) {
            case "back":
                this.hub.getGuiManager().openGui(player, new GuiProfile(this.hub));
                break;
        }
    }

    private void setGameStatisticsSlotData(String game, ItemStack icon, List<Pair<String, Callable<Integer>>> statistics) {
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + game);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Statistique de " + game);
        lore.add("");

        for (Pair<String, Callable<Integer>> statistic : statistics) {
            try {
                lore.add(ChatColor.GRAY + "- " + statistic.getLeft() + " : " + ChatColor.WHITE + statistic.getRight().call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        lore.add("");
        lore.add(ChatColor.GRAY + "Le détail de vos statistiques est");
        lore.add(ChatColor.GRAY + "disponible sur votre profil en ligne.");
        lore.add(ChatColor.GRAY + "Cliquez sur l'étoile pour y accéder.");

        meta.setLore(lore);
        icon.setItemMeta(meta);

        this.setSlotData(icon, (BASE_SLTOS[this.slot] + (this.lines * 9)), "none");

        this.slot++;

        if (this.slot == 7) {
            this.slot = 0;
            this.lines++;
        }
    }
}
