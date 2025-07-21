package io.github.andreavfh.lumia.listeners.enchanting;

import io.github.andreavfh.lumia.skill.*;
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
        SkillPerks perks = SkillType.ENCHANTING.getMeta()
                .getPerks();
        int tier = skillManager.getHolder(player).getSkill(SkillType.ENCHANTING).getTier();
        SkillPerk perk = perks.getPerk(tier);

        if (perk != null) {
            perk.apply(player, event);
        }

    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();

        SkillPerks perks = SkillType.ENCHANTING.getMeta()
                .getPerks();

        int tier = skillManager.getHolder(player).getSkill(SkillType.ENCHANTING).getTier();
        SkillPerk perk = perks.getPerk(tier);

        if (perk != null) {
            perk.apply(player, event);
        }
    }

    public static void registerEnchantingPerks(SkillMeta meta, SkillManager m) {
        SkillPerks perks = meta.getPerks();

        // Tier 1-2: reducir costo de encantamientos
        perks.setPerk(1, new SkillPerk("Encantamiento más barato", "Reduce el costo de encantamientos según el tier.",
                (player, event) -> {
                    if (!(event instanceof PrepareItemEnchantEvent)) return;
                    EnchantmentOffer[] offers = ((PrepareItemEnchantEvent) event).getOffers();
                    if (offers != null) {
                        for (EnchantmentOffer offer : offers) {
                            int reducedCost = Math.max(offer.getCost() - m.getHolder(player).getSkill(SkillType.ENCHANTING).getLevel(), 1);
                            offer.setCost(reducedCost);
                        }
                    }
                }));

        // Tier 3-4: añadir encantamiento básico si no se aplicó ninguno
        perks.setPerk(3, new SkillPerk("Encantamiento de emergencia", "Aplica uno básico si no recibes ninguno.",
                (player, event) -> {
                    if (!(event instanceof EnchantItemEvent)) return;
                    EnchantItemEvent e = (EnchantItemEvent) event;
                    if (e.getEnchantsToAdd().isEmpty()) {
                        getBasicEnchant(e.getItem()).forEach((enchant, level) -> e.getEnchantsToAdd().put(enchant, level));
                    }
                }));

        // Tier 5: encantamiento extra aleatorio
        perks.setPerk(5, new SkillPerk("Encantamiento adicional", "Añade un encantamiento aleatorio adicional.",
                (player, event) -> {
                    if (!(event instanceof EnchantItemEvent)) return;
                    EnchantItemEvent e = (EnchantItemEvent) event;
                    Map<Enchantment, Integer> extra = getRandomAdditionalEnchant(e.getItem(), e.getEnchantsToAdd().keySet());
                    extra.forEach((enchant, level) -> e.getEnchantsToAdd().put(enchant, level));
                }));
    }

    private static Map<Enchantment, Integer> getBasicEnchant(ItemStack item) {
        Map<Enchantment, Integer> enchant = new HashMap<>();
        String type = item.getType().name();
        if (type.contains("SWORD")) {
            enchant.put(Enchantment.SHARPNESS, 1);
        } else if (type.contains("PICKAXE")) {
            enchant.put(Enchantment.EFFICIENCY, 1);
        } else if (type.contains("BOW")) {
            enchant.put(Enchantment.POWER, 1);
        }
        return enchant;
    }

    private static Map<Enchantment, Integer> getRandomAdditionalEnchant(ItemStack item, Set<Enchantment> existing) {
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
