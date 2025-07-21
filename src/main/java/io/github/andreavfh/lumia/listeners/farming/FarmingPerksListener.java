package io.github.andreavfh.lumia.listeners.farming;

import io.github.andreavfh.lumia.skill.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FarmingPerksListener implements Listener {

    private final SkillManager skillManager;
    private static final Random random = new Random();

    public FarmingPerksListener(@NotNull SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onCropHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!isFarmCrop(block.getType()))
            return;

        var holder = skillManager.getHolder(player);
        var meta = holder.getSkill(SkillType.FARMING).getType().getMeta();
        int tier = holder.getSkill(SkillType.FARMING).getTier();
        SkillPerks perks = meta.getPerks();

        if (perks != null) {
            SkillPerk.applyAllUpToTier(player, event, meta.getPerks(), tier);
        }
    }

    private boolean isFarmCrop(@NotNull Material material) {
        return switch (material) {
            case WHEAT, CARROTS, POTATOES, BEETROOTS, NETHER_WART -> true;
            default -> false;
        };
    }

    public static void registerFarmingPerks(SkillMeta meta, SkillManager manager) {
        SkillPerks perks = meta.getPerks();

        perks.setPerk(1, new SkillPerk("Duplicar cosecha", "10% probabilidad de duplicar drops de cultivos.",
                (player, event) -> {
                    if (random.nextDouble() < 0.10)
                        duplicateDrops((BlockBreakEvent) event);
                }));

        perks.setPerk(2, new SkillPerk("Duplicar + XP", "15% de duplicar drops y +0.1 XP al cosechar.",
                (player, event) -> {
                    if (random.nextDouble() < 0.15)
                        duplicateDrops((BlockBreakEvent) event);

                    manager.getHolder(player).getSkill(SkillType.FARMING).addXP(0.1);
                }));

        perks.setPerk(3, new SkillPerk("Semillas raras", "20% de duplicar y 5% de tirar semillas raras.",
                (player, event) -> {
                    if (random.nextDouble() < 0.20)
                        duplicateDrops((BlockBreakEvent) event);
                    if (random.nextDouble() < 0.05)
                        dropRareSeed((BlockBreakEvent) event);
                }));

        perks.setPerk(4, new SkillPerk("Cosecha eficiente", "30% de duplicar drops.",
                (player, event) -> {
                    if (random.nextDouble() < 0.30)
                        duplicateDrops((BlockBreakEvent) event);
                    // TODO: añadir perk de crecimiento rápido
                }));

        perks.setPerk(5, new SkillPerk("Maestro agricultor", "40% de duplicar y 10% de tirar semillas raras.",
                (player, event) -> {
                    if (random.nextDouble() < 0.40)
                        duplicateDrops((BlockBreakEvent) event);
                    if (random.nextDouble() < 0.10)
                        dropRareSeed((BlockBreakEvent) event);
                }));
    }

    private static void duplicateDrops(BlockBreakEvent event) {
        event.setDropItems(false);
        for (ItemStack drop : event.getBlock().getDrops()) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop.clone());
        }
    }

    private static void dropRareSeed(BlockBreakEvent event) {
        ItemStack rareSeed = new ItemStack(Material.MELON_SEEDS);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), rareSeed);
    }
}
