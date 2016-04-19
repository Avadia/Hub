package net.samagames.hub.games.signs;

import net.minecraft.server.v1_9_R1.*;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.parties.IParty;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameSign
{
    private final Hub hub;
    private final AbstractGame game;
    private final String map;
    private final ChatColor color;
    private final String template;
    private final Sign sign;
    private final BukkitTask updateTask;
    private boolean isMaintenance;

    private int scrollIndex = 0;
    private int scrollVector = +1;
    private String scrolledMapName;

    private int playerPerGame;
    private int playerWaitFor;
    private int totalPlayerOnServers;

    public GameSign(Hub hub, AbstractGame game, String map, ChatColor color, String template, Sign sign)
    {
        this.hub = hub;
        this.game = game;
        this.map = map;
        this.color = color;
        this.template = template;
        this.sign = sign;

        this.sign.setMetadata("game", new FixedMetadataValue(hub, game.getCodeName()));
        this.sign.setMetadata("map", new FixedMetadataValue(hub, map));

        hub.getScheduledExecutorService().scheduleAtFixedRate(this::scrollMapName, 500, 500, TimeUnit.MILLISECONDS);

        this.updateTask = hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, this::update, 20L, 20L);
    }

    public void onDelete()
    {
        this.updateTask.cancel();
    }

    public void update()
    {
        if(this.isMaintenance)
        {
            this.sign.setLine(0, "");
            this.sign.setLine(1, ChatColor.DARK_RED + "Jeu en");
            this.sign.setLine(2, ChatColor.DARK_RED + "maintenance !");
            this.sign.setLine(3, "");

            this.updateSign();
            return;
        }

        String mapLine = this.color + "» " + ChatColor.BOLD + this.scrolledMapName + ChatColor.RESET + this.color + " «";

        this.sign.setLine(0, this.game.getName());
        this.sign.setLine(1, mapLine);
        this.sign.setLine(2, this.playerWaitFor + "" + ChatColor.RESET + " en attente");
        this.sign.setLine(3, this.totalPlayerOnServers + "" + ChatColor.RESET + " en jeu");

        this.updateSign();
    }

    public void updateSign()
    {
        WorldServer worldServer = ((CraftWorld) this.sign.getWorld()).getHandle();

        IChatBaseComponent[] lines = new IChatBaseComponent[]
                {
                new ChatMessage(this.sign.getLine(0)),
                new ChatMessage(this.sign.getLine(1)),
                new ChatMessage(this.sign.getLine(2)),
                new ChatMessage(this.sign.getLine(3))
        };

        PacketPlayOutUpdateSign packet = new PacketPlayOutUpdateSign(worldServer, new BlockPosition(this.sign.getX(), this.sign.getY(), this.sign.getZ()), lines);

        this.sign.getWorld().getNearbyEntities(sign.getLocation(), 30, 30, 30).stream().filter(entity -> entity instanceof Player).forEach(entity -> ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet));
    }

    public void updateMapName()
    {
        this.sign.setLine(1, this.color + "» " + ChatColor.BOLD + this.scrolledMapName + ChatColor.RESET + this.color + " «");
        this.updateSign();
    }

    public void scrollMapName()
    {
        if(this.isMaintenance)
            return;

        if(this.map.length() <= 10)
        {
            this.scrolledMapName = this.map;
            return;
        }

        int start = this.scrollIndex;
        int end = this.scrollIndex + 10;

        if(end > this.map.length())
        {
            this.scrollVector = -1;
            this.scrollIndex = this.map.length() - 10;
            return;
        }
        if(start < 0)
        {
            this.scrollVector = 1;
            this.scrollIndex = 0;
            return;
        }

        this.scrolledMapName = this.map.substring(start, end);
        this.scrollIndex += this.scrollVector;

        this.updateMapName();
    }

    public void click(Player player)
    {
        if(this.isMaintenance)
        {
            player.sendMessage(ChatColor.RED + "Ce jeu est actuellement en maintenance.");
            return;
        }

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () -> {
            IParty party = SamaGamesAPI.get().getPartiesManager().getPartyForPlayer(player.getUniqueId());

            if(party == null)
            {
                this.hub.getHydroangeasManager().addPlayerToQueue(player.getUniqueId(), template);
            }
            else
            {
                if(!party.getLeader().equals(player.getUniqueId()))
                {
                    player.sendMessage(ChatColor.RED + "Vous n'êtes pas le leader de votre partie, vous ne pouvez donc pas l'ajouter dans une file d'attente.");
                    return;
                }

                this.hub.getHydroangeasManager().addPartyToQueue(player.getUniqueId(), party.getParty(), template);
            }
        });
    }

    public void developperClick(Player player)
    {
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "Informations du panneau de jeu :");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Template : " + ChatColor.GREEN + this.template);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Jeu : " + ChatColor.GREEN + this.game.getCodeName());
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Map : " + ChatColor.GREEN + this.map);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs par jeu : " + ChatColor.GREEN + this.playerPerGame);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en attente : " + ChatColor.GREEN + this.playerWaitFor);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en jeu : " + ChatColor.GREEN + this.totalPlayerOnServers);
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
    }

    public void setPlayerPerGame(int playerPerGame)
    {
        this.playerPerGame = playerPerGame;
    }

    public void setPlayerWaitFor(int playerWaitFor)
    {
        this.playerWaitFor = playerWaitFor;
    }

    public void setTotalPlayerOnServers(int totalPlayerOnServers)
    {
        this.totalPlayerOnServers = totalPlayerOnServers;
    }

    public void setMaintenance(boolean isMaintenance)
    {
        this.isMaintenance = isMaintenance;
        this.update();
    }

    public Sign getSign()
    {
        return this.sign;
    }

    public String getTemplate()
    {
        return this.template;
    }

    public String getMap()
    {
        return this.map;
    }

    public ChatColor getColor()
    {
        return this.color;
    }

    public int getTotalPlayerOnServers()
    {
        return this.totalPlayerOnServers;
    }

    public boolean isMaintenance()
    {
        return this.isMaintenance;
    }
}