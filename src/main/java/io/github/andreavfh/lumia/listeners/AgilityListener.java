package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

public class AgilityListener implements Listener {

    private final SkillManager skillManager;

    public AgilityListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            skillManager.getHolder(player)
                    .getSkill(SkillType.AGILITY).addXP(0.2);
        }
    }
}
