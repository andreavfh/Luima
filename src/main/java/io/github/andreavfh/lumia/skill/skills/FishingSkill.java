package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class FishingSkill extends AbstractSkill {
    /**
     * Constructs a new FishingSkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public FishingSkill(Player player) {
        super(SkillType.FISHING, player);
    }
}