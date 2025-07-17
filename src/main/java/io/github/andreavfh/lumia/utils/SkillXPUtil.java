package io.github.andreavfh.lumia.utils;

public class SkillXPUtil {

    public static double getXPRequiredForLevel(int level) {
        if (level <= 1) return 50;
        return 50 * Math.pow(1.25, level - 1);
    }
}

