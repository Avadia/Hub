package net.samagames.hub.gui.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
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
public class GuiCosmetics extends AbstractGui {
    private final List<Integer> slots;
    private int slot;

    public GuiCosmetics(Hub hub) {
        super(hub);

        this.slots = Arrays.asList(2, 2, 3, 3, 3, 4, 4);
        this.slot = 0;

        Collections.shuffle(this.slots);
    }

    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Caverne aux trésors");

        this.randomIcon(0, ChatColor.DARK_RED + "❤" + ChatColor.RED + " Ensembles " + ChatColor.DARK_RED + "❤", Material.LEATHER_CHESTPLATE, new String[]{
                ChatColor.GOLD + "Prochainement..."
                //ChatColor.GRAY + "Tout le monde de la mode est entre vos",
                //ChatColor.GRAY + "mains !"
        }, DyeColor.RED, "clothes");

        this.randomIcon(1, ChatColor.GOLD + "●" + ChatColor.YELLOW + " Humeurs " + ChatColor.GOLD + "●", Material.BLAZE_POWDER, new String[]{
                ChatColor.GOLD + "Prochainement..."
                //ChatColor.GRAY + "Montrez-nous comment vous vous sentez !"
        }, DyeColor.YELLOW, "particles");

        this.randomIcon(2, ChatColor.DARK_GREEN + "▲" + ChatColor.GREEN + " Montures " + ChatColor.DARK_GREEN + "▲", Material.SADDLE, new String[]{
                ChatColor.GOLD + "Prochainement..."
                //ChatColor.GRAY + "Un fidèle compagnon qui vous suit",
                //ChatColor.GRAY + "où que vous soyez !"
        }, DyeColor.GREEN, "pets");

        this.randomIcon(4, ChatColor.GRAY + "◢" + ChatColor.WHITE + " Jukebox " + ChatColor.GRAY + "◣", Material.JUKEBOX, new String[]{
                ChatColor.GOLD + "Prochainement..."
                //ChatColor.GRAY + "Devenez un véritable SamaDJ !"
        }, DyeColor.WHITE, "jukebox");

        this.randomIcon(6, ChatColor.DARK_AQUA + "◼" + ChatColor.AQUA + " Déguisements " + ChatColor.DARK_AQUA + "◼", Material.SKULL_ITEM, new String[]{
                ChatColor.GOLD + "Prochainement..."
                //ChatColor.GRAY + "Entrez dans la peau d'un autre",
                //ChatColor.GRAY + "personnage !"
        }, DyeColor.LIGHT_BLUE, "disguises");

        this.randomIcon(7, ChatColor.DARK_BLUE + "★" + ChatColor.BLUE + " Gadgets " + ChatColor.DARK_BLUE + "★", Material.FIREWORK, new String[]{
                ChatColor.GOLD + "Prochainement..."
                //ChatColor.GRAY + "Animez le serveur en exposant vos",
                //ChatColor.GRAY + "nombreux gadgets venant du futur !"
        }, DyeColor.BLUE, "gadgets");

        this.randomIcon(8, ChatColor.DARK_PURPLE + "▼" + ChatColor.LIGHT_PURPLE + " Ballons " + ChatColor.DARK_PURPLE + "▼", Material.CLAY_BALL, new String[]{
                ChatColor.GOLD + "Prochainement..."
                //ChatColor.GRAY + "Des petits ballons au dessus de votre tête !"
        }, DyeColor.PURPLE, "balloons");

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (action) {
            //TODO Cosmétiques
//            case "clothes":
//                this.hub.getGuiManager().openGui(player, new GuiClothingSets(this.hub));
//                break;
//
//            case "particles":
//                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Particules", this.hub.getCosmeticManager().getParticleManager(), true));
//                break;

            //IL FAUT FAIRE LE SYSTEME DE PETS JE CROIS
//            case "pets":
//                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Montures", this.hub.getCosmeticManager().getPetManager(), true));
//                break;

//            case "jukebox":
//                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Jukebox", this.hub.getCosmeticManager().getJukeboxManager(), false));
//                break;
//
//            case "disguises":
//                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Déguisements", this.hub.getCosmeticManager().getDisguiseManager(), true));
//                break;
//
//            case "gadgets":
//                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Gadgets", this.hub.getCosmeticManager().getGadgetManager(), true));
//                break;
//
//            case "balloons":
//                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Ballons", this.hub.getCosmeticManager().getBalloonManager(), true));
//                break;

            case "back":
                this.hub.getGuiManager().closeGui(player);
                break;
        }
    }

    @Override
    public void onClose(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.0F);
    }

    private void randomIcon(int base, String displayName, Material material, String[] description, DyeColor color, String action) {
        int selected = base + this.slots.get(this.slot) * 9;

        this.setSlotData(displayName, material, selected, description, action);
        this.drawLineOfGlass(selected, color, action);

        this.slot++;
    }

    @SuppressWarnings("deprecation")
    private void drawLineOfGlass(int baseSlot, DyeColor color, String action) {
        int slot = baseSlot - 9;

        while (slot >= 0) {
            if (this.inventory.getItem(slot) == null)
                this.setSlotData(ChatColor.GRAY + "", new ItemStack(Material.STAINED_GLASS_PANE, 1, color.getWoolData()), slot, null, action);

            slot -= 9;
        }
    }
}
