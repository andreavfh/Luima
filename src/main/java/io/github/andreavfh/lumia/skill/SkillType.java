package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.skill.skills.*;
import org.bukkit.entity.Player;

public enum SkillType {

    FARMING {
        @Override
        public ISkill createInstance(Player player) {
            return new FarmingSkill(player);
        }

        @Override
        public String getKey() {
            return "farming";
        }
    },

    COMBAT {
        @Override
        public ISkill createInstance(Player player) {
            return new CombatSkill(player);
        }

        @Override
        public String getKey() {
            return "combat";
        }
    },

    MINING {
        @Override
        public ISkill createInstance(Player player) {
            return new MiningSkill(player);
        }

        @Override
        public String getKey() {
            return "mining";
        }
    },

    FORAGING {
        @Override
        public ISkill createInstance(Player player) {
            return new ForagingSkill(player);
        }

        @Override
        public String getKey() {
            return "foraging";
        }
    },

    FISHING {
        @Override
        public ISkill createInstance(Player player) {
            return new FishingSkill(player);
        }

        @Override
        public String getKey() {
            return "fishing";
        }
    },

    ENCHANTING {
        @Override
        public ISkill createInstance(Player player) {
            return new EnchantingSkill(player);
        }

        @Override
        public String getKey() {
            return "enchanting";
        }
    },

    ALCHEMY {
        @Override
        public ISkill createInstance(Player player) {
            return new AlchemySkill(player);
        }

        @Override
        public String getKey() {
            return "alchemy";
        }
    },

    ARCHERY {
        @Override
        public ISkill createInstance(Player player) {
            return new ArcherySkill(player);
        }

        @Override
        public String getKey() {
            return "archery";
        }
    },

    AGILITY {
        @Override
        public ISkill createInstance(Player player) {
            return new AgilitySkill(player);
        }

        @Override
        public String getKey() {
            return "agility";
        }
    },

    SMITHING {
        @Override
        public ISkill createInstance(Player player) {
            return new SmithingSkill(player);
        }

        @Override
        public String getKey() {
            return "smithing";
        }
    };

    private final SkillMeta meta = new SkillMeta(this); // 🧠 Instancia única y compartida

    public static SkillType fromKey(String lowerCase) {
        for (SkillType type : values()) {
            if (type.getKey().equalsIgnoreCase(lowerCase)) {
                return type;
            }
        }
        return null;
    }

    public abstract ISkill createInstance(Player player);

    public abstract String getKey();

    public SkillMeta getMeta() {
        return meta;
    }
}
