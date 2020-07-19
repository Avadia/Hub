package net.samagames.hub.interactions.bumper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;

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
public class BumperManager extends AbstractInteractionManager<Bumper> {
    public BumperManager(Hub hub) {
        super(hub, "bumpers");
    }

    @Override
    public void loadConfiguration(JsonArray rootJson) {
        if (rootJson.size() == 0)
            return;

        for (int i = 0; i < rootJson.size(); i++) {
            JsonObject bumperJson = rootJson.get(i).getAsJsonObject();

            Location bumperLocation = LocationUtils.str2loc(bumperJson.get("location").getAsString());
            double power = bumperJson.get("power").getAsDouble();

            Bumper bumper = new Bumper(this.hub, bumperLocation, power);

            this.interactions.add(bumper);
            this.log(Level.INFO, "Registered bumper at '" + LocationUtils.loc2str(bumperLocation));
        }
    }
}
