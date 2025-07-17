package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class CombatSkill extends AbstractSkill {
    public CombatSkill(Player player) {
        super(SkillType.COMBAT, player);
    }

    @Override
    protected void onLevelUp() {
        System.out.println("Combat leveled up to " + level);
    }
}
