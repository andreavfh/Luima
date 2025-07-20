package io.github.andreavfh.lumia.listeners.foraging;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class ForagingListener implements Listener {

    private final SkillManager skillManager;

    public ForagingListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material mat = event.getBlock().getType();
        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        double xpPerAction = plugin.getPluginConfig().getXpPerAction(SkillType.FORAGING);

        if (mat.toString().endsWith("_LOG")) {
            skillManager.getHolder(event.getPlayer())
                    .getSkill(SkillType.FORAGING).addXP(xpPerAction);
        }
    }
}
