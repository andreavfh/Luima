package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class SmithingSkill extends AbstractSkill {
    /**
     * Constructs a new SmithingSkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public SmithingSkill(Player player) {
        super(SkillType.SMITHING, player);
    }
}