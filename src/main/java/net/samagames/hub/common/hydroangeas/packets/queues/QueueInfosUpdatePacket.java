package net.samagames.hub.common.hydroangeas.packets.queues;

import net.samagames.hub.common.hydroangeas.QPlayer;
import net.samagames.hub.common.hydroangeas.connection.Packet;

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
public class QueueInfosUpdatePacket extends Packet {
    private Type type;

    private String game;
    private String map;
    private String templateId;

    //Join / remove
    private boolean success = true;
    private String errorMessage;

    //Info

    private boolean starting = false;
    private long timeToStart = -1;

    private int remainingPlayer = -1;

    private int place = -1;
    private int groupSize = -1;

    //target
    private QPlayer player;

    public QueueInfosUpdatePacket() {
    }

    public QueueInfosUpdatePacket(QPlayer player, Type type, String game, String map) {
        this(player, type);
        this.game = game;
        this.map = map;
    }

    public QueueInfosUpdatePacket(QPlayer player, Type type, String templateId) {
        this(player, type);
        this.templateId = templateId;
    }

    public QueueInfosUpdatePacket(QPlayer player, Type type) {
        this.player = player;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public QPlayer getPlayer() {
        return player;
    }

    public void setPlayer(QPlayer player) {
        this.player = player;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public boolean isStarting() {
        return starting;
    }

    public void setStarting(boolean starting) {
        this.starting = starting;
    }

    public long getTimeToStart() {
        return timeToStart;
    }

    public void setTimeToStart(long timeToStart) {
        this.timeToStart = timeToStart;
    }

    public int getRemainingPlayer() {
        return remainingPlayer;
    }

    public void setRemainingPlayer(int remainingPlayer) {
        this.remainingPlayer = remainingPlayer;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public enum Type {
        ADD, REMOVE, INFO
    }
}
