package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class FarmingSkill extends AbstractSkill {
    /**
     * Constructs a new FarmingSkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public FarmingSkill(Player player) {
        super(SkillType.FARMING, player);
    }
}
