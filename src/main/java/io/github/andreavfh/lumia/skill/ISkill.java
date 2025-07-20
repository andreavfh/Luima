package io.github.andreavfh.lumia.skill;

/**
        * Represents a skill in the game.
        * Provides methods to retrieve skill properties and manage skill progression.
        */
public interface ISkill {

    /**
     * Returns the type of the skill.
     *
     * @return The skill type.
     */
    SkillType getType();

    /**
     * Returns the current level of the skill.
     *
     * @return The current skill level.
     */
    int getLevel();

    /**
     * Returns the current XP of the skill.
     *
     * @return The current XP value.
     */
    double getCurrentXP();

    /**
     * Calculates the XP required for the next level.
     *
     * @return The XP required to reach the next level.
     */
    double getXPForNextLevel();

    /**
     * Adds XP to the skill and handles level-up logic.
     *
     * @param amount The amount of XP to add.
     */
    void addXP(double amount);

    /**
     * Checks if the skill can level up based on the current XP.
     *
     * @return True if the skill can level up, false otherwise.
     */
    boolean canLevelUp();

    /**
     * Handles the level-up process, including XP adjustment and notifications.
     */
    void levelUp();

    /**
     * Calculates the XP required for a specific level.
     *
     * @param level The level to calculate the XP for.
     * @return The XP required for the specified level.
     */
    double getXPRequiredForLevel(int level);

    /**
     * Returns the progress percentage towards the next level.
     *
     * @return The progress percentage as a value between 0.0 and 1.0.
     */
    double getProgressPercent();

    /**
     * Calculates the skill's tier based on its level.
     *
     * @return The tier of the skill (maximum is 5).
     */
    int getTier();

    /**
     * Retrieves the rank of the skill based on its tier and type.
     *
     * @return The rank of the skill as a string.
     */
    String getRank();
}