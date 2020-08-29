package net.samagames.hub.games.signs;

import net.minecraft.server.v1_12_R1.*;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
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
public class TitleSign {
    private final AbstractGame game;
    private final Sign sign;
    private final BukkitTask updateTask;

    public TitleSign(Hub hub, AbstractGame game, Sign sign) {
        this.game = game;
        this.sign = sign;

        this.sign.setMetadata("game", new FixedMetadataValue(hub, game.getCodeName()));

        this.updateTask = hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, this::update, 20L, 20L);
    }

    public void onDelete() {
        this.updateTask.cancel();
    }

    public void update() {
        this.sign.setLine(0, "");
        this.sign.setLine(1, this.game.getName());
        this.sign.setLine(2, "");
        this.sign.setLine(3, this.game.getOnlinePlayers() + " en jeu");

        this.updateSign();
    }

    public void updateSign() {
        IChatBaseComponent[] lines = new IChatBaseComponent[]{
                new ChatComponentText(this.sign.getLine(0)),
                new ChatComponentText(this.sign.getLine(1)),
                new ChatComponentText(this.sign.getLine(2)),
                new ChatComponentText(this.sign.getLine(3))
        };

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("id", "Sign");
        nbt.setInt("x", this.sign.getLocation().getBlockX());
        nbt.setInt("y", this.sign.getLocation().getBlockY());
        nbt.setInt("z", this.sign.getLocation().getBlockZ());

        for (int i = 0; i < 4; ++i)
            nbt.setString("Text" + (i + 1), IChatBaseComponent.ChatSerializer.a(lines[i]));

        PacketPlayOutTileEntityData packet = new PacketPlayOutTileEntityData(new BlockPosition(this.sign.getX(), this.sign.getY(), this.sign.getZ()), 9, nbt);

        this.sign.getWorld().getNearbyEntities(this.sign.getLocation(), 30, 30, 30).stream().filter(entity -> entity instanceof Player).forEach(entity -> ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet));
    }

    public Sign getSign() {
        return this.sign;
    }
}
