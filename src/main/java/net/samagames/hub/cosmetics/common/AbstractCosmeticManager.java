package net.samagames.hub.cosmetics.common;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
public abstract class AbstractCosmeticManager<COSMETIC extends AbstractCosmetic> {
    protected final Hub hub;
    private final AbstractCosmeticRegistry<COSMETIC> registry;
    protected ConcurrentMap<UUID, List<COSMETIC>> equipped;

    public AbstractCosmeticManager(Hub hub, AbstractCosmeticRegistry<COSMETIC> registry) {
        this.hub = hub;
        this.registry = registry;

        try {
            hub.getCosmeticManager().log(Level.INFO, "Loading registry " + this.getClass().getSimpleName() + "...");
            this.registry.register();
        } catch (Exception e) {
            hub.getCosmeticManager().log(Level.SEVERE, "Failed to load the registry " + this.getClass().getSimpleName() + "!");
            e.printStackTrace();
        }

        this.equipped = new ConcurrentHashMap<>();
    }

    public abstract void enableCosmetic(Player player, COSMETIC cosmetic, ClickType clickType, boolean login, NullType useless);

    public abstract void disableCosmetic(Player player, COSMETIC cosmetic, boolean logout, boolean replace, NullType useless);

    public abstract void update();

    public abstract boolean restrictToOne();

    public void enableCosmetic(Player player, COSMETIC cosmetic, ClickType clickType, boolean login) {
        if (cosmetic.isOwned(player)) {
            if (cosmetic.getAccessibility().canAccess(player)) {
                if (this.equipped.containsKey(player.getUniqueId()) && this.equipped.get(player.getUniqueId()).stream().anyMatch(c -> c.compareTo(cosmetic) > 0)) {
                    player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous utilisez déjà ce cosmétique.");
                } else {
                    if (this.restrictToOne())
                        this.disableCosmetics(player, false, true);

                    this.enableCosmetic(player, cosmetic, clickType, login, null);

                    if (!this.equipped.containsKey(player.getUniqueId()))
                        this.equipped.put(player.getUniqueId(), new ArrayList<>());

                    this.equipped.get(player.getUniqueId()).add(cosmetic);

                    if (this.restrictToOne())
                        this.resetCurrents(player);

                    try {
                        SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(cosmetic.getStorageId(), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    AbstractGui gui = (AbstractGui) this.hub.getGuiManager().getPlayerGui(player);

                    if (gui != null)
                        gui.update(player);
                }
            } else {
                player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous n'avez pas le grade nécessaire pour utiliser cette cosmétique.");
            }
        } else {
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous ne possédez pas ce cosmétique. Tentez de le débloquer auprès de Graou !");
        }
    }

    public void disableCosmetic(Player player, COSMETIC cosmetic, boolean logout, boolean replace) {
        if (this.equipped.containsKey(player.getUniqueId()) && this.equipped.get(player.getUniqueId()).contains(cosmetic)) {
            this.disableCosmetic(player, cosmetic, logout, replace, null);
            this.equipped.get(player.getUniqueId()).remove(cosmetic);

            if (!logout) {
                try {
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(cosmetic.getStorageId(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AbstractGui gui = (AbstractGui) this.hub.getGuiManager().getPlayerGui(player);

                if (gui != null)
                    gui.update(player);
            }
        }
    }

    public void disableCosmetics(Player player, boolean logout, boolean replace) {
        if (this.getEquippedCosmetics(player) == null)
            return;

        for (COSMETIC cosmetic : new ArrayList<>(this.getEquippedCosmetics(player)))
            this.disableCosmetic(player, cosmetic, logout, replace);
    }

    public void restoreCosmetic(Player player) {
        this.registry.getElements().keySet().stream().filter(storageId -> SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(storageId) != null).forEach(storageId ->
        {
            try {
                if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).isSelectedItem(storageId))
                    this.enableCosmetic(player, this.getRegistry().getElementByStorageId(storageId), ClickType.LEFT, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void resetCurrents(Player player) {
        this.registry.getElements().keySet().stream().filter(storageId -> SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(storageId) != null).forEach(storageId ->
        {
            try {
                SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(storageId, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public List<COSMETIC> getEquippedCosmetics(Player player) {
        if (this.equipped.containsKey(player.getUniqueId())) {
            if (!this.equipped.get(player.getUniqueId()).isEmpty())
                return this.equipped.get(player.getUniqueId());
            else
                return null;
        } else {
            return null;
        }
    }

    public AbstractCosmeticRegistry<COSMETIC> getRegistry() {
        return this.registry;
    }

    public boolean isCosmeticEquipped(Player player, AbstractCosmetic cosmetic) {
        return this.equipped.get(player.getUniqueId()).stream().anyMatch(c -> c.compareTo(cosmetic) > 0);
    }
}
