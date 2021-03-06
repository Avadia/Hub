package net.samagames.hub.interactions.well;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.pearls.CraftingPearl;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
class GuiWell extends AbstractGui {
    private static final ItemStack VIP_RESTRICTED_STACK;
    private static final ItemStack VIPPLUS_RESTRICTED_STACK;
    private static final ItemStack CRAFT_STACK;

    static {
        VIP_RESTRICTED_STACK = new ItemStack(Material.IRON_BARDING, 1);

        ItemMeta vipRestrictedMeta = VIP_RESTRICTED_STACK.getItemMeta();
        vipRestrictedMeta.setDisplayName(ChatColor.RED + "Emplacement indisponible");

        List<String> vipRestrictedLore = new ArrayList<>();
        vipRestrictedLore.add(ChatColor.DARK_GRAY + "Emplacement supplémentaire");
        vipRestrictedLore.add("");
        vipRestrictedLore.add(ChatColor.GRAY + "Devenez " + ChatColor.GREEN + "VIP" + ChatColor.GRAY + " pour pouvoir débloquer");
        vipRestrictedLore.add(ChatColor.GRAY + "cet emplacement de création.");

        vipRestrictedMeta.setLore(vipRestrictedLore);

        VIP_RESTRICTED_STACK.setItemMeta(vipRestrictedMeta);

        // ---

        VIPPLUS_RESTRICTED_STACK = new ItemStack(Material.IRON_BARDING, 1);

        ItemMeta vipPlusRestrictedMeta = VIPPLUS_RESTRICTED_STACK.getItemMeta();
        vipPlusRestrictedMeta.setDisplayName(ChatColor.RED + "Emplacement indisponible");

        List<String> vipPlusRestrictedLore = new ArrayList<>();
        vipPlusRestrictedLore.add(ChatColor.DARK_GRAY + "Emplacement supplémentaire");
        vipPlusRestrictedLore.add("");
        vipPlusRestrictedLore.add(ChatColor.GRAY + "Devenez " + ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+" + ChatColor.GRAY + " pour pouvoir débloquer");
        vipPlusRestrictedLore.add(ChatColor.GRAY + "cet emplacement de création.");

        vipPlusRestrictedMeta.setLore(vipPlusRestrictedLore);

        VIPPLUS_RESTRICTED_STACK.setItemMeta(vipPlusRestrictedMeta);

        // ---

        CRAFT_STACK = new ItemStack(Material.HOPPER, 1);

        ItemMeta craftMeta = CRAFT_STACK.getItemMeta();
        craftMeta.setDisplayName(ChatColor.GREEN + "Emplacement disponible");

        List<String> craftLore = new ArrayList<>();
        craftLore.add(ChatColor.DARK_GRAY + "Créer une perle");
        craftLore.add("");
        craftLore.add(ChatColor.GRAY + "Cliquez pour utiliser cet emplacement");
        craftLore.add(ChatColor.GRAY + "afin de créer une perle en utilisant");
        craftLore.add(ChatColor.GRAY + "vos " + ChatColor.AQUA + "poussières d'\u272F" + ChatColor.GRAY + ".");
        craftLore.add("");
        craftLore.add(ChatColor.DARK_GRAY + "Nécéssite : " + ChatColor.AQUA + "64 poussières d'\u272F");

        craftMeta.setLore(craftLore);

        CRAFT_STACK.setItemMeta(craftMeta);
    }

    private final Well parent;

    GuiWell(Hub hub, Well parent) {
        super(hub);

        this.parent = parent;
    }

    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 36, "Puit magique");

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.update(player);
            this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
        });
    }

    @Override
    public void update(Player player) {
        List<CraftingPearl> craftingPearls = this.hub.getInteractionManager().getWellManager().getPlayerCraftingPearls(player.getUniqueId());

        // First slot
        if (!craftingPearls.isEmpty())
            this.setSlotData(this.makeCraftingStack(craftingPearls.get(0)), 13, "crafting");
        else
            this.setSlotData(CRAFT_STACK, 13, "craft");

        // Second slot
        if (craftingPearls.size() > 1)
            this.setSlotData(this.makeCraftingStack(craftingPearls.get(1)), 12, "crafting");
        else if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "network.vip"))
            this.setSlotData(CRAFT_STACK, 12, "craft");
        else
            this.setSlotData(VIP_RESTRICTED_STACK, 12, "restricted");

        // Third slot
        if (craftingPearls.size() > 2)
            this.setSlotData(this.makeCraftingStack(craftingPearls.get(2)), 14, "crafting");
        else if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "network.vipplus"))
            this.setSlotData(CRAFT_STACK, 14, "craft");
        else
            this.setSlotData(VIPPLUS_RESTRICTED_STACK, 14, "restricted");

        this.setSlotData(getPowdersIcon(player), this.inventory.getSize() - 6, "none");
        this.setSlotData(getBackIcon(), this.inventory.getSize() - 4, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.equals("craft")) {
            if (SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getPowders() >= 64) {
                this.hub.getGuiManager().openGui(player, new GuiWellCraft(this.hub, this.parent));
            } else {
                player.sendMessage(Well.TAG + ChatColor.RED + "Vous n'avez pas assez de poussières d'\u272F !");
            }
        } else if (action.equals("back")) {
            this.hub.getGuiManager().closeGui(player);
            this.parent.stop(player);
        }
    }

    @Override
    public void onClose(Player player) {
        this.parent.stop(player);
    }

    private ItemStack makeCraftingStack(CraftingPearl craftingPearl) {
        ItemStack stack = new ItemStack(Material.SUGAR, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Création d'une perle en cours...");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Création en cours");
        lore.add("");
        lore.add(ChatColor.GRAY + "Une perle est en cours de création");
        lore.add(ChatColor.GRAY + "sur cet emplacement. Il est nécéssaire");
        lore.add(ChatColor.GRAY + "d'être patient afin de créer une");
        lore.add(ChatColor.GRAY + "perle d'une qualité parfaite.");
        lore.add("");
        lore.add(ChatColor.GRAY + "Cependant, vous pouvez réduire ce");
        lore.add(ChatColor.GRAY + "temps de création de " + ChatColor.GREEN + "15 minutes");
        lore.add(ChatColor.GRAY + "en étant " + ChatColor.GREEN + "VIP" + ChatColor.GRAY + " ou de " + ChatColor.AQUA + "30 minutes" + ChatColor.GRAY + " en");
        lore.add(ChatColor.GRAY + "étant " + ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+" + ChatColor.GRAY + ".");
        lore.add("");

        if (craftingPearl.getCreationInMinutes() > 1)
            lore.add(ChatColor.DARK_GRAY + "Temps restant : " + ChatColor.WHITE + craftingPearl.getCreationInMinutes() + " minute" + (craftingPearl.getCreationInMinutes() > 1 ? "s" : ""));
        else
            lore.add(ChatColor.DARK_GRAY + "Temps restant : " + ChatColor.WHITE + "Moins d'une minute");

        meta.setLore(lore);

        stack.setItemMeta(meta);

        return stack;
    }
}
