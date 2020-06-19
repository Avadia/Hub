package net.samagames.hub.gui.shop;

import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

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
public class GuiConfirm extends AbstractGui {
    private static final ItemStack WAITING_STACK;

    static {
        //noinspection deprecation
        WAITING_STACK = new ItemStack(Material.STAINED_GLASS, 1, DyeColor.YELLOW.getWoolData());

        ItemMeta meta = WAITING_STACK.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Veuillez patienter...");

        WAITING_STACK.setItemMeta(meta);
    }

    private final AbstractGui parent;
    private final ConfirmCallback callback;
    private BukkitTask waitingTask;
    private int waitingSlot;
    private boolean isInProgress;

    public GuiConfirm(Hub hub, AbstractGui parent, ConfirmCallback callback) {
        super(hub);

        this.parent = parent;
        this.callback = callback;
        this.waitingTask = null;
        this.waitingSlot = 11;
        this.isInProgress = false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 27, "Confirmer l'achat ?");

        this.setSlotData(ChatColor.GREEN + "Oui", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.GREEN.getWoolData()), 11, null, "confirm");
        this.setSlotData(ChatColor.RED + "Annuler", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.RED.getWoolData()), 15, null, "cancel");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClose(Player player) {
        if (this.waitingTask != null)
            this.waitingTask.cancel();
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action) {
        if (action.equals("confirm") && !this.isInProgress) {
            this.waitingTask = this.hub.getServer().getScheduler().runTaskTimer(this.hub, () ->
            {
                this.inventory.clear();
                this.setSlotData(this.inventory, WAITING_STACK, this.waitingSlot, "none");

                this.waitingSlot++;

                if (this.waitingSlot > 15)
                    this.waitingSlot = 11;
            }, 10L, 10L);

            this.hub.getExecutorMonoThread().execute(() ->
            {
                this.isInProgress = true;
                this.callback.run(this.parent);
            });
        } else if (action.equals("cancel") && !this.isInProgress) {
            this.hub.getGuiManager().openGui(player, this.parent);
        } else {
            player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Merci de patienter pendant le traitement de votre commande...");
        }
    }

    @FunctionalInterface
    public interface ConfirmCallback {
        void run(AbstractGui parent);
    }
}
