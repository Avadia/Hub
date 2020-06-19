package net.samagames.hub.npcs;

import net.samagames.tools.tutorials.TutorialChapter;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
class CoveredTutorialChapter extends TutorialChapter {
    private final int coverId;

    CoveredTutorialChapter(Location location, String title, List<Pair<String, Long>> content, int coverId) {
        super(location, title, content, true);
        this.coverId = coverId;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void teleport(Player player) {
        super.teleport(player);
        player.playEffect(player.getLocation(), Effect.RECORD_PLAY, this.coverId);
    }
}
