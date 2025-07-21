package io.github.andreavfh.lumia.listeners.combat;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.skill.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CombatPerksListener implements Listener {

    private final SkillManager skillManager;

    public CombatPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onDamageDealt(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.COMBAT).getTier();
        if (tier <= 0) return;

        SkillPerks perks = SkillType.COMBAT.getMeta().getPerks(); // <- este era el error
        SkillPerk perk = perks.getPerk(tier);

        if (perk != null) {
            perk.apply(player, event);
        }
    }


    @EventHandler
    public void onDamageTaken(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.COMBAT).getTier();
        if (tier < 4) return;

        SkillPerk perk = SkillType.COMBAT.getMeta().getPerks().getPerk(tier);
        if (perk != null) {
            perk.apply(player, event);
        }
    }


    public static void registerCombatPerks(SkillMeta meta) {
            SkillPerks perks = meta.getPerks();

            LanguageConfig lang = Lumia.getPlugin(Lumia.class).getLanguageConfig();

            perks.setPerk(1, new SkillPerk("Daño ligero", "+5% daño cuerpo a cuerpo.",
                    (player, event) -> {
                        if (event instanceof EntityDamageByEntityEvent e) {
                            e.setDamage(e.getDamage() * 1.05);
                        }
                    }));

            perks.setPerk(2, new SkillPerk("Daño y resistencia", "+10% daño y resistencia al retroceso.",
                    (player, event) -> {
                        if (event instanceof EntityDamageByEntityEvent e) {
                            e.setDamage(e.getDamage() * 1.10);
                            if (!player.hasPotionEffect(PotionEffectType.RESISTANCE)) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 0, true, false));
                            }
                        }
                    }));

            perks.setPerk(3, new SkillPerk("Daño fuerte", "+15% daño, resistencia y visión nocturna.",
                    (player, event) -> {
                        if (event instanceof EntityDamageByEntityEvent e) {
                            e.setDamage(e.getDamage() * 1.15);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 80, 1, true, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 0, true, false));
                        }
                    }));

            perks.setPerk(4, new SkillPerk("Daño mejorado y esquiva", "+20% daño y 5% de esquivar ataques.",
                    (player, event) -> {
                        if (event instanceof EntityDamageByEntityEvent e) {
                            e.setDamage(e.getDamage() * 1.20);
                        }

                        if (event instanceof EntityDamageEvent e) {
                            if (Math.random() < 0.05) {
                                e.setCancelled(true);
                                player.sendMessage(lang.getRaw("damage_evaded"));
                            }
                        }
                    }));

            perks.setPerk(5, new SkillPerk("Furia", "+25% daño y 10% de furia temporal.",
                    (player, event) -> {
                        if (event instanceof EntityDamageByEntityEvent e) {
                            e.setDamage(e.getDamage() * 1.25);
                            if (Math.random() < 0.10) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 250, 4, true, false));
                                player.sendMessage(lang.getRaw("fury_enabled"));
                            }
                        }
                    }));
        }
    }
