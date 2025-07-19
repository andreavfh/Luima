package io.github.andreavfh.lumia.listeners.alchemy;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.BrewEvent;

public class AlchemyListener implements Listener {

    private final SkillManager skillManager;

    public AlchemyListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        for (Player player : event.getBlock().getWorld().getPlayers()) {
            if (player.getLocation().distance(event.getBlock().getLocation()) <= 5) {
                skillManager.getHolder(player)
                        .getSkill(SkillType.ALCHEMY).addXP(10);
            }
        }
    }
}
