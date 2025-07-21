package io.github.andreavfh.lumia.utils;

import io.github.andreavfh.lumia.config.Config;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.skill.SkillType;
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

    private final Map<Player, Map<SkillType, BossBar>> bossBars = new HashMap<>();
    private final Map<Player, Map<SkillType, Integer>> removalTasks = new HashMap<>();

    public SkillBossBarManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void showXP(Player player, SkillType skillType, int currentXP, int requiredXP, int level) {
        if (currentXP <= 0 || requiredXP <= 0) return;

        Map<SkillType, BossBar> playerBars = bossBars.computeIfAbsent(player, p -> new HashMap<>());
        BossBar bar = playerBars.computeIfAbsent(skillType, k ->
                Bukkit.createBossBar(" ", BarColor.BLUE, BarStyle.SEGMENTED_10)
        );

        double progress = Math.min((double) currentXP / requiredXP, 1.0);
        String skillName = skillType.getMeta().getDisplayName();
        String levelInRoman = Convert.toRoman(level);

        bar.setProgress(progress);
        bar.setTitle(ChatColor.GRAY + skillName + " " + ChatColor.DARK_GRAY + levelInRoman);

        if (!bar.getPlayers().contains(player)) {
            bar.addPlayer(player);
        }

        Map<SkillType, Integer> playerRemovals = removalTasks.computeIfAbsent(player, p -> new HashMap<>());
        if (playerRemovals.containsKey(skillType)) {
            Bukkit.getScheduler().cancelTask(playerRemovals.get(skillType));
        }

        int taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            bar.removePlayer(player);
            playerBars.remove(skillType);
            playerRemovals.remove(skillType);
        }, 60L).getTaskId();

        playerRemovals.put(skillType, taskId);
    }

}
