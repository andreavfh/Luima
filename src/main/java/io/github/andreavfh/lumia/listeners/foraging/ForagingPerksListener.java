package io.github.andreavfh.lumia.listeners.foraging;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Listener encargado de aplicar las ventajas (perks) de la habilidad {@code FORAGING}
 * según el tier alcanzado por el jugador. Estas ventajas se activan al romper troncos o hojas.
 *
 * <p>Tier 1: Posibilidad de duplicar drops de madera.<br>
 * Tier 2: Probabilidad de obtener una manzana adicional al talar robles.<br>
 * Tier 3: Aplicación de efecto de rapidez al usar hachas.<br>
 * Tier 4: Mayor probabilidad de romper hojas cercanas.<br>
 * Tier 5: Replantado automático de brotes/saplings al talar.</p>
 *
 * @author byWolff_
 */
public class ForagingPerksListener implements Listener {

    private final SkillManager skillManager;
    private final Lumia plugin;

    /**
     * Constructor del listener de forrajeo.
     *
     * @param skillManager Manejador de habilidades de los jugadores.
     */
    public ForagingPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
        this.plugin = JavaPlugin.getPlugin(Lumia.class);
    }

    /**
     * Evento que se ejecuta cuando un jugador rompe un bloque.
     * Evalúa si el bloque es madera o hojas y aplica los perks correspondientes según el tier.
     *
     * @param event Evento de ruptura de bloque.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        int tier = skillManager.getHolder(player).getSkill(SkillType.FORAGING).getTier();

        if (isLog(block.getType())) {
            handleLogBreak(player, block, tier);
        }

        if (isLeaves(block.getType())) {
            handleLeafBreak(block, tier);
        }
    }

    /**
     * Lógica para aplicar perks al romper bloques de madera.
     *
     * @param player Jugador que rompió el bloque.
     * @param block Bloque de tronco roto.
     * @param tier Nivel de tier de la habilidad Foraging del jugador.
     */
    private void handleLogBreak(Player player, Block block, int tier) {
        Location location = block.getLocation();
        ItemStack tool = player.getInventory().getItemInMainHand();

        // Tier 1: Duplicar drops de madera con 10% de probabilidad
        if (tier >= 1 && Math.random() < 0.10) {
            block.getDrops(tool).forEach(drop ->
                    block.getWorld().dropItemNaturally(location, drop.clone())
            );
        }

        // Tier 2: Drop extra de manzana al talar roble con 10% de probabilidad
        if (tier >= 2 && block.getType() == Material.OAK_LOG && Math.random() < 0.10) {
            block.getWorld().dropItemNaturally(location, new ItemStack(Material.APPLE));
        }

        // Tier 3: Aplicar efecto de Haste (Rapidez) al usar hacha
        if (tier >= 3 && isAxe(tool.getType())) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.HASTE,
                    60, // 3 segundos (20 ticks por segundo)
                    0, // Nivel 1
                    true, // Ambient
                    false, // Sin partículas
                    false  // Sin icono
            ));
        }

        // Tier 5: Replantar automáticamente un sapling correspondiente 2 segundos después
        if (tier >= 5) {
            Material sapling = getCorrespondingSapling(block.getType());
            if (sapling != null) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Block current = location.getBlock();
                    if (current.getType() == Material.AIR || current.getType() == Material.CAVE_AIR) {
                        current.setType(sapling);
                        current.getWorld().playSound(location, Sound.ITEM_CROP_PLANT, 1f, 1.2f);
                        current.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location.add(0.5, 0.5, 0.5), 10);
                    }
                }, 40L); // 2 segundos (40 ticks)
            }
        }
    }

    /**
     * Lógica para romper hojas cercanas al romper una hoja.
     * Solo aplica desde el tier 4 en adelante con una probabilidad del 25%.
     *
     * @param block Bloque de hoja roto.
     * @param tier Nivel de tier de la habilidad Foraging del jugador.
     */
    private void handleLeafBreak(Block block, int tier) {
        if (tier >= 4 && Math.random() < 0.25) {
            block.breakNaturally();
        }
    }

    /**
     * Verifica si un material es un tipo de tronco.
     *
     * @param type Material a comprobar.
     * @return {@code true} si es tronco o tallo (stem), {@code false} en caso contrario.
     */
    private boolean isLog(Material type) {
        return type.name().endsWith("_LOG") || type.name().endsWith("_STEM");
    }

    /**
     * Verifica si un material es un tipo de hoja.
     *
     * @param type Material a comprobar.
     * @return {@code true} si es un bloque de hojas, {@code false} en caso contrario.
     */
    private boolean isLeaves(Material type) {
        return type.name().endsWith("_LEAVES");
    }

    /**
     * Verifica si el material proporcionado es un hacha.
     *
     * @param material Material del ítem.
     * @return {@code true} si el ítem es un hacha, {@code false} en caso contrario.
     */
    private boolean isAxe(Material material) {
        return material.name().endsWith("_AXE");
    }

    /**
     * Obtiene el sapling (brote) correspondiente al tipo de tronco proporcionado.
     *
     * @param log Tipo de madera.
     * @return Tipo de sapling relacionado, o {@code null} si no hay coincidencia.
     */
    private Material getCorrespondingSapling(Material log) {
        return switch (log) {
            case OAK_LOG -> Material.OAK_SAPLING;
            case SPRUCE_LOG -> Material.SPRUCE_SAPLING;
            case BIRCH_LOG -> Material.BIRCH_SAPLING;
            case JUNGLE_LOG -> Material.JUNGLE_SAPLING;
            case ACACIA_LOG -> Material.ACACIA_SAPLING;
            case DARK_OAK_LOG -> Material.DARK_OAK_SAPLING;
            case MANGROVE_LOG -> Material.MANGROVE_PROPAGULE;
            case CHERRY_LOG -> Material.CHERRY_SAPLING;
            case CRIMSON_STEM -> Material.CRIMSON_FUNGUS;
            case WARPED_STEM -> Material.WARPED_FUNGUS;
            default -> null;
        };
    }
}
