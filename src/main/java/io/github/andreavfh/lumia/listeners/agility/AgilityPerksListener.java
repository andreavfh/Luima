package io.github.andreavfh.lumia.listeners.agility;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * Listener que aplica perks especiales para la skill de Agilidad (Agility) según el nivel/tier del jugador.
 * Los perks van desde aumento de velocidad hasta doble salto con cooldown.
 */
public class AgilityPerksListener implements Listener {

    private static final int COOLDOWN_MS = 5000;
    private static final int POTION_DURATION_TICKS = 100;
    private static final double DAMAGE_REDUCTION_FACTOR = 0.7;
    private static final double DAMAGE_EVADE_CHANCE = 0.10;

    private final SkillManager skillManager;
    private final WeakHashMap<UUID, Long> doubleJumpCooldowns = new WeakHashMap<>();

    public AgilityPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    private int getTier(Player player) {
        return skillManager.getHolder(player).getSkill(SkillType.AGILITY).getTier();
    }
    /**
     * Evento que maneja la aplicación de perks en movimiento del jugador.
     * @param event Evento PlayerMoveEvent.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        int tier = skillManager.getHolder(player).getSkill(SkillType.AGILITY).getTier();

        if (tier == 0) return;

        applyBaseSpeed(player, tier);
        applyJumpAndSlowFalling(player, tier, event);
        applySprintBoost(player, tier);
    }

    /**
     * Evento que maneja la reducción de daño por caída según tier.
     * @param event Evento EntityDamageEvent.
     */
    @EventHandler
    public void onPlayerFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        if (getTier(player) >= 3) {
            double reducedDamage = event.getDamage() * DAMAGE_REDUCTION_FACTOR;
            event.setDamage(reducedDamage);
        }
    }

    /**
     * Evento que maneja la probabilidad de evadir daño según tier.
     * @param event Evento EntityDamageEvent.
     */
    @EventHandler
    public void onPlayerDamageEvade(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Lumia plugin = JavaPlugin.getPlugin(io.github.andreavfh.lumia.Lumia.class);
        LanguageConfig lang = plugin.getLanguageConfig();

        if (getTier(player) >= 4 && Math.random() < DAMAGE_EVADE_CHANCE) {
            event.setCancelled(true);
            player.sendMessage(lang.getRaw("damage_evaded"));
        }
    }

    /**
     * Evento que permite el doble salto con cooldown.
     * @param event Evento PlayerToggleSneakEvent.
     */
    @EventHandler
    public void onPlayerDoubleJump(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (getTier(player) < 5 || !event.isSneaking()) return;

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        long lastUsed = doubleJumpCooldowns.getOrDefault(uuid, 0L);

        if (now - lastUsed < COOLDOWN_MS) return;

        doubleJumpCooldowns.put(uuid, now);
        performDoubleJump(player);
        player.sendMessage("§a¡Impulso ágil activado!");
    }


    private void applyBaseSpeed(Player player, int tier) {
        int speedAmplifier = (tier >= 2) ? 1 : 0;
        PotionEffect currentSpeed = player.getPotionEffect(PotionEffectType.SPEED);

        if (currentSpeed == null || currentSpeed.getAmplifier() < speedAmplifier) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, POTION_DURATION_TICKS, speedAmplifier, true, false));
        }
    }

    private void applyJumpAndSlowFalling(Player player, int tier, PlayerMoveEvent event) {
        if (tier >= 2 && event.getFrom().getY() < event.getTo().getY()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0, true, false));
        }

        if (tier >= 4) {
            PotionEffect jumpBoost = player.getPotionEffect(PotionEffectType.JUMP_BOOST);
            if (jumpBoost == null || jumpBoost.getAmplifier() < 1) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, POTION_DURATION_TICKS, 1, true, false));
            }
        }
    }

    private void applySprintBoost(Player player, int tier) {
        if (tier >= 5 && player.isSprinting()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, POTION_DURATION_TICKS, 2, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, POTION_DURATION_TICKS, 2, true, false));
        }
    }

    /**
     * Realiza el doble salto del jugador con un vector hacia adelante y hacia arriba.
     * @param player Jugador.
     */
    private void performDoubleJump(Player player) {
        Vector jumpVelocity = player.getLocation().getDirection().multiply(1.3).setY(1);
        player.setVelocity(jumpVelocity);
    }
}
