package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class AlchemySkill extends AbstractSkill {
    public AlchemySkill(Player player) {
        super(SkillType.ALCHEMY, player);
    }

}
