package net.samagames.hub.interactions.graou;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.achievements.exceptions.AchivementNotFoundException;
import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.tools.ItemUtils;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
class PearlLogic {
    private static final ItemStack ONE_STAR_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQ5NTY5MTczNTIxMzhjN2NmNDZmOTVlOGZkOTNhODUzNzJjN2ZiMjdmOWI2MWUxZTc1MDc3MTQ0NDQ5NTMyZiJ9fX0=");
    private static final ItemStack TWO_STAR_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBkNzg4MzU3M2I0ZTQ1ZjMyOGU1NzJjYmNjYzczMmExNjcwMzBjZjkzODI2NDYzZjNhMWFhZWU4ZTkyYWQ0In19fQ==");
    private static final ItemStack THREE_STAR_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODYzM2EyNmY1MGJhODU0YmUwMjEyOTI1ZDFkNWRkNmMzMzdiODE4NGZkYTZmYjJkMjFmNTE1MTE1M2IxNTYifX19");
    private static final ItemStack FOUR_STAR_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzczN2Q3NGU3ZTZlZjJjODAyYWFmYzhjZjNmOWQ1YjhkYzM3MDdmMzgxMWRhZjFmNTAxYzc1YTBmYTNiNGMifX19");
    private static final ItemStack FIVE_STAR_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM2MTg4NjA0ODgxMWQzYTY4OWJjMjY2ZDFkZGRjYjI1YjAzYzkxZmNkMmMxZDVhM2ZhOWU4NzNlNTY1ODM4In19fQ==");
    private final Hub hub;
    private final CosmeticList cosmetics;

    PearlLogic(Hub hub) {
        this.hub = hub;

        this.cosmetics = new CosmeticList();
        this.cosmetics.addAll(hub.getCosmeticManager().getDisguiseManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getParticleManager().getRegistry().getElements().values());
        //this.cosmetics.addAll(hub.getCosmeticManager().getPetManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getJukeboxManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getBalloonManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getGadgetManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getClothManager().getRegistry().getElements().values());

        Collections.shuffle(this.cosmetics);
    }

    public AbstractCosmetic unlockRandomizedCosmetic(Player player, Pearl pearl, Location openingLocation) {
        Collections.shuffle(this.cosmetics);

        CosmeticRarity rarity = Star.getByCount(pearl.getStars()).getRandomizedRarity();
        List<AbstractCosmetic> cosmeticsSelected = this.cosmetics.getByRarity(rarity);

        AbstractCosmetic cosmeticSelected = cosmeticsSelected.get(new Random().nextInt(cosmeticsSelected.size()));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Rareté : " + cosmeticSelected.getRarity().getColor() + cosmeticSelected.getRarity().getName());
        lore.add(ChatColor.DARK_GRAY + "Type : " + ChatColor.GRAY + cosmeticSelected.getCategoryName());

        new FancyMessage("\u272F ").color(ChatColor.GOLD)
                .then("Vous avez trouvé ").color(ChatColor.YELLOW)
                .then(cosmeticSelected.getIcon().getItemMeta().getDisplayName()).tooltip(lore)
                .then(" dans le cadeau !").color(ChatColor.YELLOW)
                .then(" \u272F").color(ChatColor.GOLD)
                .send(player);

        if (cosmeticSelected.isOwned(player)) {
            player.sendMessage(ChatColor.GOLD + "\u272F " + ChatColor.YELLOW + "Malheureusement, vous possédiez déjà ce cosmétique..." + ChatColor.GOLD + " \u272F");
            player.sendMessage(ChatColor.GOLD + "\u272F " + ChatColor.YELLOW + "Graou vous offre alors " + ChatColor.AQUA + cosmeticSelected.getRefundPrice() + " poussières d'\u272F" + ChatColor.GOLD + " \u272F");

            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).increasePowders(cosmeticSelected.getRefundPrice());

            this.hub.getScoreboardManager().update(player);
        } else {
            if (cosmeticSelected.getRarity() == CosmeticRarity.EPIC || cosmeticSelected.getRarity() == CosmeticRarity.LEGENDARY) {
                FancyMessage globalMessage = new FancyMessage("\u272F " + player.getName()).color(ChatColor.GOLD)
                        .then(" a trouvé ").color(ChatColor.YELLOW)
                        .then(cosmeticSelected.getIcon().getItemMeta().getDisplayName()).tooltip(lore)
                        .then(" dans un cadeau !").color(ChatColor.YELLOW)
                        .then(" \u272F").color(ChatColor.GOLD);

                this.hub.getServer().getOnlinePlayers().stream().filter(p -> p.getUniqueId() != player.getUniqueId()).forEach(globalMessage::send);

                if (cosmeticSelected.getRarity() == CosmeticRarity.LEGENDARY) {
                    openingLocation.getWorld().strikeLightningEffect(openingLocation);
                    this.hub.getServer().getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.0F, 1.0F));
                }
            }

            cosmeticSelected.unlock(player);
        }

        try {
            SamaGamesAPI.get().getAchievementManager().getAchievementByID(50).unlock(player.getUniqueId());
        } catch (AchivementNotFoundException e) {
            e.printStackTrace();
        }

        this.hub.getInteractionManager().getGraouManager().deletePlayerPearl(player.getUniqueId(), pearl.getUUID());
        this.hub.getScoreboardManager().update(player);

        return cosmeticSelected;
    }

    public ItemStack getIcon(Pearl pearl) {
        ItemStack stack;

        if (pearl.getStars() == 1)
            stack = ONE_STAR_HEAD.clone();
        else if (pearl.getStars() == 2)
            stack = TWO_STAR_HEAD.clone();
        else if (pearl.getStars() == 3)
            stack = THREE_STAR_HEAD.clone();
        else if (pearl.getStars() == 4)
            stack = FOUR_STAR_HEAD.clone();
        else
            stack = FIVE_STAR_HEAD.clone();

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Perle");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Niveau " + ChatColor.GREEN + pearl.getStars());
        lore.add("");
        lore.add(ChatColor.GRAY + "Cette perle si brillante et si");
        lore.add(ChatColor.GRAY + "mystérieuse en fait envier plus");
        lore.add(ChatColor.GRAY + "d'un. " + ChatColor.GOLD + "Graou" + ChatColor.GRAY + " en fait collection");
        lore.add(ChatColor.GRAY + "et vous l'échangera contre un");
        lore.add(ChatColor.GRAY + "cosmétique à la hauteur du niveau");
        lore.add(ChatColor.GRAY + "de cette perle.");
        lore.add("");

        if (pearl.getStars() > 3)
            lore.add(ChatColor.DARK_GRAY + "Nécéssite : " + (pearl.getStars() == 5 ? ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+" : ChatColor.GREEN + "VIP"));

        lore.add(ChatColor.DARK_GRAY + "Expire dans : " + ChatColor.WHITE + pearl.getExpirationInDays() + " jour" + (pearl.getExpirationInDays() > 1 ? "s" : ""));

        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez pour échanger");

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    private enum Star {
        ONE(80, 20, 0),
        TWO(65, 30, 5),
        THREE(32, 45, 20),
        FOUR(5, 30, 50),
        FIVE(0, 20, 60);

        private final int commonPercentage;
        private final int rarePercentage;
        private final int epicPercentage;

        Star(int commonPercentage, int rarePercentage, int epicPercentage) {
            this.commonPercentage = commonPercentage;
            this.rarePercentage = rarePercentage;
            this.epicPercentage = epicPercentage;
        }

        public static Star getByCount(int stars) {
            if (stars == 1)
                return ONE;
            else if (stars == 2)
                return TWO;
            else if (stars == 3)
                return THREE;
            else if (stars == 4)
                return FOUR;
            else
                return FIVE;
        }

        /**
         * Return a randomized cosmetic rarity calculated with
         * the percentages.
         * <p>
         * Note: We don't need a legendary percentage because of
         * the total of the percentage have to be equals to 100
         * (obvious).
         *
         * @return A randomized cosmetic rarity
         */
        public CosmeticRarity getRandomizedRarity() {
            int random = new Random().nextInt(100);

            if (random <= this.commonPercentage)
                return CosmeticRarity.COMMON;
            else if (random <= this.commonPercentage + this.rarePercentage)
                return CosmeticRarity.RARE;
            else if (random <= this.commonPercentage + this.rarePercentage + this.epicPercentage)
                return CosmeticRarity.EPIC;
            else
                return CosmeticRarity.LEGENDARY;
        }
    }

    private static class CosmeticList extends ArrayList<AbstractCosmetic> {
        public List<AbstractCosmetic> getByRarity(CosmeticRarity cosmeticRarity) {
            List<AbstractCosmetic> list = new ArrayList<>();

            for (AbstractCosmetic cosmetic : this)
                if (cosmetic.getRarity() == cosmeticRarity)
                    list.add(cosmetic);

            return list;
        }
    }
}
