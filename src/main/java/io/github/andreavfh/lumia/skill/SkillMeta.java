package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents metadata for a specific skill type.
 * Provides methods to retrieve the display name and description of the skill
 * based on the plugin's language configuration.
 */
public class SkillMeta {

    private final SkillType type;
    private final LanguageConfig languageConfig;
    private final SkillPerks perks = new SkillPerks();

    /**
     * Constructs a new SkillMeta instance for the specified skill type.
     *
     * @param type The skill type associated with this metadata.
     */
    public SkillMeta(SkillType type) {
        languageConfig = JavaPlugin.getPlugin(Lumia.class).getLanguageConfig();
        this.type = type;
    }

    /**
     * Retrieves the skill type associated with this metadata.
     *
     * @return The skill type.
     */
    public SkillType getType() {
        return type;
    }

    /**
     * Retrieves the display name of the skill from the language configuration.
     *
     * @return The display name of the skill.
     */
    public String getDisplayName() {
        return languageConfig.getRaw("skill_" + type.getKey() + "_name");
    }

    /**
     * Retrieves the description of the skill from the language configuration.
     *
     * @return The description of the skill.
     */
    public String getDescription() {
        return languageConfig.getRaw("skill_" + type.getKey() + "_description");
    }

    public SkillPerks getPerks() {
        return perks;
    }

}
