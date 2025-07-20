package io.github.andreavfh.lumia.skill.skills;

import io.github.andreavfh.lumia.skill.AbstractSkill;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.entity.Player;

public class CombatSkill extends AbstractSkill {
    /**
     * Constructs a new CombatSkill for the specified player.
     *
     * @param player The player associated with this skill.
     */
    public CombatSkill(Player player) {
        super(SkillType.COMBAT, player);
    }

    /**
     * Called whenever the player levels up this skill.
     * Prints a message indicating the new level.
     */
    @Override
    protected void onLevelUp() {
        System.out.println("Combat leveled up to " + level);
    }
}
