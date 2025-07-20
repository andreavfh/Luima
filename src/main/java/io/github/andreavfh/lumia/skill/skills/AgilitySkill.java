package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class AgilitySkill extends AbstractSkill {
    /**
     * Constructs a new AgilitySkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public AgilitySkill(Player player) {
        super(SkillType.AGILITY, player);
    }
}