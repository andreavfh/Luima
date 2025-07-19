package io.github.andreavfh.lumia.utils;

import org.bukkit.Material;

public class MiningValidator {
    public boolean isValidTool(Material tool, Material ore) {
        if (!tool.name().endsWith("_PICKAXE")) return false;

        int toolLevel = getToolLevel(tool);
        int requiredLevel = getRequiredLevel(ore);

        return toolLevel >= requiredLevel;
    }

    public int getToolLevel(Material tool) {
        return switch (tool) {
            case WOODEN_PICKAXE -> 1;
            case STONE_PICKAXE -> 2;
            case IRON_PICKAXE -> 3;
            case DIAMOND_PICKAXE -> 4;
            case NETHERITE_PICKAXE -> 5;
            default -> 0;
        };
    }

    public int getRequiredLevel(Material ore) {
        return switch (ore) {
            case COAL_ORE, COPPER_ORE, NETHER_QUARTZ_ORE -> 1;
            case IRON_ORE, LAPIS_ORE, REDSTONE_ORE -> 3;
            case GOLD_ORE, DIAMOND_ORE, EMERALD_ORE -> 3;
            case OBSIDIAN, ANCIENT_DEBRIS -> 4;
            default -> 1;
        };
    }

    public final boolean isOre(Material type) {
        return switch (type) {
            case COAL_ORE, IRON_ORE, COPPER_ORE, GOLD_ORE, REDSTONE_ORE,
                    LAPIS_ORE, DIAMOND_ORE, EMERALD_ORE, DEEPSLATE_COAL_ORE,
                    DEEPSLATE_IRON_ORE, DEEPSLATE_COPPER_ORE, DEEPSLATE_GOLD_ORE,
                    DEEPSLATE_REDSTONE_ORE, DEEPSLATE_LAPIS_ORE,
                    DEEPSLATE_DIAMOND_ORE, DEEPSLATE_EMERALD_ORE -> true;
            default -> false;
        };
    }
}
