package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class AlchemySkill extends AbstractSkill {
    /**
     * Constructs a new AlchemySkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public AlchemySkill(Player player) {
        super(SkillType.ALCHEMY, player);
    }

}
