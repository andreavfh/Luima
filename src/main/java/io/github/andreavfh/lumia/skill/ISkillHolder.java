package io.github.andreavfh.lumia.skill;

import java.util.Map;

public interface ISkillHolder {

    ISkill getSkill(SkillType type);

    Map<SkillType, ISkill> getAllSkills();

    void addSkillXP(SkillType type, double xp);

    int getTotalSkillLevel();
}
