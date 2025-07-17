package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.database.SQLStorage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillManager {

    private final SQLStorage storage;
    private final Map<UUID, PlayerSkillHolder> holders = new HashMap<>();

    public SkillManager(SQLStorage storage) {
        this.storage = storage;
    }

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

    public void save(UUID uuid) {
        PlayerSkillHolder holder = holders.get(uuid);
        if (holder != null) {
            storage.savePlayerSkills(uuid, holder);
        }
    }

    public PlayerSkillHolder getHolder(Player player) {
        return holders.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerSkillHolder(player));
    }

    public void remove(UUID uuid) {
        holders.remove(uuid);
    }
}
