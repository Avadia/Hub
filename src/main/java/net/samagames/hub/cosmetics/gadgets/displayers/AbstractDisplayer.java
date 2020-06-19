package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.Hub;
import net.samagames.tools.SimpleBlock;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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
public abstract class AbstractDisplayer {
    protected static final Random RANDOM = new Random();

    protected final Hub hub;
    protected final Player player;
    protected final Map<Location, SimpleBlock> blocksUsed;
    protected final Map<Location, SimpleBlock> blocksBefore;
    protected final List<UUID> interactingPlayers;
    protected Location baseLocation;

    public AbstractDisplayer(Hub hub, Player player) {
        this.hub = hub;
        this.player = player;
        this.blocksUsed = new HashMap<>();
        this.blocksBefore = new HashMap<>();
        this.interactingPlayers = new ArrayList<>();
        this.baseLocation = player.getLocation();
    }

    public abstract void display();

    public abstract void handleInteraction(Entity who, Entity with);

    public abstract boolean isInteractionsEnabled();

    public abstract boolean canUse();

    public void end() {
        this.hub.getCosmeticManager().getGadgetManager().callbackGadget(this);
    }

    public void addBlockToUse(Location location, SimpleBlock block) {
        this.blocksUsed.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ()), block);
        this.blocksBefore.put(new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ()), new SimpleBlock(location.getBlock()));
    }

    public void addBlocksToUse(Map<Location, SimpleBlock> blocks) {
        for (Location block : blocks.keySet()) {
            this.blocksUsed.put(new Location(block.getWorld(), block.getBlockX(), block.getBlockY(), block.getBlockZ()), blocks.get(block));
            this.blocksBefore.put(new Location(block.getWorld(), block.getBlockX(), block.getBlockY(), block.getBlockZ()), new SimpleBlock(block.getBlock()));
        }
    }

    public void interactWith(Player player) {
        this.interactingPlayers.add(player.getUniqueId());
    }

    @SuppressWarnings("deprecation")
    public void restore() {
        for (Location block : this.blocksUsed.keySet()) {
            block.getBlock().setType(this.blocksBefore.get(block).getType());
            block.getBlock().setData(this.blocksBefore.get(block).getData());
        }

        this.blocksUsed.clear();
        this.blocksBefore.clear();
    }

    @SuppressWarnings("deprecation")
    public void restore(Location location) {
        Location finalLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

        if (this.blocksUsed.containsKey(finalLocation) && this.blocksBefore.containsKey(finalLocation)) {
            this.blocksUsed.remove(finalLocation);
            finalLocation.getBlock().setType(this.blocksBefore.get(finalLocation).getType());
            finalLocation.getBlock().setData(this.blocksBefore.get(finalLocation).getData());
            this.blocksBefore.remove(finalLocation);
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public Map<Location, SimpleBlock> getBlocksUsed() {
        return this.blocksUsed;
    }

    public boolean canInteractWith(Player player) {
        return !this.hub.getPlayerManager().isBusy(player);
    }

    public boolean isBlockGloballyUsed(Location location) {
        return this.hub.getCosmeticManager().getGadgetManager().isBlockUsed(location);
    }

    public boolean isBlockUsed(Location location) {
        return this.blocksUsed.containsKey(new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    public boolean isInteractingWith(Player player) {
        return this.interactingPlayers.contains(player.getUniqueId());
    }
}
