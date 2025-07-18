package io.github.andreavfh.lumia.utils;

import io.github.andreavfh.lumia.config.Config;
import io.github.andreavfh.lumia.config.LanguageConfig;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SkillBossBarManager {

    private final JavaPlugin plugin;
    private final Map<Player, BossBar> bossBars = new HashMap<>();

    public SkillBossBarManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void showXP(Player player, String s, int currentXP, int requiredXP, int level) {
        BossBar bar = bossBars.computeIfAbsent(player, p ->
                Bukkit.createBossBar(" ", BarColor.GREEN, BarStyle.SEGMENTED_10)
        );
        Config config = new Config(plugin);
        LanguageConfig languageConfig = new LanguageConfig(plugin, config);

        String skillName = languageConfig.getRaw("skill_" + s.toLowerCase() + "_name");

        double progress = Math.min((double) currentXP / requiredXP, 1.0);
        bar.setProgress(progress);

        String levelInRoman = Convert.toRoman(level);

        bar.setTitle(ChatColor.GRAY + skillName + " " + ChatColor.DARK_GRAY + levelInRoman);
        bar.setColor(BarColor.BLUE);

        if (!bar.getPlayers().contains(player)) {
            bar.addPlayer(player);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            bar.removePlayer(player);
        }, 60L); //
    }

    public void clear(Player player) {
        BossBar bar = bossBars.remove(player);
        if (bar != null) {
            bar.removeAll();
        }
    }
}