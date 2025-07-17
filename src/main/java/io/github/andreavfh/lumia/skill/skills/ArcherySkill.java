package io.github.andreavfh.lumia.skill.skills;


import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class ArcherySkill extends AbstractSkill {
    public ArcherySkill(Player player) {
        super(SkillType.ARCHERY, player);
    }
}