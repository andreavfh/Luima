package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class AgilitySkill extends AbstractSkill {
    public AgilitySkill(Player player) {
        super(SkillType.AGILITY, player);
    }
}