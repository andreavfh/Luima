package io.github.andreavfh.lumia.skill;

import org.bukkit.entity.Player;
import java.util.Map;

/**
 * Represents a skill perk that can be applied to a player.
 * A perk includes a name, description, and an action to execute when applied.
 */
public class SkillPerk {

    private final String name;
    private final String description;
    private final java.util.function.BiConsumer<Player, Object> action;

    /**
     * Constructs a new SkillPerk instance.
     *
     * @param name        The name of the perk.
     * @param description The description of the perk.
     * @param action      The action to execute when the perk is applied.
     */
    public SkillPerk(String name, String description, java.util.function.BiConsumer<Player, Object> action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }

    /**
     * Applies the perk to the specified player without additional context.
     *
     * @param player The player to whom the perk is applied.
     */
    public void apply(Player player) {
        apply(player, null);
    }

    /**
     * Applies the perk to the specified player with additional context.
     *
     * @param player  The player to whom the perk is applied.
     * @param context Additional context for the perk action (e.g., an event).
     */
    public void apply(Player player, Object context) {
        action.accept(player, context);
    }

    /**
     * Retrieves the name of the perk.
     *
     * @return The name of the perk.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the description of the perk.
     *
     * @return The description of the perk.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Applies all perks from tier 1 up to the specified maximum tier.
     *
     * @param player   The player to whom the perks are applied.
     * @param context  Additional context for the perk actions.
     * @param perks    The SkillPerks instance containing the perks.
     * @param maxTier  The maximum tier up to which perks are applied.
     */
    public static void applyAllUpToTier(Player player, Object context, SkillPerks perks, int maxTier) {
        for (int i = 1; i <= maxTier; i++) {
            SkillPerk perk = perks.getPerk(i);
            if (perk != null) {
                perk.apply(player, context);
            }
        }
    }

    public static void applyAllUpToTier(Player player, SkillPerks perks, int maxTier) {
        for (int i = 1; i <= maxTier; i++) {
            SkillPerk perk = perks.getPerk(i);
            if (perk != null) {
                perk.apply(player);
            }
        }
    }
}
