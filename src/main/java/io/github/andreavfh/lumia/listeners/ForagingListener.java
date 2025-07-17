package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Material;

public class ForagingListener implements Listener {

    private final SkillManager skillManager;

    public ForagingListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material mat = event.getBlock().getType();
        if (mat.toString().endsWith("_LOG")) {
            skillManager.getHolder(event.getPlayer())
                    .getSkill(SkillType.FORAGING).addXP(4);
        }
    }
}
