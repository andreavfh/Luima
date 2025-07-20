package io.github.andreavfh.lumia.listeners.mining;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.Config;
import io.github.andreavfh.lumia.skill.ISkill;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import io.github.andreavfh.lumia.utils.MiningValidator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MiningProgressListener implements Listener {

    private final SkillManager skillManager;

    public MiningProgressListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        MiningValidator validator = new MiningValidator();
        Material block = event.getBlock().getType();
        Material tool = player.getInventory().getItemInMainHand().getType();
        Config config = JavaPlugin.getPlugin(Lumia.class).getPluginConfig();
        ISkill skill = skillManager.getHolder(player).getSkill(SkillType.MINING);

        String blockName = block.name();

        String normalized = blockName
                .replace("DEEPSLATE_", "")
                .replace("NETHER_", "")
                .replace("RAW_", "")
                .replace("INFESTED_", "");

        Material normalizedMaterial = Material.matchMaterial(normalized);
        if (normalizedMaterial != null && validator.isValidTool(tool, normalizedMaterial)) {
            double xp = config.getXpForMaterial(SkillType.MINING, normalizedMaterial);
            skill.addXP(xp);
        }
    }
}
