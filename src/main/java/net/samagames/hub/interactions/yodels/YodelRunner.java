package net.samagames.hub.interactions.yodels;

import net.samagames.hub.Hub;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

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
class YodelRunner extends BukkitRunnable {
    private static final double SPEED = 1;

    private final Hub hub;
    private final Yodel yodel;
    private final Player player;

    private boolean wasAllowedToFly = false;
    private boolean wasFlying = false;

    private Vector velocityStep;


    YodelRunner(Hub hub, Yodel yodel, Player player) {
        this.yodel = yodel;
        this.player = player;
        this.hub = hub;
    }

    public void start() {
        wasAllowedToFly = player.getAllowFlight();
        wasFlying = player.isFlying();

        player.teleport(getStart().setDirection(yodel.getAngleVector().multiply(1)));
        player.teleport(player.getLocation().add(0, 0.3, 0));

        player.setAllowFlight(true);
        player.setFlying(true);

        velocityStep = yodel.getAngleVector().normalize().multiply(SPEED).multiply(1);
        runTaskTimer(this.hub, 2L, 2L);
    }

    @Override
    public void run() {
        final Location playerLocation = player.getLocation();


        // Checks if the player is still on the line

        Vector position = playerLocation.toVector();
        Vector origin = getStart().toVector();
        Vector director = yodel.getAngleVector();
        double k;

        if (isZero(origin)) origin = getEnd().toVector();

        try {
            k = director.getX() * (position.getX() - origin.getX())
                    + director.getY() * (position.getY() - origin.getY())
                    + director.getZ() * (position.getZ() - origin.getZ());

            k /= NumberConversions.square(director.getX())
                    + NumberConversions.square(director.getY())
                    + NumberConversions.square(director.getZ());
        } catch (ArithmeticException e) {
            this.hub.getLogger().log(Level.SEVERE, "Division by zero while checking route on yodel, remains unfixed", e);
            return;
        }

        Vector projection = origin.add(director.multiply(k));

        if (projection.distanceSquared(position) > 4) {
            player.teleport(projection.toLocation(playerLocation.getWorld()).setDirection(playerLocation.getDirection()));
        }


        // Updates the velocity

        player.setVelocity(velocityStep);
        player.setFallDistance(0);


        // Checks if the line is finished (either we are on the finish zone, or out of the path if for some
        // reason the landing zone was not entered)

        if (playerLocation.distanceSquared(getEnd()) < 6
                || playerLocation.distanceSquared(getStart()) > yodel.getLength() + 10) {
            this.yodel.stop(player);
        }
    }

    public void stop() {
        try {
            cancel();

            player.setFlying(wasFlying);
            player.setAllowFlight(wasAllowedToFly);

            player.teleport(getLanding().setDirection(player.getLocation().getDirection()));
        } catch (IllegalStateException ignored) {
        }
    }

    private Location getStart() {
        return yodel.getStart();
    }

    private Location getEnd() {
        return yodel.getEnd();
    }

    private Location getLanding() {
        return yodel.getLanding();
    }

    private boolean isZero(Vector vector) {
        return Math.abs(vector.getX()) < Vector.getEpsilon()
                && Math.abs(vector.getY()) < Vector.getEpsilon()
                && Math.abs(vector.getZ()) < Vector.getEpsilon();
    }
}
