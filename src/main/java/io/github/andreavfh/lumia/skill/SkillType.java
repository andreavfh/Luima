package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.skill.skills.*;
import org.bukkit.entity.Player;

/**
 * Enum representing different skill types in the game.
 * Each skill type can create an instance of its corresponding skill and has an associated config key.
 */
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

    /**
     * Abstract method to create an instance of the skill associated with the skill type.
     *
     * @param player The player for whom the skill instance is created.
     * @return A new instance of the skill.
     */
    public abstract ISkill createInstance(Player player);

    /**
     * Gets the configuration key associated with this skill type.
     *
     * @return A lowercase string key.
     */
    public abstract String getKey();
}
