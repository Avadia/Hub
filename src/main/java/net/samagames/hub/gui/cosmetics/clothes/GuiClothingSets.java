package net.samagames.hub.gui.cosmetics.clothes;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.clothes.ClothRegistry;
import net.samagames.hub.cosmetics.clothes.ClothingSet;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.cosmetics.GuiCosmetics;
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
public class GuiClothingSets extends AbstractGui {
    public GuiClothingSets(Hub hub) {
        super(hub);
    }

    @Override
    public void display(Player player) {
        int lines = 1;
        int slot = 0;

        for (ClothingSet ignored : ((ClothRegistry) this.hub.getCosmeticManager().getClothManager().getRegistry()).getClothingSets().values()) {
            slot++;

            if (slot == 8) {
                slot = 0;
                lines++;
            }
        }

        this.inventory = this.hub.getServer().createInventory(null, 9 + (lines * 9) + (9 * 2), "Ensembles");

        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player) {
        int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        int lines = 0;
        int slot = 0;

        for (ClothingSet set : ((ClothRegistry) this.hub.getCosmeticManager().getClothManager().getRegistry()).getClothingSets().values()) {
            this.setSlotData(set.getIcon(player), (baseSlots[slot] + (lines * 9)), "set_" + set.getStorageId());

            slot++;

            if (slot == 7) {
                slot = 0;
                lines++;
            }
        }

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 4, "back");
        this.setSlotData(ChatColor.RED + "Désactiver vos cosmétiques actuels", Material.FLINT_AND_STEEL, this.inventory.getSize() - 6, null, "delete");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.startsWith("set_")) {
            int setStorageId = Integer.parseInt(action.split("_")[1]);
            this.hub.getGuiManager().openGui(player, new GuiClothingSet(this.hub, ((ClothRegistry) this.hub.getCosmeticManager().getClothManager().getRegistry()).getClothingSetByStorageId(setStorageId)));
        } else if (action.equals("delete")) {
            this.hub.getCosmeticManager().getClothManager().disableCosmetics(player, false, false);
        } else if (action.equals("back")) {
            this.hub.getGuiManager().openGui(player, new GuiCosmetics(this.hub));
        }
    }
}
