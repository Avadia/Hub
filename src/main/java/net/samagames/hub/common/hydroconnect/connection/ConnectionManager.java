package net.samagames.hub.common.hydroconnect.connection;

import com.google.gson.Gson;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroconnect.HydroManager;
import net.samagames.hub.common.hydroconnect.packets.hubinfo.GameInfoToHubPacket;
import net.samagames.hub.common.hydroconnect.packets.queues.*;

import java.util.logging.Level;

/**
 * This file is a part of the SamaGames Project CodeBase
 * This code is absolutely confidential.
 * Created by Geekpower14 on 25/06/2015.
 * (C) Copyright Elydra Network 2014 & 2015
 * All rights reserved.
 */
public class ConnectionManager {

    public AbstractPacket[] packets = new AbstractPacket[256];

    protected Gson gson;
    protected Hub plugin;
    private HydroManager manager;

    public ConnectionManager(Hub plugin, HydroManager manager)
    {
        this.manager = manager;

        //Queues Packets
        packets[100] = new QueueAddPlayerPacket();
        packets[101] = new QueueRemovePlayerPacket();
        packets[102] = new QueueAttachPlayerPacket();
        packets[103] = new QueueDetachPlayerPacket();
        packets[104] = new QueueInfosUpdatePacket();

        //HubInfos
        packets[110] = new GameInfoToHubPacket();

        this.plugin = plugin;

        gson = new Gson();
    }

    public void getPacket(String packet)
    {
        String id;
        try{
            id = packet.split(":")[0];
            if(id == null || packets[Integer.parseInt(id)] == null)
            {
                plugin.log(Level.SEVERE, "Error bad packet ID in the channel");
                return;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            plugin.log(Level.SEVERE, "Error packet no ID in the channel");
            return;
        }

        packet = packet.substring(id.length()+1, packet.length());

        this.handler(Integer.valueOf(id), packet);
    }

    protected int packetId(AbstractPacket p)
    {
        for (int i = 0; i < packets.length; i++)
        {
            if(packets[i] == null)
                continue;
            if(packets[i].getClass().equals(p.getClass()))
                return i;
        }
        return -1;
    }

    protected void sendPacket(String channel, AbstractPacket data)
    {
        int i = packetId(data);
        if(i < 0)
        {
            plugin.log(Level.SEVERE, "Bad packet ID: " + i);
            return;
        }else if(channel == null)
        {
            plugin.log(Level.SEVERE, "Channel null !");
            return;
        }
        try{
            SamaGamesAPI.get().getPubSub().send(channel, i + ":" + gson.toJson(data));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendPacket(AbstractPacket packet)
    {
        String channel = "global@hydroangeas-server";
        this.sendPacket(channel, packet);
    }

    public void handler(int id, String data)
    {
        try{
            manager.getPacketReceiver().callPacket(gson.fromJson(data, packets[id].getClass()));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}