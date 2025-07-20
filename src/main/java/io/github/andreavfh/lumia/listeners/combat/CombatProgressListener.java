package io.github.andreavfh.lumia.listeners.combat;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CombatProgressListener implements Listener {

    private final SkillManager skillManager;

    public CombatProgressListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onKill(EntityDamageByEntityEvent event) {
        if (event.getEntity().isDead() && event.getDamager() instanceof Player player) {
            Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
            double xpPerAction = plugin.getPluginConfig().getXpPerAction(SkillType.COMBAT);

            skillManager.getHolder(player)
                    .getSkill(SkillType.COMBAT)
                    .addXP(xpPerAction);
        }
    }
}
