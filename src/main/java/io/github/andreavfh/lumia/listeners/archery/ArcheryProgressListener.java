package io.github.andreavfh.lumia.listeners.archery;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ArcheryProgressListener implements Listener {

    private final SkillManager skillManager;

    public ArcheryProgressListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }


    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player player) {
            Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
            double xpPerAction = plugin.getPluginConfig().getXpPerAction(SkillType.ARCHERY);

            skillManager.getHolder(player)
                    .getSkill(SkillType.ARCHERY).addXP(xpPerAction);
        }
    }
}
