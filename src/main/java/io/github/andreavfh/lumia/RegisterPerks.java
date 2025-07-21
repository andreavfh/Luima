package io.github.andreavfh.lumia;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillMeta;
import io.github.andreavfh.lumia.skill.SkillType;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Handles the registration of perks for all skills in the game.
 * Dynamically loads and invokes static registration methods in perk listener classes
 * based on the skill type.
 */
public class RegisterPerks {

    private final SkillManager skillManager;
    private final Lumia plugin;

    /**
     * Constructs a new RegisterPerks instance.
     *
     * @param skillManager The SkillManager instance for managing player skills.
     * @param plugin       The Lumia plugin instance.
     */
    public RegisterPerks(SkillManager skillManager, Lumia plugin) {
        this.skillManager = skillManager;
        this.plugin = plugin;
    }

    /**
     * Registers all perks for each skill type.
     * Dynamically locates and invokes static registration methods in listener classes
     * corresponding to each skill type.
     */
    public void registerAllPerks() {
        for (SkillType type : SkillType.values()) {
            try {
                SkillMeta meta = type.getMeta();
                String className = "io.github.andreavfh.lumia.listeners." + type.name().toLowerCase() + "." + capitalize(type.name()) + "PerksListener";
                Class<?> clazz = Class.forName(className);

                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().startsWith("register") && java.lang.reflect.Modifier.isStatic(method.getModifiers())) {

                        // Detects expected arguments for the method
                        Object[] fullArgs = Arrays.stream(method.getParameterTypes()).map(param -> {
                            if (param == SkillMeta.class) return meta;
                            if (param == SkillManager.class) return skillManager;
                            if (param == Lumia.class) return plugin;
                            throw new IllegalArgumentException("Unknown parameter type: " + param.getName());
                        }).toArray();

                        method.invoke(null, fullArgs);
                        break; // Invokes only the first valid method
                    }
                }

            } catch (Exception e) {
                System.err.println("Error registering perks for skill: " + type.name());
                e.printStackTrace();
            }
        }
    }

    /**
     * Capitalizes the skill type name for class name formatting.
     * Converts a skill type name (e.g., FISHING) to a capitalized format (e.g., Fishing).
     *
     * @param input The skill type name in uppercase.
     * @return The capitalized skill type name.
     */
    private String capitalize(String input) {
        input = input.toLowerCase();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}