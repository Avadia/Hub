package net.samagames.hub.common.players;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxSong;
import net.samagames.tools.PlayerUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

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
public class ChatManager extends AbstractManager {
    private final Map<UUID, Date> lastPlayerMessages;
    private final List<UUID> chatDisablers;

    private int actualSlowDuration;
    private boolean chatLocked;

    public ChatManager(Hub hub) {
        super(hub);

        this.lastPlayerMessages = new HashMap<>();
        this.chatDisablers = new ArrayList<>();

        this.actualSlowDuration = 0;
        this.chatLocked = false;
    }

    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;

        if (this.chatLocked) {
            if (SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.bypassmute")) {
                event.getPlayer().sendMessage(PlayerManager.MODERATING_TAG + ChatColor.YELLOW + "Attention, le chat a été désactivé par un modérateur.");
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(PlayerManager.MODERATING_TAG + ChatColor.RED + "Le chat a été désactivé par un modérateur.");

                return;
            }
        } else if (this.actualSlowDuration > 0 && !SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.bypassmute")) {
            if (!this.hasPlayerTalked(event.getPlayer())) {
                this.actualizePlayerLastMessage(event.getPlayer());
            } else {
                Date lastMessage = this.getLastPlayerMessageDate(event.getPlayer());
                Date actualMessage = new Date(Objects.requireNonNull(lastMessage).getTime() + this.getActualSlowDuration() * 1000);
                Date current = new Date();

                if (actualMessage.after(current)) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "Le chat est actuellement ralenti.");

                    double whenNext = Math.floor((actualMessage.getTime() - current.getTime()) / 1000.0);
                    event.getPlayer().sendMessage(ChatColor.GOLD + "Prochain message autorisé dans : " + (int) whenNext + " secondes");

                    return;
                } else {
                    this.actualizePlayerLastMessage(event.getPlayer());
                }
            }
        }

        JukeboxSong current = this.hub.getCosmeticManager().getJukeboxManager().getCurrentSong();

        if (current != null && current.getPlayedBy().equals(event.getPlayer().getName()))
            event.setFormat(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "DJ" + ChatColor.DARK_AQUA + "]" + event.getFormat());

        List<Player> receivers = new ArrayList<>(event.getRecipients());
        receivers.stream().filter(this::hasChatDisabled).forEach(player -> event.getRecipients().remove(player));

        this.hub.getServer().getOnlinePlayers().stream().filter(player -> StringUtils.containsIgnoreCase(event.getMessage(), player.getName())).filter(player -> !this.hasChatDisabled(player)).forEach(player ->
        {
            event.getRecipients().remove(player);

            if (SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isNotificationReceive())
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0F, 1.0F);

            String suffixRaw = SamaGamesAPI.get().getPermissionsManager().getSuffix(SamaGamesAPI.get().getPermissionsManager().getPlayer(event.getPlayer().getUniqueId()));
            ChatColor suffix = ChatColor.getByChar(suffixRaw.charAt(1));

            player.sendMessage(PlayerUtils.getFullyFormattedPlayerName(event.getPlayer()) + suffix + ": " + event.getMessage().replaceAll("(?i)" + player.getName(), ChatColor.GOLD + player.getName() + suffix));
        });
    }

    @Override
    public void onDisable() {
        this.lastPlayerMessages.clear();
        this.chatDisablers.clear();
    }

    @Override
    public void onLogin(Player player) {
        boolean chatOnSetting = SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isChatVisible();

        if (!chatOnSetting) {
            this.disableChatFor(player);
            player.sendMessage(PlayerManager.SETTINGS_TAG + "Vous avez désactivé le chat. Pour le réactiver, rendez-vous dans vos paramêtres en cliquant sur votre tête.");
        }
    }

    @Override
    public void onLogout(Player player) {
        if (this.hasChatDisabled(player))
            this.enableChatFor(player);
    }

    public void enableChatFor(Player player) {
        this.chatDisablers.remove(player.getUniqueId());
    }

    public void disableChatFor(Player player) {
        if (!this.chatDisablers.contains(player.getUniqueId()))
            this.chatDisablers.add(player.getUniqueId());
    }

    public int getActualSlowDuration() {
        return this.actualSlowDuration;
    }

    public void setActualSlowDuration(int time) {
        this.actualSlowDuration = time;
    }

    public boolean isChatLocked() {
        return this.chatLocked;
    }

    public void setChatLocked(boolean flag) {
        this.chatLocked = flag;
    }

    private void actualizePlayerLastMessage(Player player) {
        this.lastPlayerMessages.put(player.getUniqueId(), new Date());
    }

    private Date getLastPlayerMessageDate(Player player) {
        return this.lastPlayerMessages.getOrDefault(player.getUniqueId(), null);
    }

    private boolean hasPlayerTalked(Player player) {
        return this.lastPlayerMessages.containsKey(player.getUniqueId());
    }

    private boolean hasChatDisabled(Player player) {
        return this.chatDisablers.contains(player.getUniqueId());
    }
}
