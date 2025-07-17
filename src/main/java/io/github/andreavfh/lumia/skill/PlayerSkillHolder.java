package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.skill.skills.*;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public class PlayerSkillHolder implements ISkillHolder {

    private final Map<SkillType, ISkill> skills = new EnumMap<>(SkillType.class);

    private final Player player;

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

    @Override
    public ISkill getSkill(SkillType type) {
        return skills.get(type);
    }

    @Override
    public Map<SkillType, ISkill> getAllSkills() {
        return skills;
    }

    @Override
    public void addSkillXP(SkillType type, double xp) {
        ISkill skill = skills.get(type);
        if (skill != null) {
            skill.addXP(xp);
        }
    }

    @Override
    public int getTotalSkillLevel() {
        return skills.values().stream()
                .mapToInt(ISkill::getLevel)
                .sum();
    }
}
