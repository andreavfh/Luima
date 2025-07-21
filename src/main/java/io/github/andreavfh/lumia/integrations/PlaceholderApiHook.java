package io.github.andreavfh.lumia.integrations;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.*;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Integration hook for PlaceholderAPI.
 * Provides custom placeholders for the Lumia plugin, allowing players to retrieve
 * skill-related information such as levels, XP, tiers, and ranks.
 */
public class PlaceholderApiHook extends PlaceholderExpansion {

    private final Lumia plugin;
    private final SkillManager skillManager;

    /**
     * Constructs a new PlaceholderApiHook instance.
     *
     * @param plugin       The Lumia plugin instance.
     * @param skillManager The SkillManager instance for managing player skills.
     */
    public PlaceholderApiHook(Lumia plugin, SkillManager skillManager) {
        this.plugin = plugin;
        this.skillManager = skillManager;
    }

    /**
     * Returns the identifier for the placeholders.
     * Placeholders will use the format %lumia_<something>%.
     *
     * @return The identifier string.
     */
    @Override
    public @NotNull String getIdentifier() {
        return "lumia";
    }

    /**
     * Returns the author of the PlaceholderAPI expansion.
     *
     * @return The author's name.
     */
    @Override
    public @NotNull String getAuthor() {
        return "andreavfh";
    }

    /**
     * Returns the version of the PlaceholderAPI expansion.
     *
     * @return The version string.
     */
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * Indicates whether the expansion should persist across server reloads.
     *
     * @return True if the expansion should persist, false otherwise.
     */
    @Override
    public boolean persist() {
        return true;
    }

    /**
     * Handles placeholder requests and returns the corresponding value.
     * Supported placeholders include:
     * <ul>
     *   <li>%lumia_level_<skill>%: Retrieves the level of the specified skill.</li>
     *   <li>%lumia_xp_<skill>%: Retrieves the current XP of the specified skill.</li>
     *   <li>%lumia_tier_<skill>%: Retrieves the tier of the specified skill.</li>
     *   <li>%lumia_rank_<skill>%: Retrieves the rank of the specified skill.</li>
     *   <li>%lumia_name_<skill>%: Retrieves the display name of the specified skill.</li>
     * </ul>
     *
     * @param player The player requesting the placeholder.
     * @param params The placeholder parameters.
     * @return The value of the placeholder, or an empty string if invalid.
     */
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        String[] parts = params.split("_");
        if (parts.length < 2) return "";

        String stat = parts[0]; // level, xp, tier, rank, name
        String skillName = parts[1]; // fishing, combat, etc.

        SkillType type = SkillType.fromKey(skillName.toLowerCase());
        if (type == null) return "";

        SkillMeta meta = type.getMeta();
        PlayerSkillHolder skillHolder = skillManager.getHolder(player);
        ISkill skill = skillHolder.getSkill(type);

        return switch (stat.toLowerCase()) {
            case "level" -> String.valueOf(skill.getLevel());
            case "xp" -> String.valueOf((int) skill.getCurrentXP());
            case "tier" -> String.valueOf(skill.getTier());
            case "rank" -> skill.getRank();
            case "name" -> meta.getDisplayName();
            default -> "";
        };
    }
}