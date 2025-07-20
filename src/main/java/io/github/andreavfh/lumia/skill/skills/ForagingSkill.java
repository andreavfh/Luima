package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class ForagingSkill extends AbstractSkill {
    /**
     * Constructs a new ForagingSkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public ForagingSkill(Player player) {
        super(SkillType.FORAGING, player);
    }
}