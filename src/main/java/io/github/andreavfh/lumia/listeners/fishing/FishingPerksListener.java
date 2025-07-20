package io.github.andreavfh.lumia.listeners.fishing;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.WeakHashMap;

public class FishingPerksListener implements Listener {

    private final SkillManager skillManager;
    private final WeakHashMap<UUID, Long> cooldowns = new WeakHashMap<>();
    private static final long DOUBLE_CATCH_COOLDOWN_MS = 10_000L; // 10 segundos

    public FishingPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        int tier = skillManager.getHolder(player).getSkill(SkillType.FISHING).getTier();

        if (tier <= 0) return; // Sin perks si tier 0

        switch (tier) {
            case 1 -> applyTier1Perks(player);
            case 2 -> applyTier2Perks(player);
            case 3 -> applyTier3Perks(player);
            case 4 -> applyTier4Perks(player);
            case 5 -> applyTier5Perks(event, player);
        }
    }

    private void applyTier1Perks(Player player) {
        // Velocidad (SPEED I) durante 3 segundos para agilizar moverse mientras pesca
        applyPotionEffect(player, PotionEffectType.SPEED, 60, 0);
    }

    private void applyTier2Perks(Player player) {
        // Velocidad (SPEED II)
        applyPotionEffect(player, PotionEffectType.SPEED, 60, 1);
    }

    private void applyTier3Perks(Player player) {
        // Suerte (LUCK I) por 3 segundos
        applyPotionEffect(player, PotionEffectType.LUCK, 60, 0);
    }

    private void applyTier4Perks(Player player) {
        // Suerte (LUCK II)
        applyPotionEffect(player, PotionEffectType.LUCK, 60, 1);
    }

    private void applyTier5Perks(PlayerFishEvent event, Player player) {
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        long lastUsed = cooldowns.getOrDefault(uuid, 0L);

        if (now - lastUsed >= DOUBLE_CATCH_COOLDOWN_MS && Math.random() < 0.15) { // 15% chance
            cooldowns.put(uuid, now);
            player.sendMessage("§a¡Has conseguido una doble captura!");

            // Obtener el ítem pescado (el pez o ítem que el jugador pescó)
            if (event.getCaught() != null && event.getCaught() instanceof org.bukkit.entity.Item) {
                org.bukkit.entity.Item caughtItemEntity = (org.bukkit.entity.Item) event.getCaught();
                org.bukkit.inventory.ItemStack caughtItem = caughtItemEntity.getItemStack();

                org.bukkit.inventory.ItemStack duplicatedItem = caughtItem.clone();

                player.getWorld().dropItemNaturally(player.getLocation(), duplicatedItem);
            }
        }
    }


    private void applyPotionEffect(Player player, PotionEffectType type, int durationTicks, int amplifier) {
        if (!player.hasPotionEffect(type)) {
            player.addPotionEffect(new PotionEffect(type, durationTicks, amplifier, true, false));
        }
    }
}
