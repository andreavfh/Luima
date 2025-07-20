package io.github.andreavfh.lumia.listeners.archery;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ArcheryPerksListener implements Listener {

    private static final double DAMAGE_BONUS_TIER_1 = 0.05;
    private static final double DAMAGE_BONUS_TIER_2 = 0.10;
    private static final double DAMAGE_BONUS_TIER_3 = 0.10;
    private static final double CRIT_CHANCE_TIER_3 = 0.15;
    private static final double CRIT_DAMAGE_MULTIPLIER = 1.5;
    private static final double DAMAGE_BONUS_TIER_4 = 0.20;
    private static final double DAMAGE_BONUS_TIER_5 = 0.20;
    private static final int SLOW_EFFECT_DURATION_TICKS = 60; // 3 seconds
    private static final int SLOW_EFFECT_AMPLIFIER = 1;

    private final SkillManager skillManager;
    private final Random random;
    private final Logger logger;

    public ArcheryPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
        this.logger = JavaPlugin.getPlugin(Lumia.class).getLogger();
        this.random = new Random();
    }


    @EventHandler
    public void onArrowDamage(@NotNull EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile projectile)) return;

        if (!(projectile.getShooter() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.ARCHERY).getTier();
        if (tier <= 0) return;

        Entity target = event.getEntity();
        if (!(target instanceof LivingEntity livingTarget)) return;

        double damage = event.getDamage();

        switch (tier) {
            case 1 -> damage = applyTier1Bonus(damage);
            case 2 -> damage = applyTier2Bonus(damage);
            case 3 -> damage = applyTier3Bonus(damage, player);
            case 4 -> damage = applyTier4Bonus(damage, livingTarget);
            case 5 -> {
                damage = applyTier5Bonus(damage, livingTarget, player);
            }
            default -> logger.log(Level.WARNING, "Archery tier " + tier + " not handled.");
        }

        event.setDamage(damage);
    }

    private double applyTier1Bonus(double baseDamage) {
        return baseDamage * (1.0 + DAMAGE_BONUS_TIER_1);
    }

    private double applyTier2Bonus(double baseDamage) {
        return baseDamage * (1.0 + DAMAGE_BONUS_TIER_2);
    }

    private double applyTier3Bonus(double baseDamage, Player player) {
        double modifiedDamage = baseDamage * (1.0 + DAMAGE_BONUS_TIER_3);

        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);

        if (random.nextDouble() < CRIT_CHANCE_TIER_3) {
            modifiedDamage *= CRIT_DAMAGE_MULTIPLIER;
            player.sendMessage(plugin.getLanguageConfig().getRaw("critical_bow_hit"));
        }
        return modifiedDamage;
    }

    private static final double PENETRATION_PERCENTAGE_TIER_4 = 0.4;

    private double applyTier4Bonus(double baseDamage, LivingEntity target) {
        double extraDamage = 0.0;

        AttributeInstance armorAttribute = target.getAttribute(Attribute.GENERIC_ARMOR);
        if (armorAttribute != null) {
            double armorValue = armorAttribute.getValue();
            // Simula el daño ignorando parte de la armadura
            extraDamage = armorValue * PENETRATION_PERCENTAGE_TIER_4 * 0.04; // Mojang: cada punto de armadura ≈ 4% reducción
        }

        return baseDamage + extraDamage;
    }


    private double applyTier5Bonus(double baseDamage, LivingEntity target, Player player) {
        double damageWithBonus = baseDamage * (1.0 + DAMAGE_BONUS_TIER_5);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, SLOW_EFFECT_DURATION_TICKS, SLOW_EFFECT_AMPLIFIER, true, false));
        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        player.sendMessage(plugin.getLanguageConfig().getRaw("stun_arrow"));
        return damageWithBonus;
    }
}
