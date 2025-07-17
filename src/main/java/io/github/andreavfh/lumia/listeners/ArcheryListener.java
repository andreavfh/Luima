package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

public class ArcheryListener implements Listener {

    private final SkillManager skillManager;

    public ArcheryListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }


    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player player) {
            skillManager.getHolder(player)
                    .getSkill(SkillType.ARCHERY).addXP(5);
        }
    }
}
