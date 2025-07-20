package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.utils.Convert;
import io.github.andreavfh.lumia.utils.SkillXPUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract base implementation of the ISkill interface.
 * Handles common logic for all skill types (XP, level-ups, tiers, etc).
 */
public abstract class AbstractSkill implements ISkill {

    protected final SkillType type;
    protected final Player player;

    protected int level;
    protected double xp;

    protected final Lumia plugin;
    protected final LanguageConfig languageConfig;

    /**
     * Constructs a new skill for a given player and type.
     *
     * @param type   The type of skill.
     * @param player The player associated with the skill.
     */
    public AbstractSkill(SkillType type, Player player) {
        this.type = type;
        this.player = player;
        this.level = 1;
        this.xp = 0;
        this.plugin = JavaPlugin.getPlugin(Lumia.class);
        this.languageConfig = plugin.getLanguageConfig();
    }

/**
 * Returns the type of the skill.
 *
 * @return The skill type.
 */
    @Override
    public SkillType getType() {
        return type;
    }

    /**
     * Returns the current level of the skill.
     *
     * @return The current skill level.
     */
    @Override
    public int getLevel() {
        return level;
    }

    /**
     * Returns the current XP of the skill.
     *
     * @return The current XP value.
     */
    @Override
    public double getCurrentXP() {
        return xp;
    }

    /**
     * Calculates the XP required for the next level.
     *
     * @return The XP required to reach the next level.
     */
    @Override
    public double getXPForNextLevel() {
        return SkillXPUtil.getXPRequiredForLevel(level, type);
    }

    /**
     * Adds XP to the skill and handles level-up logic.
     *
     * @param amount The amount of XP to add.
     */
    @Override
    public void addXP(double amount) {
        this.xp += amount;

        plugin.getBossBarManager().showXP(
                player,
                type.name(),
                (int) xp,
                (int) getXPForNextLevel(),
                level
        );

        while (canLevelUp()) {
            levelUp();
        }
    }

    /**
     * Checks if the skill can level up based on the current XP.
     *
     * @return True if the skill can level up, false otherwise.
     */
    @Override
    public boolean canLevelUp() {
        return xp >= getXPForNextLevel();
    }

    /**
     * Handles the level-up process, including XP adjustment and notifications.
     */
    @Override
    public void levelUp() {
        double xpRequired = getXPForNextLevel();
        this.xp -= xpRequired;
        if (this.xp < 0) this.xp = 0;
        this.level++;
        onLevelUp();
    }

    /**
     * Calculates the XP required for a specific level.
     *
     * @param level The level to calculate the XP for.
     * @return The XP required for the specified level.
     */
    @Override
    public double getXPRequiredForLevel(int level) {
        return SkillXPUtil.getXPRequiredForLevel(level, type);
    }

    /**
     * Returns the progress percentage towards the next level.
     *
     * @return The progress percentage as a value between 0.0 and 1.0.
     */
    @Override
    public double getProgressPercent() {
        return Math.min(1.0, xp / getXPForNextLevel());
    }

    /**
     * Calculates the skill's tier based on its level.
     *
     * @return The tier of the skill (maximum is 5).
     */
    @Override
    public int getTier() {
        return Math.min(level / 10, 5);
    }

    /**
     * Retrieves the rank of the skill based on its tier and type.
     *
     * @return The rank of the skill as a string.
     */
    @Override
    public String getRank() {
        final String key = "skills_" + type.name().toLowerCase() + "_" + getTier();
        return languageConfig.getRaw(key);
    }

    /**
     * Called whenever the player levels up this skill.
     * Handles message formatting, sound playback, and notifications.
     */
    protected void onLevelUp() {
        final String skillKey = "skill_" + type.name().toLowerCase() + "_name";

        final String formerSkillName = languageConfig.getRaw(skillKey) + " " + Convert.toRoman(level - 1);
        final String currentSkillName = languageConfig.getRaw(skillKey) + " " + Convert.toRoman(level);

        final String message = languageConfig.getRaw("skills_level_up")
                .replace("{formerSkill}", formerSkillName)
                .replace("{currentSkill}", currentSkillName)
                .replace("{rank}", getRank());

        plugin.getMessageFormatter().sendMessage(player, message);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
    }
}
