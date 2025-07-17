package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    private final SkillManager skillManager;

    public CombatListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onKill(EntityDamageByEntityEvent event) {
        if (event.getEntity().isDead() && event.getDamager() instanceof Player player) {
            skillManager.getHolder(player)
                    .getSkill(SkillType.COMBAT)
                    .addXP(20);
        }
    }
}
