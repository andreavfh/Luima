package io.github.andreavfh.lumia.listeners.smithing;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class SmithingListener implements Listener {

    private final SkillManager skillManager;

    public SmithingListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onSmithing(PrepareSmithingEvent event) {
        if (event.getInventory().getViewers().isEmpty()) return;

        HumanEntity viewer = event.getInventory().getViewers().get(0);
        if (viewer instanceof Player player) {
            skillManager.getHolder(player)
                    .getSkill(SkillType.SMITHING).addXP(20);
        }
    }
}
