package io.github.andreavfh.lumia.listeners.foraging;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.*;
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

public class ForagingPerksListener implements Listener {

    private final SkillManager skillManager;
    private final Lumia plugin;

    public ForagingPerksListener(SkillManager skillManager, Lumia plugin) {
        this.skillManager = skillManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        int tier = skillManager.getHolder(player).getSkill(SkillType.FORAGING).getTier();

        if (tier <= 0) return;

        SkillMeta meta = SkillType.FORAGING.getMeta();

        if (isLog(block.getType()) && (tier != 4)) {
            SkillPerk.applyAllUpToTier(player, event, meta.getPerks(), tier);
        } else if (isLeaves(block.getType()) && tier >= 4) {
            SkillPerk.applyAllUpToTier(player, event, meta.getPerks(), tier);
        }
    }

    public static void registerForagingPerks(SkillMeta meta, Lumia plugin) {

        meta.getPerks().setPerk(1, new SkillPerk(
                "Doble drop de madera",
                "10% de probabilidad de duplicar drops de madera al talar.",
                (player, context) -> {
                    if (context instanceof BlockBreakEvent event) {
                        Block block = event.getBlock();
                        if (Math.random() < 0.10) {
                            ItemStack tool = player.getInventory().getItemInMainHand();
                            block.getDrops(tool).forEach(drop ->
                                    block.getWorld().dropItemNaturally(block.getLocation(), drop.clone())
                            );
                        }
                    }
                }
        ));

        meta.getPerks().setPerk(2, new SkillPerk(
                "Manzana extra",
                "10% de probabilidad de soltar una manzana extra al talar robles.",
                (player, context) -> {
                    if (context instanceof BlockBreakEvent event) {
                        Block block = event.getBlock();
                        if (block.getType() == Material.OAK_LOG && Math.random() < 0.10) {
                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE));
                        }
                    }
                }
        ));

        meta.getPerks().setPerk(3, new SkillPerk(
                "Rapidez al talar",
                "Obtienes Haste (rapidez) I por 3 segundos cuando usas hacha.",
                (player, context) -> {
                    if (context instanceof BlockBreakEvent event) {
                        ItemStack tool = player.getInventory().getItemInMainHand();
                        if (isAxe(tool.getType())) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 60, 0, true, false, false));
                        }
                    }
                }
        ));

        // Tier 4 es para hojas, rompimiento extra
        meta.getPerks().setPerk(4, new SkillPerk(
                "Rompimiento de hojas",
                "25% de probabilidad de romper hojas cercanas cuando rompes una hoja.",
                (player, context) -> {
                    if (context instanceof BlockBreakEvent event) {
                        Block block = event.getBlock();
                        if (Math.random() < 0.25) {
                            block.breakNaturally();
                        }
                    }
                }
        ));

        meta.getPerks().setPerk(5, new SkillPerk(
                "Replantado automático",
                "Replanta automáticamente un sapling tras talar un árbol.",
                (player, context) -> {
                    if (context instanceof BlockBreakEvent event) {
                        Block block = event.getBlock();
                        Material sapling = getCorrespondingSapling(block.getType());
                        if (sapling != null) {
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                Block current = block.getLocation().getBlock();
                                if (current.getType() == Material.AIR || current.getType() == Material.CAVE_AIR) {
                                    current.setType(sapling);
                                    current.getWorld().playSound(current.getLocation(), Sound.ITEM_CROP_PLANT, 1f, 1.2f);
                                    current.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, current.getLocation().add(0.5, 0.5, 0.5), 10);
                                }
                            }, 40L);
                        }
                    }
                }
        ));
    }

    // Helpers

    private static boolean isLog(Material type) {
        return type.name().endsWith("_LOG") || type.name().endsWith("_STEM");
    }

    private static boolean isLeaves(Material type) {
        return type.name().endsWith("_LEAVES");
    }

    private static boolean isAxe(Material material) {
        return material.name().endsWith("_AXE");
    }

    private static Material getCorrespondingSapling(Material log) {
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
