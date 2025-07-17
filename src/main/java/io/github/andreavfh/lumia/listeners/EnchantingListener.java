package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantingListener implements Listener {

    private final SkillManager skillManager;

    public EnchantingListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }


    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        skillManager.getHolder(event.getEnchanter())
                .getSkill(SkillType.ENCHANTING).addXP(15);
    }
}
