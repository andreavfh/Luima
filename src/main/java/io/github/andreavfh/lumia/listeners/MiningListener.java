package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MiningListener implements Listener {

    private final SkillManager skillManager;

    public MiningListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material block = event.getBlock().getType();

        switch (block) {
            case COAL_ORE, IRON_ORE, COPPER_ORE, GOLD_ORE, REDSTONE_ORE, LAPIS_ORE, DIAMOND_ORE, EMERALD_ORE -> {
                skillManager.getHolder(player)
                        .getSkill(SkillType.MINING)
                        .addXP(10);
            }
        }
    }
}
