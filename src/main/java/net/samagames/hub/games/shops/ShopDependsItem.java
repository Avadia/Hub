package net.samagames.hub.games.shops;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.shop.GuiConfirm;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
public class ShopDependsItem extends ShopItem {
    private final ShopItem dependsOn;

    public ShopDependsItem(Hub hub, String categoryName, int storageId, int slot, int[] resetIds, ShopItem dependsOn) throws Exception {
        super(hub, categoryName, storageId, slot, resetIds);

        this.dependsOn = dependsOn;
    }

    @Override
    public void execute(Player player, ClickType clickType) {
        if (this.isActive(player)) {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Cet objet est déjà équipé.");
        } else if (this.isOwned(player) || this.isDefaultItem()) {
            this.resetCurrents(player);

            try {
                if (SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(this.storageId) == null)
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).addItem(this.storageId, 0, 0, true);
                else
                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).setSelectedItem(this.storageId, true);

                player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Une erreur s'est produite. Veuillez réessayer ultérieurement.");

                return;
            }
        } else if (!CosmeticAccessibility.valueOf(this.itemDescription.getRankAccessibility()).canAccess(player)) {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous n'avez pas le grade nécessaire pour acheter cet objet.");
        } else if (this.dependsOn != null && !hasDepend(player)) {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Il est nécessaire de posséder " + ChatColor.AQUA + this.dependsOn.getIcon().getItemMeta().getDisplayName() + ChatColor.RED + " pour acheter cela.");
        } else if (!SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).hasEnoughCoins(this.itemDescription.getPriceCoins())) {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Vous n'avez pas assez de pièces pour acheter cela.");
        } else {
            GuiConfirm confirm = new GuiConfirm(this.hub, (AbstractGui) this.hub.getGuiManager().getPlayerGui(player), (parent) ->
            {
                if (this.isActive(player))
                    return;

                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).withdrawCoins(this.itemDescription.getPriceCoins(), (newAmount, difference, error) ->
                {
                    this.resetCurrents(player);

                    SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).addItem(this.storageId, this.itemDescription.getPriceCoins(), 0, true, (aBoolean, throwable) ->
                    {
                        player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.GREEN + "Vous avez acheté et équipé " + ChatColor.AQUA + this.getIcon().getItemMeta().getDisplayName());

                        this.hub.getScoreboardManager().update(player);
                        this.hub.getGuiManager().openGui(player, parent);
                    });
                });

                this.hub.getGuiManager().getPlayerGui(player).update(player);
                this.hub.getGuiManager().openGui(player, parent);
            });

            this.hub.getGuiManager().openGui(player, confirm);
            return;
        }

        this.hub.getGuiManager().getPlayerGui(player).update(player);
    }

    @Override
    public ItemStack getFormattedIcon(Player player) {
        ItemStack stack = getIcon().clone();
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

        if (CosmeticAccessibility.valueOf(this.itemDescription.getRankAccessibility()) != CosmeticAccessibility.ALL) {
            lore.add("");
            lore.add(ChatColor.GRAY + "Vous devez posséder le grade");
            lore.add(CosmeticAccessibility.valueOf(this.itemDescription.getRankAccessibility()).getDisplay() + ChatColor.GRAY + " pour pouvoir utiliser cet");
            lore.add(ChatColor.GRAY + "objet.");
        }

        lore.add("");

        if (this.isActive(player))
            lore.add(ChatColor.GREEN + "Objet actif");
        else if (this.isOwned(player) || this.isDefaultItem())
            lore.add(ChatColor.GREEN + "Objet possédé");
        else if (this.dependsOn == null || this.hasDepend(player))
            lore.add(ChatColor.GRAY + "Prix : " + ChatColor.GOLD + this.itemDescription.getPriceCoins() + " pièces");
        else
            lore.add(ChatColor.RED + "Nécessite " + ChatColor.AQUA + this.dependsOn.getIcon().getItemMeta().getDisplayName());

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    public ShopDependsItem defaultItem() {
        super.defaultItem();
        return this;
    }

    private boolean hasDepend(Player player) {
        return this.dependsOn.isDefaultItem() || SamaGamesAPI.get().getShopsManager().getPlayer(player.getUniqueId()).getTransactionsByID(this.dependsOn.getStorageId()) != null;
    }
}
