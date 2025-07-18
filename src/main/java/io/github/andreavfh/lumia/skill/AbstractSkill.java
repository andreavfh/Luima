package io.github.andreavfh.lumia.skill;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.utils.Convert;
import io.github.andreavfh.lumia.utils.SkillXPUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractSkill implements ISkill {

    protected final SkillType type;
    protected final Player player;
    protected int level;
    protected double xp;

    public AbstractSkill(SkillType type, Player player) {
        this.player = player;
        this.type = type;
        this.level = 1;
        this.xp = 0;
    }

    @Override
    public SkillType getType() {
        return type;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public double getCurrentXP() {
        return xp;
    }

    @Override
    public double getXPForNextLevel() {
        return SkillXPUtil.getXPRequiredForLevel(level);
    }

    @Override
    public void addXP(double amount) {
        this.xp += amount;
        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        plugin.getBossBarManager().showXP(player, type.name(), (int) xp, (int) getXPForNextLevel(), level);

        while (canLevelUp()) {
            levelUp();
        }
    }

    @Override
    public boolean canLevelUp() {
        return xp >= getXPForNextLevel();
    }

    @Override
    public void levelUp() {
        this.xp -= getXPForNextLevel();
        this.level++;
        onLevelUp();
    }

    @Override
    public double getXPRequiredForLevel(int level) {
        return SkillXPUtil.getXPRequiredForLevel(level);
    }

    @Override
    public double getProgressPercent() {
        return Math.min(1.0, xp / getXPForNextLevel());
    }

    @Override
    public int getTier() {
        return Math.min(level / 10, 5); // 0 a 5
    }

    @Override
    public String getRank() {
        String key = "skills_" + type.name().toLowerCase() + "_" + getTier();
        LanguageConfig lang = JavaPlugin.getPlugin(Lumia.class).getLanguageConfig();
        return lang.getRaw(key);
    }

    protected void onLevelUp() {
        Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
        LanguageConfig l = plugin.getLanguageConfig();
        String formerLevelRoman = Convert.toRoman(level - 1);
        String formerSkillName = l.getRaw("skill_" + type.name().toLowerCase() + "_name") + " " + formerLevelRoman;
        String currentLevelRoman = Convert.toRoman(level);
        String currentSkillName = l.getRaw("skill_" + type.name().toLowerCase() + "_name") + " " + currentLevelRoman;
        String message = plugin.getLanguageConfig()
                .getRaw("skills_level_up")
                .replace("{formerSkill}", formerSkillName)
                .replace("{currentSkill}", currentSkillName)
                .replace("{rank}", getRank());
        plugin.getMessageFormatter().sendMessage(player, message);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
    }
}
