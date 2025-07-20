package io.github.andreavfh.lumia.listeners.farming;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Listener para aplicar beneficios (perks) de la skill Farming (Agricultura).
 * Los beneficios dependen del tier de la skill (0-5).
 */
public class FarmingPerksListener implements Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FarmingPerksListener.class);

    private static final double DUPLICATE_DROP_CHANCE_TIER_1 = 0.10;
    private static final double DUPLICATE_DROP_CHANCE_TIER_2 = 0.15;
    private static final double DUPLICATE_DROP_CHANCE_TIER_3 = 0.20;
    private static final double DUPLICATE_DROP_CHANCE_TIER_4 = 0.30;
    private static final double DUPLICATE_DROP_CHANCE_TIER_5 = 0.40;

    private static final double RARE_SEED_DROP_CHANCE_TIER_3 = 0.05;
    private static final double RARE_SEED_DROP_CHANCE_TIER_5 = 0.10;

    private final SkillManager skillManager;
    private final Random random;

    /**
     * Constructor que recibe el SkillManager para acceder a la info del jugador.
     * @param skillManager gestor de skills
     */
    public FarmingPerksListener(@NotNull SkillManager skillManager) {
        this.skillManager = skillManager;
        this.random = new Random();
    }

    /**
     * Evento que se dispara cuando un jugador rompe un bloque.
     * Se aplican los beneficios relacionados con la skill Farming según el tier.
     * @param event evento de rompimiento de bloque
     */
    @EventHandler
    public void onCropHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        int tier = skillManager.getHolder(player)
                .getSkill(SkillType.FARMING)
                .getTier();

        if (tier == 0) {
            return; // Sin perks para tier 0
        }

        if (!isFarmCrop(block.getType())) {
            return; // No es cultivo, no hacer nada
        }

        try {
            switch (tier) {
                case 1 -> applyTier1Perks(event);
                case 2 -> applyTier2Perks(event, player);
                case 3 -> applyTier3Perks(event);
                case 4 -> applyTier4Perks(event);
                case 5 -> applyTier5Perks(event);
                default -> LOGGER.warn("Tier inesperado {} para Farming en jugador {}", tier, player.getName());
            }
        } catch (Exception e) {
            LOGGER.error("Error aplicando perks de Farming para jugador {}", player.getName(), e);
        }
    }

    private void applyTier1Perks(@NotNull BlockBreakEvent event) {
        if (random.nextDouble() < DUPLICATE_DROP_CHANCE_TIER_1) {
            duplicateDrops(event);
        }
    }

    private void applyTier2Perks(@NotNull BlockBreakEvent event, @NotNull Player player) {
        if (random.nextDouble() < DUPLICATE_DROP_CHANCE_TIER_2) {
            duplicateDrops(event);
        }
        // Bonus XP pequeño
        skillManager.getHolder(player).getSkill(SkillType.FARMING).addXP(0.1);
    }

    private void applyTier3Perks(@NotNull BlockBreakEvent event) {
        if (random.nextDouble() < DUPLICATE_DROP_CHANCE_TIER_3) {
            duplicateDrops(event);
        }
        maybeDropRareSeed(event, RARE_SEED_DROP_CHANCE_TIER_3);
    }

    private void applyTier4Perks(@NotNull BlockBreakEvent event) {
        if (random.nextDouble() < DUPLICATE_DROP_CHANCE_TIER_4) {
            duplicateDrops(event);
        }
        /*
         * Todo: implementar crecimiento rápido de cultivos.
         */
    }

    private void applyTier5Perks(@NotNull BlockBreakEvent event) {
        if (random.nextDouble() < DUPLICATE_DROP_CHANCE_TIER_5) {
            duplicateDrops(event);
        }
        maybeDropRareSeed(event, RARE_SEED_DROP_CHANCE_TIER_5);
    }

    /**
     * Verifica si un material es considerado un cultivo válido para Farming.
     * @param material material a verificar
     * @return true si es cultivo, false si no
     */
    private boolean isFarmCrop(@NotNull Material material) {
        return switch (material) {
            case WHEAT, CARROTS, POTATOES, BEETROOTS, NETHER_WART -> true;
            default -> false;
        };
    }

    /**
     * Duplica los drops originales del bloque.
     * @param event evento con el bloque roto
     */
    private void duplicateDrops(@NotNull BlockBreakEvent event) {
        event.setDropItems(false);
        for (ItemStack drop : event.getBlock().getDrops()) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop.clone());
        }
    }

    /**
     * Lanza una semilla rara con cierta probabilidad.
     * @param event evento de rompimiento
     * @param chance probabilidad de lanzar la semilla rara
     */
    private void maybeDropRareSeed(@NotNull BlockBreakEvent event, double chance) {
        if (random.nextDouble() < chance) {
            ItemStack rareSeed = new ItemStack(Material.MELON_SEEDS);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), rareSeed);
        }
    }
}
