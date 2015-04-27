package net.samagames.hub.games;

import net.samagames.hub.games.shop.ShopCategory;
import net.samagames.hub.games.sign.GameSign;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractGame
{
    private final HashMap<String, GameSign> signs;
    private boolean isMaintenance;

    public AbstractGame()
    {
        this.signs = new HashMap<>();
    }

    public abstract String getCodeName();
    public abstract String getName();
    public abstract ItemStack getIcon();
    public abstract String[] getDescription();
    public abstract int getSlotInMainMenu();
    public abstract ShopCategory getShopConfiguration();
    public abstract ArrayList<DisplayedStat> getDisplayedStats();
    public abstract Location getLobbySpawn();
    public abstract boolean isLocked();

    public void addSignForMap(String map, Sign sign)
    {
        this.signs.put(map, new GameSign(this, map, sign));
    }

    public void setMaintenance(boolean flag)
    {
        this.isMaintenance = flag;

        for(GameSign sign : this.signs.values())
            sign.update(null);
    }

    public DisplayedStat getDisplayedStatByIdentifier(String identifier)
    {
        for(DisplayedStat stat : this.getDisplayedStats())
        {
            if(stat.getDatabaseName().equals(identifier))
                return stat;
        }

        return null;
    }

    public GameSign getGameSignByMap(String map)
    {
        if(this.signs.containsKey(map))
            return this.signs.get(map);
        else
            return null;
    }

    public HashMap<String, GameSign> getSigns()
    {
        return this.signs;
    }

    public boolean hasShop()
    {
        return this.getShopConfiguration() != null;
    }

    public boolean isMaintenance()
    {
        return this.isMaintenance;
    }
}
