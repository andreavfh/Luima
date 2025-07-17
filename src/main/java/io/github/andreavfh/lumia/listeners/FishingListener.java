package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListener implements Listener {

    private final SkillManager skillManager;

    public FishingListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() != null) {
            Player player = event.getPlayer();
            skillManager.getHolder(player)
                    .getSkill(SkillType.FISHING)
                    .addXP(15);
        }
    }
}
