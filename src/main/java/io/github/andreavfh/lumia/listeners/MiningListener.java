package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.Config;
import io.github.andreavfh.lumia.skill.ISkill;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MiningListener implements Listener {

    private final SkillManager skillManager;

    public MiningListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material block = event.getBlock().getType();
        Config config = JavaPlugin.getPlugin(Lumia.class).getPluginConfig();
        ISkill skill = skillManager.getHolder(player).getSkill(SkillType.MINING);

        String blockName = block.name();

        String normalized = blockName
                .replace("DEEPSLATE_", "")
                .replace("NETHER_", "")
                .replace("RAW_", "")
                .replace("INFESTED_", "");

        Material normalizedMaterial = Material.matchMaterial(normalized);
        if (normalizedMaterial != null) {
            int xp = config.getXpPerMaterial(normalizedMaterial);
            skill.addXP(xp);
        }

    }
}
