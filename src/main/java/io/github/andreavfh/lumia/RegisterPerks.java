package io.github.andreavfh.lumia;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillMeta;
import io.github.andreavfh.lumia.skill.SkillType;

import java.lang.reflect.Method;
import java.util.Arrays;

public class RegisterPerks {

    private final SkillManager skillManager;
    private final Lumia plugin;

    public RegisterPerks(SkillManager skillManager, Lumia plugin) {
        this.skillManager = skillManager;
        this.plugin = plugin;
    }

    public void registerAllPerks() {
        for (SkillType type : SkillType.values()) {
            try {
                SkillMeta meta = type.getMeta();
                String className = "io.github.andreavfh.lumia.listeners." + type.name().toLowerCase() + "." + capitalize(type.name()) + "PerksListener";
                Class<?> clazz = Class.forName(className);

                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().startsWith("register") && java.lang.reflect.Modifier.isStatic(method.getModifiers())) {

                        // Detecta argumentos esperados por el método
                        Object[] fullArgs = Arrays.stream(method.getParameterTypes()).map(param -> {
                            if (param == SkillMeta.class) return meta;
                            if (param == SkillManager.class) return skillManager;
                            if (param == Lumia.class) return plugin;
                            throw new IllegalArgumentException("Unknown parameter type: " + param.getName());
                        }).toArray();

                        method.invoke(null, fullArgs);
                        break; // Solo invocamos el primer método válido
                    }
                }

            } catch (Exception e) {
                System.err.println("Error registering perks for skill: " + type.name());
                e.printStackTrace();
            }
        }
    }

    // Utilidad para capitalizar tipo de habilidad (e.g., FISHING → Fishing)
    private String capitalize(String input) {
        input = input.toLowerCase();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
