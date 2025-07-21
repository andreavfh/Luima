package io.github.andreavfh.lumia.listeners.agility;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.skill.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.WeakHashMap;

public class AgilityPerksListener implements Listener {

    private final SkillManager skillManager;
    public static final WeakHashMap<UUID, Long> doubleJumpCooldowns = new WeakHashMap<>();

    public AgilityPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int tier = skillManager.getHolder(player).getSkill(SkillType.AGILITY).getTier();

        if (tier == 0) return;

        SkillPerk perk = SkillType.AGILITY.getMeta().getPerks().getPerk(tier);
        if (perk != null) {
            perk.apply(player, event);
        }
    }

    @EventHandler
    public void onPlayerFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.AGILITY).getTier();
        if (tier == 0) return;

        SkillPerk perk = SkillType.AGILITY.getMeta().getPerks().getPerk(tier);
        if (perk != null) {
            perk.apply(player, event);
        }
    }

    @EventHandler
    public void onPlayerDamageEvade(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.AGILITY).getTier();
        if (tier == 0) return;

        SkillPerk perk = SkillType.AGILITY.getMeta().getPerks().getPerk(tier);
        if (perk != null) {
            perk.apply(player, event);
        }
    }

    @EventHandler
    public void onPlayerDoubleJump(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        int tier = skillManager.getHolder(player).getSkill(SkillType.AGILITY).getTier();

        if (tier == 0) return;

        SkillPerk perk = SkillType.AGILITY.getMeta().getPerks().getPerk(tier);
        if (perk != null) {
            perk.apply(player, event);
        }
    }

    // --- Método estático para registrar los perks ---

    public static void registerAgilityPerks(SkillMeta meta) {
        SkillPerks perks = meta.getPerks();
        Lumia plugin = Lumia.getPlugin(Lumia.class);
        LanguageConfig lang = plugin.getLanguageConfig();

        final int COOLDOWN_MS = 5000;
        final int POTION_DURATION_TICKS = 100;
        final double DAMAGE_REDUCTION_FACTOR = 0.7;
        final double DAMAGE_EVADE_CHANCE = 0.10;

        // Tier 1: Velocidad base + sin perks adicionales en movimiento
        perks.setPerk(1, new SkillPerk("Velocidad básica", "Aumenta velocidad base.",
                (player, event) -> {
                    if (event instanceof PlayerMoveEvent) {
                        int speedAmplifier = 0;
                        PotionEffect currentSpeed = player.getPotionEffect(PotionEffectType.SPEED);
                        if (currentSpeed == null || currentSpeed.getAmplifier() < speedAmplifier) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, POTION_DURATION_TICKS, speedAmplifier, true, false));
                        }
                    } else if (event instanceof EntityDamageEvent) {
                        // No aplica para Tier 1 en daño o toggle sneak
                    }
                }));

        // Tier 2: Velocidad mejorada y slow falling cuando sube
        perks.setPerk(2, new SkillPerk("Velocidad y caída lenta", "Velocidad mejorada y caída lenta al subir.",
                (player, event) -> {
                    if (event instanceof PlayerMoveEvent e) {
                        int speedAmplifier = 1;
                        PotionEffect currentSpeed = player.getPotionEffect(PotionEffectType.SPEED);
                        if (currentSpeed == null || currentSpeed.getAmplifier() < speedAmplifier) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, POTION_DURATION_TICKS, speedAmplifier, true, false));
                        }
                        if (e.getFrom().getY() < e.getTo().getY()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0, true, false));
                        }
                    }
                }));

        // Tier 3: Reduce daño por caída al 70%
        perks.setPerk(3, new SkillPerk("Reducción de daño por caída", "Reduce daño por caída a 70%.",
                (player, event) -> {
                    if (event instanceof EntityDamageEvent e && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        double reducedDamage = e.getDamage() * DAMAGE_REDUCTION_FACTOR;
                        e.setDamage(reducedDamage);
                    } else if (event instanceof PlayerMoveEvent) {
                        // También incluye perks tier 2 para movimiento
                        int speedAmplifier = 1;
                        PotionEffect currentSpeed = player.getPotionEffect(PotionEffectType.SPEED);
                        if (currentSpeed == null || currentSpeed.getAmplifier() < speedAmplifier) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, POTION_DURATION_TICKS, speedAmplifier, true, false));
                        }
                        PlayerMoveEvent e2 = (PlayerMoveEvent) event;
                        if (e2.getFrom().getY() < e2.getTo().getY()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0, true, false));
                        }
                    }
                }));

        // Tier 4: Probabilidad 10% de evadir daño y jump boost
        perks.setPerk(4, new SkillPerk("Evasión y salto mejorado", "10% de evadir daño y salto potenciado.",
                (player, event) -> {
                    if (event instanceof EntityDamageEvent e) {
                        if (Math.random() < DAMAGE_EVADE_CHANCE) {
                            e.setCancelled(true);
                            player.sendMessage(lang.getRaw("damage_evaded"));
                        }
                    } else if (event instanceof PlayerMoveEvent e2) {
                        int speedAmplifier = 1;
                        PotionEffect currentSpeed = player.getPotionEffect(PotionEffectType.SPEED);
                        if (currentSpeed == null || currentSpeed.getAmplifier() < speedAmplifier) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, POTION_DURATION_TICKS, speedAmplifier, true, false));
                        }
                        if (e2.getFrom().getY() < e2.getTo().getY()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0, true, false));
                        }
                        PotionEffect jumpBoost = player.getPotionEffect(PotionEffectType.JUMP_BOOST);
                        if (jumpBoost == null || jumpBoost.getAmplifier() < 1) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, POTION_DURATION_TICKS, 1, true, false));
                        }
                    }
                }));

        // Tier 5: Doble salto con cooldown + sprint boost
        perks.setPerk(5, new SkillPerk("Doble salto y sprint", "Doble salto con cooldown y sprint boost.",
                (player, event) -> {
                    if (event instanceof PlayerToggleSneakEvent e) {
                        if (!e.isSneaking()) return;

                        UUID uuid = player.getUniqueId();
                        long now = System.currentTimeMillis();
                        WeakHashMap<UUID, Long> cooldowns = AgilityPerksListener.doubleJumpCooldowns;

                        Long lastUsed = cooldowns.getOrDefault(uuid, 0L);
                        if (now - lastUsed < COOLDOWN_MS) return;

                        cooldowns.put(uuid, now);

                        Vector jumpVelocity = player.getLocation().getDirection().multiply(1.3).setY(1);
                        player.setVelocity(jumpVelocity);
                        player.sendMessage(lang.getRaw("agility_boost_enabled"));
                    } else if (event instanceof PlayerMoveEvent e2) {
                        if (player.isSprinting()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, POTION_DURATION_TICKS, 2, true, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, POTION_DURATION_TICKS, 2, true, false));
                        }
                        // También aplica perks de tiers anteriores
                        int speedAmplifier = 1;
                        PotionEffect currentSpeed = player.getPotionEffect(PotionEffectType.SPEED);
                        if (currentSpeed == null || currentSpeed.getAmplifier() < speedAmplifier) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, POTION_DURATION_TICKS, speedAmplifier, true, false));
                        }
                        if (e2.getFrom().getY() < e2.getTo().getY()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0, true, false));
                        }
                        PotionEffect jumpBoost = player.getPotionEffect(PotionEffectType.JUMP_BOOST);
                        if (jumpBoost == null || jumpBoost.getAmplifier() < 1) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, POTION_DURATION_TICKS, 1, true, false));
                        }
                    } else if (event instanceof EntityDamageEvent e3) {
                        if (e3.getCause() == EntityDamageEvent.DamageCause.FALL) {
                            double reducedDamage = e3.getDamage() * DAMAGE_REDUCTION_FACTOR;
                            e3.setDamage(reducedDamage);
                        } else if (Math.random() < DAMAGE_EVADE_CHANCE) {
                            e3.setCancelled(true);
                            player.sendMessage(lang.getRaw("damage_evaded"));
                        }
                    }
                }));
    }
}
