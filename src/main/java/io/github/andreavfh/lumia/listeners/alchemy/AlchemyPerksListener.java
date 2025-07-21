package io.github.andreavfh.lumia.listeners.alchemy;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.skill.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public class AlchemyPerksListener implements Listener {

    private final SkillManager skillManager;
    private static final Random random = new Random();

    public AlchemyPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        if (!(event.getBlock().getState() instanceof BrewingStand stand)) return;

        Location location = stand.getLocation();
        Collection<Player> nearbyPlayers = location.getWorld().getNearbyEntities(location, 5, 5, 5).stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .collect(Collectors.toList());

        for (Player player : nearbyPlayers) {
            int tier = skillManager.getHolder(player).getSkill(SkillType.ALCHEMY).getTier();
            if (tier <= 0) continue;

            SkillPerk perk = SkillType.ALCHEMY.getMeta().getPerks().getPerk(tier);
            if (perk != null) {
                perk.apply(player, event);
            }
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        if (!(event.getPotion().getShooter() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.ALCHEMY).getTier();
        if (tier <= 0) return;

        SkillPerk perk = SkillType.ALCHEMY.getMeta().getPerks().getPerk(tier);
        if (perk != null) {
            perk.apply(player, event);
        }
    }

    @EventHandler
    public void onPotionEffectGain(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.ALCHEMY).getTier();
        if (tier <= 0) return;

        SkillPerk perk = SkillType.ALCHEMY.getMeta().getPerks().getPerk(tier);
        if (perk != null) {
            perk.apply(player, event);
        }
    }

    // --- Registro estático de perks para Alchemy ---

    public static void registerAlchemyPerks(SkillMeta meta) {
        SkillPerks perks = meta.getPerks();
        LanguageConfig lang = Lumia.getPlugin(Lumia.class).getLanguageConfig();

        // Tier 1: Mejora duración efectos negativos (10%)
        perks.setPerk(1, new SkillPerk("Efectos prolongados", "Aumenta duración de efectos negativos un 10%.",
                (player, event) -> {
                    if (event instanceof EntityPotionEffectEvent e) {
                        if (!isApplicableEffect(e)) return;

                        PotionEffect oldEffect = e.getNewEffect();
                        if (oldEffect == null) return;

                        PotionEffectType type = oldEffect.getType();
                        if (isBeneficial(type)) return;

                        int newDuration = (int) (oldEffect.getDuration() * 1.10);
                        PotionEffect extended = new PotionEffect(
                                type,
                                newDuration,
                                oldEffect.getAmplifier(),
                                oldEffect.isAmbient(),
                                oldEffect.hasParticles(),
                                oldEffect.hasIcon()
                        );
                        e.setCancelled(true);
                        Player p = (Player) e.getEntity();
                        p.addPotionEffect(extended);
                    }
                }));

        // Tier 2: 15% faster brewing
        perks.setPerk(2, new SkillPerk("Cocción rápida", "15% más rápido al preparar pociones.",
                (player, event) -> {
                    if (event instanceof BrewEvent e) {
                        BrewingStand stand = (BrewingStand) e.getBlock().getState();
                        stand.setBrewingTime((int) (stand.getBrewingTime() * 0.85));
                    }
                }));

        // Tier 3: 15% chance de duplicar pociones al preparar
        perks.setPerk(3, new SkillPerk("Poción extra", "15% probabilidad de duplicar pociones al preparar.",
                (player, event) -> {
                    if (event instanceof BrewEvent e) {
                        BrewingStand stand = (BrewingStand) e.getBlock().getState();
                        for (int i = 0; i < 3; i++) {
                            ItemStack potion = stand.getInventory().getItem(i);
                            if (potion != null && potion.getType().name().contains("POTION")) {
                                if (random.nextDouble() < 0.15) {
                                    player.getInventory().addItem(potion.clone());
                                    player.sendMessage(ChatColor.LIGHT_PURPLE + lang.getRaw("alchemy_bonus_potion"));
                                }
                            }
                        }
                    }
                }));

        // Tier 4: +25% alcance a pociones lanzadas
        perks.setPerk(4, new SkillPerk("Alcance potenciado", "Las pociones lanzadas tienen 25% más alcance.",
                (player, event) -> {
                    if (event instanceof PotionSplashEvent e) {
                        ThrownPotion potion = e.getPotion();
                        Vector velocity = potion.getVelocity();
                        potion.setVelocity(velocity.multiply(1.25));
                    }
                }));

        // Tier 5: Mejora efectos beneficiosos de pociones splash (amplificador +1)
        perks.setPerk(5, new SkillPerk("Potenciador de pociones", "Mejora efectos beneficiosos de pociones splash (+1 nivel).",
                (player, event) -> {
                    if (event instanceof EntityPotionEffectEvent e) {
                        if (e.getCause() != EntityPotionEffectEvent.Cause.POTION_SPLASH) return;

                        PotionEffect effect = e.getNewEffect();
                        if (effect == null) return;

                        PotionEffectType type = effect.getType();
                        if (!isBeneficial(type)) return;

                        PotionEffect upgraded = new PotionEffect(
                                type,
                                effect.getDuration(),
                                effect.getAmplifier() + 1,
                                effect.isAmbient(),
                                effect.hasParticles(),
                                effect.hasIcon()
                        );
                        e.setCancelled(true);
                        Player p = (Player) e.getEntity();
                        p.addPotionEffect(upgraded);
                    }
                }));
    }

    // --- Helpers privados para condiciones ---

    private static boolean isBeneficial(PotionEffectType type) {
        return switch (type.getKey().getKey().toUpperCase()) {
            case "SPEED", "HASTE", "STRENGTH", "REGENERATION", "RESISTANCE",
                    "FIRE_RESISTANCE", "WATER_BREATHING", "INVISIBILITY", "NIGHT_VISION",
                    "HEALTH_BOOST", "ABSORPTION", "SATURATION", "LUCK", "HERO_OF_THE_VILLAGE",
                    "DOLPHINS_GRACE", "CONDUIT_POWER", "JUMP_BOOST" -> true;
            default -> false;
        };
    }

    private static boolean isApplicableEffect(EntityPotionEffectEvent event) {
        EntityPotionEffectEvent.Cause cause = event.getCause();
        return cause == EntityPotionEffectEvent.Cause.POTION_DRINK
                || cause == EntityPotionEffectEvent.Cause.POTION_SPLASH
                || cause == EntityPotionEffectEvent.Cause.BEACON;
    }
}
