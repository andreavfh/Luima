package io.github.andreavfh.lumia.commands.sub;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.commands.SubCommand;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.skill.ISkill;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import io.github.andreavfh.lumia.utils.Convert;
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

    public static final String MENU_TITLE = ChatColor.GOLD + "Skills";
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
        if (!(sender instanceof Player player)) return;

        SkillManager skillManager = plugin.getSkillManager();
        SkillType[] skillTypes = SkillType.values();
        int inventorySize = ((skillTypes.length + 8) / 9) * 9;

        Inventory inventory = Bukkit.createInventory(null, inventorySize, MENU_TITLE);

        for (SkillType type : skillTypes) {
            ISkill skill = skillManager.getHolder(player).getSkill(type);
            if (skill == null) continue;

            ItemStack skillItem = createSkillItem(type, skill);
            inventory.addItem(skillItem);
        }

        player.openInventory(inventory);
    }

    private ItemStack createSkillItem(SkillType type, ISkill skill) {
        Material icon = getSkillIcon(type);
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        LanguageConfig lang = plugin.getLanguageConfig();

        String levelRoman = Convert.toRoman(skill.getLevel());
        String nextLevelRoman = Convert.toRoman(skill.getLevel() + 1);
        String displayName = ChatColor.GOLD + type.getMeta().getDisplayName() + " " + levelRoman;

        double currentXP = skill.getCurrentXP();
        double xpNeeded = skill.getXPForNextLevel();
        int percent = (int) Math.min(100.0, (currentXP / xpNeeded) * 100.0);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + type.getMeta().getDescription());
        lore.add("");
        lore.add(ChatColor.YELLOW + skill.getRank() + " " + levelRoman);
        lore.add("");
        String progress = lang.getRaw("progress_to_level")
                .replace("{level}", ChatColor.YELLOW + nextLevelRoman + ChatColor.GRAY)
                + ": "
                + ChatColor.AQUA + String.format("%d%%", percent)
                + ChatColor.GRAY + " (" + ChatColor.AQUA + String.format("%d", (int) currentXP)
                + ChatColor.DARK_GRAY + "/"
                + ChatColor.AQUA + (int) xpNeeded + ChatColor.GRAY + ")";

        lore.add(progress);
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + lang.getRaw("level_perks"));
        if (skill.getTier() <= 0) {
            lore.add(ChatColor.GRAY + " - " + ChatColor.RED + lang.getRaw("no_perks"));
            lore.add("");
        } else {
            lore.add(ChatColor.GRAY + " - " + type.getMeta().getPerks().getPerk(skill.getTier()).getName());
            lore.add("");
            lore.add(ChatColor.GRAY + type.getMeta().getPerks().getPerk(skill.getTier()).getDescription());
        }
        // lore.add(ChatColor.DARK_PURPLE + "Tip: " + ChatColor.LIGHT_PURPLE + SkillTips.getTip(type));

        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
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

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
