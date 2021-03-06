package net.samagames.hub.common.refresh;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.permissions.IPermissionsEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;

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
public class JsonHub {
    private final HashMap<String, Integer> playersDetails;
    private int hubNumber;
    private int connectedPlayers;

    public JsonHub(int hubNumber, int connectedPlayers) {
        this.hubNumber = hubNumber;
        this.connectedPlayers = connectedPlayers;
        this.playersDetails = new HashMap<>();
    }

    public JsonHub() {
        this.playersDetails = new HashMap<>();
    }

    public void addConnectedPlayer(Player player) {
        IPermissionsEntity user = SamaGamesAPI.get().getPermissionsManager().getPlayer(player.getUniqueId());
        String display = user.getDisplayTag();

        if (display.length() < 5)
            display += "Joueurs";

        if (this.playersDetails.containsKey(display))
            this.playersDetails.put(display, this.playersDetails.get(display) + 1);
        else
            this.playersDetails.put(display, 1);
    }

    public int getHubNumber() {
        return this.hubNumber;
    }

    public void setHubNumber(int hubNumber) {
        this.hubNumber = hubNumber;
    }

    public int getConnectedPlayers() {
        return this.connectedPlayers;
    }

    public void setConnectedPlayers(int connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }

    public HashMap<String, Integer> getPlayersDetails() {
        return this.playersDetails;
    }
}
