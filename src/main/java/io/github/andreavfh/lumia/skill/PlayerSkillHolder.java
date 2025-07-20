package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.skill.skills.*;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public class PlayerSkillHolder implements ISkillHolder {

    private final Map<SkillType, ISkill> skills = new EnumMap<>(SkillType.class);

    private final Player player;

    /**
     * Constructs a new PlayerSkillHolder for a given player.
     *
     * @param player The player associated with this skill holder.
     */
    public PlayerSkillHolder(Player player) {
        this.player = player;

        skills.put(SkillType.COMBAT, new CombatSkill(player));
        skills.put(SkillType.FARMING, new FarmingSkill(player));
        skills.put(SkillType.MINING, new MiningSkill(player));
        skills.put(SkillType.FORAGING, new ForagingSkill(player));
        skills.put(SkillType.FISHING, new FishingSkill(player));
        skills.put(SkillType.ENCHANTING, new EnchantingSkill(player));
        skills.put(SkillType.ALCHEMY, new AlchemySkill(player));
        skills.put(SkillType.ARCHERY, new ArcherySkill(player));
        skills.put(SkillType.AGILITY, new AgilitySkill(player));
        skills.put(SkillType.SMITHING, new SmithingSkill(player));
    }

    /**
     * Retrieves the skill associated with the specified type.
     *
     * @param type The type of skill to retrieve.
     * @return The skill associated with the given type.
     */
    @Override
    public ISkill getSkill(SkillType type) {
        return skills.get(type);
    }

    /**
     * Retrieves all skills associated with the skill holder.
     *
     * @return A map containing all skills, where the key is the skill type and the value is the skill instance.
     */
    @Override
    public Map<SkillType, ISkill> getAllSkills() {
        return skills;
    }

    /**
     * Adds experience points (XP) to a specific skill.
     *
     * @param type The type of skill to add XP to.
     * @param xp   The amount of XP to add.
     */
    @Override
    public void addSkillXP(SkillType type, double xp) {
        ISkill skill = skills.get(type);
        if (skill != null) {
            skill.addXP(xp);
        }
    }

    /**
     * Calculates the total level of all skills combined.
     *
     * @return The total level of all skills.
     */
    @Override
    public int getTotalSkillLevel() {
        return skills.values().stream()
                .mapToInt(ISkill::getLevel)
                .sum();
    }
}
