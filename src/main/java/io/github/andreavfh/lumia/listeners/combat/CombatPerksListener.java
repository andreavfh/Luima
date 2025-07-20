package io.github.andreavfh.lumia.listeners.combat;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CombatPerksListener implements Listener {

    private final SkillManager skillManager;

    public CombatPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onDamageBoost(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        int tier = skillManager.getHolder(player).getSkill(SkillType.COMBAT).getTier();
        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        LanguageConfig lang = plugin.getLanguageConfig();

        switch (tier) {
            case 1 -> {
                // Tier 1: +5% daño extra cuerpo a cuerpo
                event.setDamage(event.getDamage() * 1.05);
            }
            case 2 -> {
                // Tier 2: +10% daño y resistencia al retroceso
                event.setDamage(event.getDamage() * 1.10);
                if (!player.hasPotionEffect(PotionEffectType.RESISTANCE)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 0, true, false));
                }
            }
            case 3 -> {
                // Tier 3: +15% daño, resistencia y visión nocturna
                event.setDamage(event.getDamage() * 1.15);
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 80, 1, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200, 0, true, false));
            }
            case 4 -> {
                // Tier 4: +20% daño, +5% de esquivar ataques
                event.setDamage(event.getDamage() * 1.20);
            }
            case 5 -> {
                // Tier 5: +25% daño, probabilidad de furia temporal
                event.setDamage(event.getDamage() * 1.25);
                if (Math.random() < 0.10) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 250, 4, true, false));
                    player.sendMessage(lang.getRaw("fury_enabled"));
                }
            }
        }
    }

    @EventHandler
    public void onDamageTaken(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        int tier = skillManager.getHolder(player).getSkill(SkillType.COMBAT).getTier();
        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        LanguageConfig lang = plugin.getLanguageConfig();

        if (tier >= 4 && Math.random() < 0.05) {
            event.setCancelled(true);
            player.sendMessage(lang.getRaw("damage_evaded"));
        }
    }
}
