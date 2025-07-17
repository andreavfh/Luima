package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class FarmingListener implements Listener {

    private final SkillManager skillManager;

    public FarmingListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (isCrop(block.getType())) {
            skillManager.getHolder(player)
                    .getSkill(SkillType.FARMING)
                    .addXP(5);
        }
    }

    private boolean isCrop(Material type) {
        return switch (type) {
            case WHEAT, CARROTS, POTATOES, BEETROOTS, NETHER_WART, SWEET_BERRY_BUSH, COCOA -> true;
            default -> false;
        };
    }
}
