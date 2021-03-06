package net.samagames.hub.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.balloons.BalloonManager;
import net.samagames.hub.cosmetics.clothes.ClothManager;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.disguises.DisguiseManager;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;
import net.samagames.hub.cosmetics.particles.ParticleManager;
import org.bukkit.entity.Player;

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
public class CosmeticManager extends AbstractManager {
    private DisguiseManager disguiseManager;
    private JukeboxManager jukeboxManager;
    //private PetManager petManager;
    private GadgetManager gadgetManager;
    private ParticleManager particleManager;
    private BalloonManager balloonManager;
    private ClothManager clothManager;

    public CosmeticManager(Hub hub) {
        super(hub);
    }

    @Override
    public void onDisable() { /* Not needed **/}

    @Override
    public void onLogin(Player player) {
        this.jukeboxManager.onLogin(player);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            this.disguiseManager.restoreCosmetic(player);
            //this.petManager.restoreCosmetic(player);
            this.gadgetManager.restoreCosmetic(player);
            this.particleManager.restoreCosmetic(player);
            this.balloonManager.restoreCosmetic(player);
            this.clothManager.restoreCosmetic(player);
        }, 20L);
    }

    @Override
    public void onLogout(Player player) {
        this.jukeboxManager.onLogout(player);

        this.disguiseManager.disableCosmetics(player, true, false);
        this.jukeboxManager.disableCosmetics(player, true, false);
        //this.petManager.disableCosmetics(player, true, false);
        this.gadgetManager.disableCosmetics(player, true, false);
        this.particleManager.disableCosmetics(player, true, false);
        this.balloonManager.disableCosmetics(player, true, false);
        this.clothManager.disableCosmetics(player, true, false);
    }

    public DisguiseManager getDisguiseManager() {
        return this.disguiseManager;
    }

    public JukeboxManager getJukeboxManager() {
        return this.jukeboxManager;
    }

//    public PetManager getPetManager() {
//        return this.petManager;
//    }

    public GadgetManager getGadgetManager() {
        return this.gadgetManager;
    }

    public ParticleManager getParticleManager() {
        return this.particleManager;
    }

    public BalloonManager getBalloonManager() {
        return balloonManager;
    }

    public ClothManager getClothManager() {
        return this.clothManager;
    }

    public boolean isEquipped(Player player, AbstractCosmetic cosmetic) {
        if (this.disguiseManager.getEquippedCosmetics(player) != null && this.disguiseManager.isCosmeticEquipped(player, cosmetic))
            return true;
//        else if (this.petManager.getEquippedCosmetics(player) != null && this.petManager.isCosmeticEquipped(player, cosmetic))
//            return true;
        else if (this.gadgetManager.getEquippedCosmetics(player) != null && this.gadgetManager.isCosmeticEquipped(player, cosmetic))
            return true;
        else if (this.particleManager.getEquippedCosmetics(player) != null && this.particleManager.isCosmeticEquipped(player, cosmetic))
            return true;
        else if (this.balloonManager.getEquippedCosmetics(player) != null && this.balloonManager.isCosmeticEquipped(player, cosmetic))
            return true;
        else
            return this.clothManager.getEquippedCosmetics(player) != null && this.clothManager.isCosmeticEquipped(player, cosmetic);
    }

    public void init() {
        this.disguiseManager = new DisguiseManager(this.hub);
        this.jukeboxManager = new JukeboxManager(this.hub);
        //this.petManager = new PetManager(this.hub);
        this.gadgetManager = new GadgetManager(this.hub);
        this.particleManager = new ParticleManager(this.hub);
        this.balloonManager = new BalloonManager(this.hub);
        this.clothManager = new ClothManager(this.hub);
    }
}
