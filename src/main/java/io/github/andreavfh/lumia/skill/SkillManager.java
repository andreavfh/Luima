package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.database.SQLStorage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillManager {

    private final SQLStorage storage;
    private final Map<UUID, PlayerSkillHolder> holders = new HashMap<>();

    /**
     * Constructs a new SkillManager with the specified storage system.
     *
     * @param storage The SQLStorage instance used for saving and loading skill data.
     */
    public SkillManager(SQLStorage storage) {
        this.storage = storage;
    }

    /**
     * Loads the player's skill data from the database and initializes their skill holder.
     *
     * @param player The player whose skills are being loaded.
     */
    public void load(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSkillHolder holder = new PlayerSkillHolder(player);

        Map<SkillType, Integer> loadedLevels = storage.loadPlayerSkillLevels(uuid);
        Map<SkillType, Double> loadedXP = storage.loadPlayerSkillXP(uuid);

        for (SkillType type : SkillType.values()) {
            ISkill skill = holder.getSkill(type);
            skill.addXP(loadedXP.getOrDefault(type, 0.0));
            while (skill.getLevel() < loadedLevels.getOrDefault(type, 1)) {
                skill.levelUp();
            }
        }

        holders.put(uuid, holder);
    }

    /**
     * Saves the player's skill data to the database.
     *
     * @param uuid The UUID of the player whose skills are being saved.
     */
    public void save(UUID uuid) {
        PlayerSkillHolder holder = holders.get(uuid);
        if (holder != null) {
            storage.savePlayerSkills(uuid, holder);
        }
    }

    /**
     * Retrieves the PlayerSkillHolder for the specified player.
     * If the player does not have a skill holder, a new one is created.
     *
     * @param player The player whose skill holder is being retrieved.
     * @return The PlayerSkillHolder associated with the player.
     */
    public PlayerSkillHolder getHolder(Player player) {
        return holders.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerSkillHolder(player));
    }

    /**
     * Removes the player's skill holder from memory.
     *
     * @param uuid The UUID of the player whose skill holder is being removed.
     */
    public void remove(UUID uuid) {
        holders.remove(uuid);
    }
}
