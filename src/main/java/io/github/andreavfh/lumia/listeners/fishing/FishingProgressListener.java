package io.github.andreavfh.lumia.listeners.fishing;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FishingProgressListener implements Listener {

    private final SkillManager skillManager;

    public FishingProgressListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() != null) {
            Player player = event.getPlayer();
            Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
            double xpPerAction = plugin.getPluginConfig().getXpPerAction(SkillType.FISHING);

            skillManager.getHolder(player)
                    .getSkill(SkillType.FISHING)
                    .addXP(xpPerAction);
        }
    }
}
