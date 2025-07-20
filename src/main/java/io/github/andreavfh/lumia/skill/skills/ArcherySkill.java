package io.github.andreavfh.lumia.skill.skills;


import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class ArcherySkill extends AbstractSkill {
    /**
     * Constructs a new ArcherySkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public ArcherySkill(Player player) {
        super(SkillType.ARCHERY, player);
    }
}