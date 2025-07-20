package io.github.andreavfh.lumia.skill;

import java.util.Map;

public interface ISkillHolder {

    /**
     * Retrieves the skill associated with the specified type.
     *
     * @param type The type of skill to retrieve.
     * @return The skill associated with the given type.
     */
    ISkill getSkill(SkillType type);

    /**
     * Retrieves all skills associated with the skill holder.
     *
     * @return A map containing all skills, where the key is the skill type and the value is the skill instance.
     */
    Map<SkillType, ISkill> getAllSkills();

    /**
     * Adds experience points (XP) to a specific skill.
     *
     * @param type The type of skill to add XP to.
     * @param xp   The amount of XP to add.
     */
    void addSkillXP(SkillType type, double xp);

    /**
     * Calculates the total level of all skills combined.
     *
     * @return The total level of all skills.
     */
    int getTotalSkillLevel();
}
