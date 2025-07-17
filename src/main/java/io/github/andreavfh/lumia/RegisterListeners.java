package io.github.andreavfh.lumia;

import io.github.andreavfh.lumia.skill.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Set;

public class RegisterListeners {
    public void registerAllListeners(JavaPlugin plugin, SkillManager skillManager) {
        String packageName = "io.github.andreavfh.lumia.listeners";

        try {
            Reflections reflections = new Reflections(packageName);
            Set<Class<? extends Listener>> classes = reflections.getSubTypesOf(Listener.class);

            for (Class<? extends Listener> clazz : classes) {
                Constructor<?>[] constructors = clazz.getConstructors();

                Listener listener = null;
                for (Constructor<?> constructor : constructors) {
                    Class<?>[] params = constructor.getParameterTypes();
                    if (params.length == 1 && params[0] == SkillManager.class) {
                        listener = (Listener) constructor.newInstance(skillManager);
                        break;
                    }
                }

                if (listener == null) {
                    listener = clazz.getDeclaredConstructor().newInstance();
                }

                Bukkit.getPluginManager().registerEvents(listener, plugin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
