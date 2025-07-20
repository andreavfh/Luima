package io.github.andreavfh.lumia.listeners.smithing;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SmithingProgressListener implements Listener {

    private final SkillManager skillManager;

    public SmithingProgressListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onSmithing(PrepareSmithingEvent event) {
        if (event.getInventory().getViewers().isEmpty()) return;
        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        double xpPerAction = plugin.getPluginConfig().getXpPerAction(SkillType.SMITHING);

        HumanEntity viewer = event.getInventory().getViewers().get(0);
        if (viewer instanceof Player player) {
            skillManager.getHolder(player)
                    .getSkill(SkillType.SMITHING).addXP(xpPerAction);
        }
    }
}
