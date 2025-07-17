package io.github.andreavfh.lumia.skill;

public interface ISkill {
    SkillType getType();
    int getLevel();
    double getCurrentXP();
    double getXPForNextLevel();
    void addXP(double amount);
    boolean canLevelUp();
    void levelUp();
    double getXPRequiredForLevel(int level);
    double getProgressPercent();
    int getTier();
    String getRank();
}
