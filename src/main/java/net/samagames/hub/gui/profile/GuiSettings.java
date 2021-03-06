package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.settings.IPlayerSettings;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
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
class GuiSettings extends AbstractGui {
    private final List<GuiSettingsCallback> callbackList;
    private final int page;
    private int index;

    GuiSettings(Hub hub, int page) {
        super(hub);

        this.callbackList = new ArrayList<>();
        this.page = page;
        this.index = 0;
    }

    @Override
    public void display(Player player) {
        this.inventory = this.hub.getServer().createInventory(null, 45, "Paramètres (Page " + this.page + ")");

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.update(player);
            this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
        });
    }

    @Override
    public void update(Player player) {
        this.index = 0;
        this.callbackList.clear();

        if (this.page == 1) {
            this.drawSetting(player, "Joueurs", new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal()), 10, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, vous verrez",
                    ChatColor.GRAY + "les joueurs autour de vous dans le hub. Dans le cas",
                    ChatColor.GRAY + "contraire, vous verrez seulement le " + ChatColor.GOLD + "Staff" + ChatColor.GRAY + ",",
                    ChatColor.GRAY + "les " + ChatColor.GOLD + "Coupaings" + ChatColor.GRAY + " et vos " + ChatColor.GOLD + "amis" + ChatColor.GRAY + "."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isPlayerVisible();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    boolean value = !this.get(setting);
                    setting.setPlayerVisible(value);
                    updateSettings(setting);

                    if (value)
                        GuiSettings.this.hub.getPlayerManager().removeHider(player);
                    else
                        GuiSettings.this.hub.getPlayerManager().addHider(player);
                }
            });

            this.drawSetting(player, "Chat", new ItemStack(Material.BOOK_AND_QUILL, 1), 11, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, vous pourrez",
                    ChatColor.GRAY + "voir les messages des joueurs dans le chat."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isChatVisible();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    boolean value = !this.get(setting);
                    setting.setChatVisible(value);
                    updateSettings(setting);

                    if (value)
                        GuiSettings.this.hub.getChatManager().enableChatFor(player);
                    else
                        GuiSettings.this.hub.getChatManager().disableChatFor(player);
                }
            });

            this.drawSetting(player, "Messages privés", new ItemStack(Material.PAPER, 1), 12, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, vous pourrez",
                    ChatColor.GRAY + "recevoir des messages privés de la part des",
                    ChatColor.GRAY + "joueurs. Vos amis pourront quand même vous",
                    ChatColor.GRAY + "envoyer des messages."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isPrivateMessageReceive();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    setting.setPrivateMessageReceive(!this.get(setting));
                    updateSettings(setting);
                }
            });

            ItemStack map = new ItemStack(Material.MAP, 1);
            ItemMeta itemMeta = map.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            map.setItemMeta(itemMeta);
            this.drawSetting(player, "Notifications", map, 13, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, vous pourrez",
                    ChatColor.GRAY + "recevoir un signal sonore lorsqu'un joueur",
                    ChatColor.GRAY + "écrit votre nom dans le chat.",
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isNotificationReceive();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    setting.setNotificationReceive(!this.get(setting));
                    updateSettings(setting);
                }
            });

            this.drawSetting(player, "Demandes d'amis", new ItemStack(Material.RAW_FISH, 1, (short) 3), 14, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, les joueurs",
                    ChatColor.GRAY + "pourront vous envoyer des demandes d'amis."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isFriendshipDemandReceive();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    setting.setFriendshipDemandReceive(!this.get(setting));
                    updateSettings(setting);
                }
            });

            this.drawSetting(player, "Demandes de groupe", new ItemStack(Material.LEASH, 1), 15, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, les joueurs",
                    ChatColor.GRAY + "pourront vous envoyer des demandes de groupe.",
                    ChatColor.GRAY + "Vos amis pourront quand même vous inviter",
                    ChatColor.GRAY + "dans un groupe."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isGroupDemandReceive();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    setting.setGroupDemandReceive(!this.get(setting));
                    updateSettings(setting);
                }
            });

            this.drawSetting(player, "Jukebox", new ItemStack(Material.JUKEBOX, 1), 16, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, vous",
                    ChatColor.GRAY + "entenderez la musique du Jukebox dans",
                    ChatColor.GRAY + "les hubs."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isJukeboxListen();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    boolean value = !this.get(setting);
                    setting.setJukeboxListen(value);
                    updateSettings(setting);
                    GuiSettings.this.hub.getCosmeticManager().getJukeboxManager().mute(player, !value);
                }
            });
        } else if (this.page == 2) {
            this.drawSetting(player, "Intéractions", new ItemStack(Material.COOKIE, 1), 10, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, les",
                    ChatColor.GRAY + "intéractions seront possibles avec votre",
                    ChatColor.GRAY + "joueur, comme par exemple celles avec les",
                    ChatColor.GRAY + "gadgets. Seuls vos " + ChatColor.GOLD + "amis" + ChatColor.GRAY + " pourront tout",
                    ChatColor.GRAY + "de même intéragir avec vous."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isNotificationReceive();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    setting.setNotificationReceive(!this.get(setting));
                    updateSettings(setting);
                }
            });

            this.drawSetting(player, "Notifications de file d'attente", new ItemStack(Material.SIGN, 1), 11, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, vous",
                    ChatColor.GRAY + "recevrez des informations sur votre",
                    ChatColor.GRAY + "statut dans les files d'attente."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isWaitingLineNotification();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    setting.setWaitingLineNotification(!this.get(setting));
                    updateSettings(setting);
                }
            });

            this.drawSetting(player, "Ailes", new ItemStack(Material.ELYTRA, 1), 12, new String[]{
                    ChatColor.GRAY + "Quand cette option est activée, vous",
                    ChatColor.GRAY + "serez équipé par défaut d'une paire",
                    ChatColor.GRAY + "d'ailes quand vous entrerez dans un",
                    ChatColor.GRAY + "Hub.",
                    "",
                    ChatColor.RED + "Cette option est disponible uniquement",
                    ChatColor.RED + "pour les versions supérieures à la 1.9."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isElytraActivated();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    setting.setElytraActivated(!this.get(setting));
                    updateSettings(setting);
                    GuiSettings.this.hub.getPlayerManager().getStaticInventory().setInventoryToPlayer(player);
                }
            });

            this.drawSetting(player, "ClickMe", new ItemStack(Material.WOOD_BUTTON, 1), 16, new String[]{
                    ChatColor.GRAY + "En cliquant, vous accéderez à un menu",
                    ChatColor.GRAY + "avec les différents paramètres du ClickMe.",
                    ChatColor.GRAY + "Avec celui-ci vous pourrez accéder aux",
                    ChatColor.GRAY + "statistiques des joueurs en faisant un",
                    ChatColor.GRAY + "clic-gauche sur ceux-ci."
            }, new GuiSettingsCallback() {
                @Override
                public boolean get(IPlayerSettings setting) {
                    return setting.isClickOnMeActivation();
                }

                @Override
                public void invert(IPlayerSettings setting) {
                    setting.setClickOnMeActivation(!this.get(setting));
                    updateSettings(setting);
                }
            });
        }

        if (this.page > 1)
            this.setSlotData(ChatColor.YELLOW + "« Page " + (this.page - 1), Material.PAPER, this.inventory.getSize() - 9, null, "page_back");

        if (this.page < 2)
            this.setSlotData(ChatColor.YELLOW + "Page " + (this.page + 1) + " »", Material.PAPER, this.inventory.getSize() - 1, null, "page_next");

        this.setSlotData(getBackIcon(), 40, "back");
    }

    private void updateSettings(IPlayerSettings setting) {
        this.hub.getServer().getScheduler().runTaskAsynchronously(hub, setting::update);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType) {
        if (action.startsWith("setting_")) {
            if (this.getClass().equals(GuiSettings.class) && (this.getSlot(action) == 16 || this.getSlot(action) == 25) && stack.getType() == Material.WOOD_BUTTON) {
                this.hub.getGuiManager().openGui(player, new GuiClickMeSettings(this.hub));
                return;
            }

            GuiSettingsCallback callback = this.callbackList.get(Integer.parseInt(action.replace("setting_", "").toUpperCase()));
            IPlayerSettings settings = SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId());
            if (callback != null)
                callback.invert(settings);
            this.update(player);
        } else if (action.equals("page_back")) {
            this.hub.getGuiManager().openGui(player, new GuiSettings(this.hub, this.page - 1));
        } else if (action.equals("page_next")) {
            this.hub.getGuiManager().openGui(player, new GuiSettings(this.hub, this.page + 1));
        } else if (action.equals("back")) {
            this.hub.getGuiManager().openGui(player, new GuiProfile(this.hub));
        }
    }

    @SuppressWarnings("deprecation")
    protected void drawSetting(Player player, String displayName, ItemStack icon, int slot, String[] description, GuiSettingsCallback callback) {
        IPlayerSettings settings = SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId());
        boolean enabled = callback.get(settings);
        ChatColor titleColor = enabled ? ChatColor.GREEN : ChatColor.RED;
        ItemStack glassBlock = new ItemStack(Material.STAINED_GLASS, 1, enabled ? DyeColor.GREEN.getWoolData() : DyeColor.RED.getWoolData());

        this.setSlotData(titleColor + displayName, icon, slot, description, "setting_" + this.index);
        this.setSlotData(titleColor + (enabled ? "Activé" : "Désactivé"), glassBlock, slot + 9, null, "setting_" + this.index);
        this.callbackList.add(callback);
        this.index++;
    }

    public interface GuiSettingsCallback {
        boolean get(IPlayerSettings setting);

        void invert(IPlayerSettings setting);
    }
}
