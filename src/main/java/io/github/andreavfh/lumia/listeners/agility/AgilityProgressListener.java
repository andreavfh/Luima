package io.github.andreavfh.lumia.listeners.agility;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AgilityProgressListener implements Listener {

    private final SkillManager skillManager;

    public AgilityProgressListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
            double xpPerAction = plugin.getPluginConfig().getXpPerAction(SkillType.AGILITY);

            skillManager.getHolder(player)
                    .getSkill(SkillType.AGILITY).addXP(xpPerAction);
        }
    }
}
