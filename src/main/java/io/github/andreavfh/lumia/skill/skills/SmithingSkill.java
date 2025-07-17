package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class SmithingSkill extends AbstractSkill {
    public SmithingSkill(Player player) {
        super(SkillType.SMITHING, player);
    }
}