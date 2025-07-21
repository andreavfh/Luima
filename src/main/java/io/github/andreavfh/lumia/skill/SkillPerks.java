package io.github.andreavfh.lumia.skill;

import java.util.HashMap;
import java.util.Map;

public class SkillPerks {

    private final Map<Integer, SkillPerk> tierPerks = new HashMap<>();

    public void setPerk(int tier, SkillPerk perk) {
        tierPerks.put(tier, perk);
    }

    public SkillPerk getPerk(int tier) {
        return tierPerks.get(tier);
    }

    public SkillPerk[] getAllPerks() {
        SkillPerk[] result = new SkillPerk[5];
        for (int i = 1; i <= 5; i++) {
            result[i - 1] = tierPerks.get(i);
        }
        return result;
    }
}
