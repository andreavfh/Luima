package io.github.andreavfh.lumia.listeners.enchanting;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantingProgressListener implements Listener {

    private final SkillManager skillManager;

    public EnchantingProgressListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }


    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        double xpPerAction = plugin.getPluginConfig().getXpPerAction(SkillType.ENCHANTING);

        skillManager.getHolder(event.getEnchanter())
                .getSkill(SkillType.ENCHANTING).addXP(xpPerAction);
    }
}
