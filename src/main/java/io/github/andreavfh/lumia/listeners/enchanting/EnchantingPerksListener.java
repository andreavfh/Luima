package io.github.andreavfh.lumia.listeners.enchanting;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EnchantingPerksListener implements Listener {

    private final SkillManager skillManager;

    public EnchantingPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onPrepareEnchant(PrepareItemEnchantEvent event) {
        Player player = event.getEnchanter();

        int tier = skillManager.getHolder(player).getSkill(SkillType.ENCHANTING).getTier();

        if (tier >= 1) {
            EnchantmentOffer[] offers = event.getOffers();
            if (offers != null) {
                for (EnchantmentOffer offer : offers) {
                    int reducedCost = Math.max(offer.getCost() - tier, 1); // reduce cost según tier, mínimo 1
                    offer.setCost(reducedCost);
                }
            }
        }
    }



    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        int tier = skillManager.getHolder(player).getSkill(SkillType.ENCHANTING).getTier();

        if (tier >= 3 && event.getEnchantsToAdd().isEmpty()) {
            Map<Enchantment, Integer> fallback = getBasicEnchant(event.getItem());
            fallback.forEach((enchant, level) -> event.getEnchantsToAdd().put(enchant, level));
        }

        if (tier >= 5) {
            Map<Enchantment, Integer> extra = getRandomAdditionalEnchant(event.getItem(), event.getEnchantsToAdd().keySet());
            extra.forEach((enchant, level) -> event.getEnchantsToAdd().put(enchant, level));
        }
    }

    private Map<Enchantment, Integer> getBasicEnchant(ItemStack item) {
        Map<Enchantment, Integer> enchant = new HashMap<>();
        if (item.getType().name().contains("SWORD")) {
            enchant.put(Enchantment.SHARPNESS, 1);
        } else if (item.getType().name().contains("PICKAXE")) {
            enchant.put(Enchantment.EFFICIENCY, 1);
        } else if (item.getType().name().contains("BOW")) {
            enchant.put(Enchantment.POWER, 1);
        }
        return enchant;
    }

    private Map<Enchantment, Integer> getRandomAdditionalEnchant(ItemStack item, Set<Enchantment> existing) {
        Map<Enchantment, Integer> result = new HashMap<>();
        List<Enchantment> possible = new ArrayList<>(Arrays.asList(Enchantment.values()));

        possible.removeIf(e -> !e.canEnchantItem(item) || existing.contains(e));

        if (!possible.isEmpty()) {
            Collections.shuffle(possible);
            Enchantment chosen = possible.get(0);
            result.put(chosen, 1);
        }
        return result;
    }
}
