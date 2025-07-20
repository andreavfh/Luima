package io.github.andreavfh.lumia.listeners.alchemy;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;

public class AlchemyPerksListener implements Listener {

    private final SkillManager skillManager;

    public AlchemyPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        if (!(event.getBlock().getState() instanceof BrewingStand stand)) return;

        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        LanguageConfig languageConfig = plugin.getLanguageConfig();

        Location location = stand.getLocation();
        Collection<Entity> nearby = location.getWorld().getNearbyEntities(location, 5, 5, 5);

        Collection<Player> players = nearby.stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .toList();

        for (Player player : players) {
            int tier = skillManager.getHolder(player).getSkill(SkillType.ALCHEMY).getTier();

            if (tier >= 2) {
                stand.setBrewingTime((int) (stand.getBrewingTime() * 0.85)); // 15% faster
            }

            if (tier >= 3) {
                for (int i = 0; i < 3; i++) {
                    ItemStack potion = stand.getInventory().getItem(i);
                    if (potion != null && potion.getType().name().contains("POTION")) {
                        if (Math.random() < 0.15) {


                            player.getInventory().addItem(potion.clone());
                            player.sendMessage(ChatColor.LIGHT_PURPLE + languageConfig.getRaw("alchemy_bonus_potion"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        if (!(potion.getShooter() instanceof Player)) return;

        Player player = (Player) potion.getShooter();
        int tier = skillManager.getHolder(player).getSkill(SkillType.ALCHEMY).getTier();

        if (tier >= 4) {
            Vector velocity = potion.getVelocity();
            potion.setVelocity(velocity.multiply(1.25)); // 25% más alcance
        }
    }

    @EventHandler
    public void onPotionEffectGain(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityPotionEffectEvent.Cause.POTION_DRINK &&
                event.getCause() != EntityPotionEffectEvent.Cause.POTION_SPLASH &&
                event.getCause() != EntityPotionEffectEvent.Cause.BEACON) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.ALCHEMY).getTier();

        if (tier >= 1 && event.getNewEffect() != null) {
            PotionEffect oldEffect = event.getNewEffect();
            PotionEffectType type = oldEffect.getType();

            if (isBeneficial(type)) return;

            int newDuration = (int) (oldEffect.getDuration() * 1.10); // +10%
            PotionEffect extendedEffect = new PotionEffect(
                    type,
                    newDuration,
                    oldEffect.getAmplifier(),
                    oldEffect.isAmbient(),
                    oldEffect.hasParticles(),
                    oldEffect.hasIcon()
            );
            event.setCancelled(true);
            player.addPotionEffect(extendedEffect); // Aplica el efecto extendido
        }
    }


    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.ALCHEMY).getTier();

        if (tier >= 5 && event.getCause() == EntityPotionEffectEvent.Cause.POTION_SPLASH) {
            PotionEffect effect = event.getNewEffect();
            if (effect != null && isBeneficial(effect.getType())) {
                PotionEffect upgraded = new PotionEffect(
                        effect.getType(),
                        effect.getDuration(),
                        effect.getAmplifier() + 1,
                        effect.isAmbient(),
                        effect.hasParticles(),
                        effect.hasIcon()
                );

                event.setCancelled(true); // Cancela el cambio normal
                player.addPotionEffect(upgraded); // Aplica el nuevo efecto mejorado
            }
        }
    }

    private boolean isBeneficial(PotionEffectType type) {
        return switch (type.getKey().getKey().toUpperCase()) {
            case "SPEED", "HASTE", "STRENGTH", "REGENERATION", "RESISTANCE",
                    "FIRE_RESISTANCE", "WATER_BREATHING", "INVISIBILITY", "NIGHT_VISION",
                    "HEALTH_BOOST", "ABSORPTION", "SATURATION", "LUCK", "HERO_OF_THE_VILLAGE",
                    "DOLPHINS_GRACE", "CONDUIT_POWER", "JUMP_BOOST" -> true;
            default -> false;
        };
    }

}
