package io.github.andreavfh.lumia.commands.sub;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.commands.SubCommand;
import io.github.andreavfh.lumia.skill.ISkill;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Skills implements SubCommand {

    private final Lumia plugin;

    public Skills(Lumia plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "skills";
    }

    @Override
    public String getDescription() {
        return "Opens a compact menu showing all your skills.";
    }

    @Override
    public String getPermission() {
        return "lumia.skills";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            return;
        }



        SkillType[] types = SkillType.values();
        SkillManager skillManager = plugin.getSkillManager();
        int size = ((types.length + 8) / 9) * 9;
        Inventory inv = Bukkit.createInventory(null, size, ChatColor.DARK_GREEN + "Your Skills");

        for (SkillType type : types) {
            ISkill skill = skillManager.getHolder(((Player) sender).getPlayer()).getSkill(type);
            if (skill == null) continue;

            ItemStack item = new ItemStack(getSkillIcon(type));
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.GOLD + capitalize(type.name()));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Level: " + ChatColor.GREEN + skill.getLevel());
            lore.add(ChatColor.GRAY + "XP: " + ChatColor.AQUA + String.format("%.2f", skill.getCurrentXP()) + " / " + skill.getXPForNextLevel());
            lore.add(ChatColor.GRAY + "Rank: " + ChatColor.YELLOW + skill.getRank());
            lore.add("");
            // Todo: lore.add(ChatColor.DARK_PURPLE + "Tip: " + ChatColor.LIGHT_PURPLE + SkillTips.getTip(type));

            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.addItem(item);
        }

        player.openInventory(inv);
    }

    private Material getSkillIcon(SkillType type) {
        return switch (type) {
            case FARMING -> Material.WHEAT;
            case COMBAT -> Material.DIAMOND_SWORD;
            case MINING -> Material.IRON_PICKAXE;
            case FORAGING -> Material.OAK_LOG;
            case FISHING -> Material.FISHING_ROD;
            case ENCHANTING -> Material.ENCHANTING_TABLE;
            case ALCHEMY -> Material.BREWING_STAND;
            case ARCHERY -> Material.BOW;
            case AGILITY -> Material.FEATHER;
            case SMITHING -> Material.SMITHING_TABLE;
        };
    }

    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
