package io.github.andreavfh.lumia.skill;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

public class SkillPerk {

    private final String name;
    private final String description;
    private final BiConsumer<Player, Object> action;

    public SkillPerk(String name, String description, BiConsumer<Player, Object> action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }

    /**
     * Aplica el perk al jugador, sin contexto extra.
     */
    public void apply(Player player) {
        apply(player, null);
    }

    /**
     * Aplica el perk con un contexto adicional, por ejemplo un evento.
     */
    public void apply(Player player, Object context) {
        action.accept(player, context);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
