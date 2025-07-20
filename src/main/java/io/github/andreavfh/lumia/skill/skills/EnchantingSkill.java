package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class EnchantingSkill extends AbstractSkill {
    /**
     * Constructs a new EnchantingSkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public EnchantingSkill(Player player) {
        super(SkillType.ENCHANTING, player);
    }
}