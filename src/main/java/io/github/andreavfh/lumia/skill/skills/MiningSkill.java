package io.github.andreavfh.lumia.skill.skills;


import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class MiningSkill extends AbstractSkill {
    /**
     * Constructs a new MiningSkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public MiningSkill(Player player) {
        super(SkillType.MINING, player);
    }
}
