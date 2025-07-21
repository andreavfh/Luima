package io.github.andreavfh.lumia.listeners.fishing;

import io.github.andreavfh.lumia.skill.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FishingPerksListener implements Listener {

    private final SkillManager skillManager;

    public FishingPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        int tier = skillManager.getHolder(player).getSkill(SkillType.FISHING).getTier();

        if (tier <= 0) return;

        SkillMeta meta = SkillType.FISHING.getMeta();
        SkillPerk perk = meta.getPerks().getPerk(tier);

        if (perk != null) {
            if (tier == 5 ) {
                perk.apply(player, event);
            } else {
                perk.apply(player);
            }
        }
    }


    public static void registerFishingPerks(SkillMeta meta) {
        SkillPerks perks = meta.getPerks();

        perks.setPerk(1, new SkillPerk("Velocidad I", "Te da velocidad I por 3 segundos al pescar.",
                (player, context) -> applyPotionEffect(player, PotionEffectType.SPEED, 60, 0)));

        perks.setPerk(2, new SkillPerk("Velocidad II", "Te da velocidad II por 3 segundos al pescar.",
                (player, context) -> applyPotionEffect(player, PotionEffectType.SPEED, 60, 1)));

        perks.setPerk(3, new SkillPerk("Suerte I", "Aumenta tu suerte al pescar.",
                (player, context) -> applyPotionEffect(player, PotionEffectType.LUCK, 60, 0)));

        perks.setPerk(4, new SkillPerk("Suerte II", "Mayor suerte aún al pescar.",
                (player, context) -> applyPotionEffect(player, PotionEffectType.LUCK, 60, 1)));

        perks.setPerk(5, new SkillPerk("Doble captura", "15% de probabilidad de duplicar el ítem pescado.",
                (player, context) -> {
                    if (context instanceof PlayerFishEvent event && event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                        if (Math.random() < 0.15) {
                            Entity caught = event.getCaught();
                            if (caught instanceof Item item) {
                                ItemStack clone = item.getItemStack().clone();
                                player.getWorld().dropItemNaturally(player.getLocation(), clone);
                            }
                        }
                    }
                }));

    }

    private static void applyPotionEffect(Player player, PotionEffectType type, int durationTicks, int amplifier) {
        if (!player.hasPotionEffect(type)) {
            player.addPotionEffect(new PotionEffect(type, durationTicks, amplifier, true, false));
        }
    }

}
