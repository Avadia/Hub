package net.samagames.hub.gui.cosmetics.clothes;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.clothes.ClothingSet;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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
class GuiClothingSet extends AbstractGui {
    private final ClothingSet set;

    GuiClothingSet(Hub hub, ClothingSet set) {
        super(hub);

        this.set = set;
    }

    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 54, this.set.getName());

        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player) {
        this.setSlotData(this.set.getSet()[0].getIcon(player), 13, "cosmetic_" + this.set.getSet()[0].getStorageId());
        this.setSlotData(this.set.getSet()[1].getIcon(player), 22, "cosmetic_" + this.set.getSet()[1].getStorageId());
        this.setSlotData(this.set.getSet()[2].getIcon(player), 31, "cosmetic_" + this.set.getSet()[2].getStorageId());
        this.setSlotData(this.set.getSet()[3].getIcon(player), 40, "cosmetic_" + this.set.getSet()[3].getStorageId());

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 4, "back");
        this.setSlotData(ChatColor.RED + "Désactiver vos cosmétiques actuels", Material.FLINT_AND_STEEL, this.inventory.getSize() - 6, null, "delete");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.startsWith("cosmetic_")) {
            int cosmetic = Integer.parseInt(action.split("_")[1]);
            this.hub.getCosmeticManager().getClothManager().enableCosmetic(player, this.hub.getCosmeticManager().getClothManager().getRegistry().getElementByStorageId(cosmetic), clickType, false);
        } else if (action.equals("delete")) {
            this.hub.getCosmeticManager().getClothManager().disableCosmetics(player, false, false);
        } else if (action.equals("back")) {
            this.hub.getGuiManager().openGui(player, new GuiClothingSets(this.hub));
        }
    }
}
