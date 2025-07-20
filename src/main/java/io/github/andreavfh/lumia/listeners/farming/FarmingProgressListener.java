package io.github.andreavfh.lumia.listeners.farming;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FarmingProgressListener implements Listener {

    private final SkillManager skillManager;

    public FarmingProgressListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        double xpPerAction = plugin.getPluginConfig().getXpPerAction(SkillType.FARMING);

        if (isCrop(block.getType())) {
            skillManager.getHolder(player)
                    .getSkill(SkillType.FARMING)
                    .addXP(xpPerAction);
        }
    }

    private boolean isCrop(Material type) {
        return switch (type) {
            case WHEAT, CARROTS, POTATOES, BEETROOTS, NETHER_WART, SWEET_BERRY_BUSH, COCOA -> true;
            default -> false;
        };
    }
}
