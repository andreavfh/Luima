package io.github.andreavfh.lumia.listeners.alchemy;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AlchemyProgressListener implements Listener {

    private final SkillManager skillManager;

    public AlchemyProgressListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        for (Player player : event.getBlock().getWorld().getPlayers()) {
            if (player.getLocation().distance(event.getBlock().getLocation()) <= 5) {
                Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
                double xpPerAction = plugin.getPluginConfig().getXpPerAction(SkillType.ALCHEMY);

                skillManager.getHolder(player)
                        .getSkill(SkillType.ALCHEMY).addXP(xpPerAction);
            }
        }
    }
}
