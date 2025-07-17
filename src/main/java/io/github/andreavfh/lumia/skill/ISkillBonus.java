package io.github.andreavfh.lumia.skill;

public interface ISkillBonus {

    SkillType getSkillType();

    String getDescription();

    void applyBonus(ISkillHolder player);
}
