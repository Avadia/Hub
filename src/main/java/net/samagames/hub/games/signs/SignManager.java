package net.samagames.hub.games.signs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.utils.RestrictedVersion;
import net.samagames.tools.JsonConfiguration;
import net.samagames.tools.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
public class SignManager extends AbstractManager {
    private final JsonConfiguration jsonConfig;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public SignManager(Hub hub) {
        super(hub);

        File config = new File(this.hub.getDataFolder(), "signs.json");

        if (!config.exists()) {
            try {
                config.createNewFile();

                FileOutputStream fileOutputStream = new FileOutputStream(config);
                OutputStreamWriter fw = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("{\"zones\":[]}");
                bw.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.jsonConfig = new JsonConfiguration(config);
        this.reloadList();
    }

    @Override
    public void onDisable() { /* Not needed **/}

    @Override
    public void onLogin(Player player) { /* Not needed **/}

    @Override
    public void onLogout(Player player) { /* Not needed **/}

    public void reloadList() {
        this.log(Level.INFO, "Reloading game sign list...");

        this.hub.getGameManager().getGames().values().forEach(net.samagames.hub.games.AbstractGame::clearSigns);

        JsonArray signZonesArray = this.jsonConfig.load().getAsJsonArray("zones");

        for (int i = 0; i < signZonesArray.size(); i++) {
            JsonObject signZoneObject = signZonesArray.get(i).getAsJsonObject();

            String game = signZoneObject.get("game").getAsString();

            AbstractGame gameObject = this.hub.getGameManager().getGameByIdentifier(game);

            if (gameObject == null) {
                this.log(Level.SEVERE, "Wanted to register a game sign withing an unknown game!");
                continue;
            }

            JsonArray maps = signZoneObject.get("maps").getAsJsonArray();

            for (int j = 0; j < maps.size(); j++) {
                JsonObject mapObject = maps.get(j).getAsJsonObject();
                String map = mapObject.get("map").getAsString().replaceAll(" ", "_");
                String template = mapObject.get("template").getAsString();
                ChatColor color = ChatColor.valueOf(mapObject.get("color").getAsString());
                Location sign = LocationUtils.str2loc(mapObject.get("sign").getAsString());

                RestrictedVersion restrictedVersion = null;

                if (mapObject.has("restricted-version")) {
                    try {
                        restrictedVersion = RestrictedVersion.parse(mapObject.get("restricted-version").getAsString());
                    } catch (Exception e) {
                        this.log(Level.SEVERE, "Wanted to register a game sign with an invalid restricted version! (" + e.getMessage() + ")");
                        continue;
                    }
                }

                Block block = this.hub.getWorld().getBlockAt(sign);

                if (!(block.getState() instanceof Sign)) {
                    this.log(Level.SEVERE, "Sign block for game '" + game + "' and map '" + map + "' is not a sign in the world!");
                    continue;
                }

                gameObject.addSignForMap(map, color, template, restrictedVersion, (Sign) block.getState());

                this.log(Level.INFO, "Registered sign zone for the game '" + game + "' and the map '" + map + "'!");
            }

            if (signZoneObject.has("title")) {
                Location sign = LocationUtils.str2loc(signZoneObject.get("title").getAsString());

                Block block = this.hub.getWorld().getBlockAt(sign);

                if (!(block.getState() instanceof Sign)) {
                    this.log(Level.SEVERE, "Sign block for game '" + game + "' is not a sign in the world!");
                    continue;
                }

                gameObject.setSignForGame((Sign) block.getState());

                this.log(Level.INFO, "Registered sign zone for the game '" + game + "'!");
            }
        }

        this.log(Level.INFO, "Reloaded game sign list.");
    }

    public void setSignForGame(String game, Sign sign) {
        JsonObject root = this.jsonConfig.load();
        JsonArray signZonesArray = root.getAsJsonArray("zones");

        for (int i = 0; i < signZonesArray.size(); i++) {
            JsonObject signZoneObject = signZonesArray.get(i).getAsJsonObject();

            if (signZoneObject.get("game").getAsString().equals(game)) {
                signZoneObject.addProperty("title", LocationUtils.loc2str(sign.getLocation()));

                this.jsonConfig.save(root);
                this.reloadList();

                return;
            }
        }

        JsonObject signZoneObject = new JsonObject();
        signZoneObject.addProperty("game", game);

        signZoneObject.add("maps", new JsonObject().getAsJsonArray());

        signZoneObject.addProperty("title", LocationUtils.loc2str(sign.getLocation()));

        signZonesArray.add(signZoneObject);

        this.jsonConfig.save(root);
        this.reloadList();
    }

    public void setSignForMap(String game, String map, ChatColor color, String template, Sign sign) {
        JsonObject root = this.jsonConfig.load();
        JsonArray signZonesArray = root.getAsJsonArray("zones");

        for (int i = 0; i < signZonesArray.size(); i++) {
            JsonObject signZoneObject = signZonesArray.get(i).getAsJsonObject();

            if (signZoneObject.get("game").getAsString().equals(game)) {
                JsonArray maps = signZoneObject.get("maps").getAsJsonArray();

                JsonObject mapObject = new JsonObject();
                mapObject.addProperty("map", map);
                mapObject.addProperty("template", template);
                mapObject.addProperty("color", color.name());
                mapObject.addProperty("sign", LocationUtils.loc2str(sign.getLocation()));
                maps.add(mapObject);

                this.jsonConfig.save(root);
                this.reloadList();

                return;
            }
        }

        JsonObject signZoneObject = new JsonObject();
        signZoneObject.addProperty("game", game);

        JsonArray maps = new JsonArray();

        JsonObject mapObject = new JsonObject();
        mapObject.addProperty("map", map);
        mapObject.addProperty("template", template);
        mapObject.addProperty("color", color.name());
        mapObject.addProperty("sign", LocationUtils.loc2str(sign.getLocation()));
        maps.add(mapObject);

        signZoneObject.add("maps", maps);
        signZonesArray.add(signZoneObject);

        this.jsonConfig.save(root);
        this.reloadList();
    }
}
