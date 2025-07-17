package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.skill.SkillManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnQuitListener implements Listener {

    private final SkillManager skillManager;

    public OnQuitListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        skillManager.save(event.getPlayer().getUniqueId());
        skillManager.remove(event.getPlayer().getUniqueId());
    }
}
