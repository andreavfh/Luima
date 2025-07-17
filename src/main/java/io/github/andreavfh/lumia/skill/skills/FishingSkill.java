package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class FishingSkill extends AbstractSkill {
    public FishingSkill(Player player) {
        super(SkillType.FISHING, player);
    }
}