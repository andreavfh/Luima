package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.skill.skills.*;
import org.bukkit.entity.Player;

public enum SkillType {
    FARMING {
        @Override
        public ISkill createInstance(Player player) {
            return new FarmingSkill(player);
        }
    },
    COMBAT {
        @Override
        public ISkill createInstance(Player player) {
            return new CombatSkill(player);
        }
    },
    MINING {
        @Override
        public ISkill createInstance(Player player) {
            return new MiningSkill(player);
        }
    },
    FORAGING {
        @Override
        public ISkill createInstance(Player player) {
            return new ForagingSkill(player);
        }
    },
    FISHING {
        @Override
        public ISkill createInstance(Player player) {
            return new FishingSkill(player);
        }
    },
    ENCHANTING {
        @Override
        public ISkill createInstance(Player player) {
            return new EnchantingSkill(player);
        }
    },
    ALCHEMY {
        @Override
        public ISkill createInstance(Player player) {
            return new AlchemySkill(player);
        }
    },
    ARCHERY {
        @Override
        public ISkill createInstance(Player player) {
            return new ArcherySkill(player);
        }
    },
    AGILITY {
        @Override
        public ISkill createInstance(Player player) {
            return new AgilitySkill(player);
        }
    },
    SMITHING {
        @Override
        public ISkill createInstance(Player player) {
            return new SmithingSkill(player);
        }
    };

    public abstract ISkill createInstance(Player player);
}
