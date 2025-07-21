package io.github.andreavfh.lumia.listeners.archery;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.logging.Logger;

public class ArcheryPerksListener implements Listener {

    private final SkillManager skillManager;

    public ArcheryPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onArrowDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile projectile)) return;
        if (!(projectile.getShooter() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.ARCHERY).getTier();
        if (tier <= 0) return;

        SkillPerks perks = SkillType.ARCHERY.getMeta()
                .getPerks();
        SkillPerk perk = perks.getPerk(tier);

        if (perk != null) {
            perk.apply(player, event);
        }
    }

    private static final Random random = new Random();

    public static void registerArcheryPerks(SkillMeta meta) {
        SkillPerks perks = meta.getPerks();
        Lumia plugin = Lumia.getPlugin(Lumia.class);

        perks.setPerk(1, new SkillPerk("Daño ligero", "+5% de daño con arco.",
                (player, event) -> {
                    if (event instanceof EntityDamageByEntityEvent e) {
                        e.setDamage(e.getDamage() * 1.05);
                    }
                }));

        perks.setPerk(2, new SkillPerk("Daño mejorado", "+10% de daño con arco.",
                (player, event) -> {
                    if (event instanceof EntityDamageByEntityEvent e) {
                        e.setDamage(e.getDamage() * 1.10);
                    }
                }));

        perks.setPerk(3, new SkillPerk("Golpe crítico", "+10% daño y 15% probabilidad de crítico.",
                (player, event) -> {
                    if (event instanceof EntityDamageByEntityEvent e) {
                        double damage = e.getDamage() * 1.10;
                        if (random.nextDouble() < 0.15) {
                            damage *= 1.5;
                            player.sendMessage(plugin.getLanguageConfig().getRaw("critical_bow_hit"));
                        }
                        e.setDamage(damage);
                    }
                }));

        perks.setPerk(4, new SkillPerk("Penetración de armadura", "+20% daño y parte del daño ignora armadura.",
                (player, event) -> {
                    if (event instanceof EntityDamageByEntityEvent e) {
                        LivingEntity target = null;
                        if (e.getEntity() instanceof LivingEntity le) {
                            target = le;
                        }
                        if (target != null) {
                            double baseDamage = e.getDamage();
                            double extraDamage = 0;
                            AttributeInstance armor = target.getAttribute(Attribute.GENERIC_ARMOR);
                            if (armor != null) {
                                double armorValue = armor.getValue();
                                extraDamage = armorValue * 0.4 * 0.04;
                            }
                            e.setDamage(baseDamage + extraDamage);
                        }
                    }
                }));

        perks.setPerk(5, new SkillPerk("Flecha aturdidora", "+20% daño y flecha ralentizadora.",
                (player, event) -> {
                    if (event instanceof EntityDamageByEntityEvent e) {
                        LivingEntity target = null;
                        if (e.getEntity() instanceof LivingEntity le) {
                            target = le;
                        }
                        if (target != null) {
                            e.setDamage(e.getDamage() * 1.20);
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1, true, false));
                            player.sendMessage(plugin.getLanguageConfig().getRaw("stun_arrow"));
                        }
                    }
                }));
    }
}
