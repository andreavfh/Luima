package io.github.andreavfh.lumia.listeners.mining;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import io.github.andreavfh.lumia.utils.MessageFormatter;
import io.github.andreavfh.lumia.utils.MiningValidator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;


public class MiningPerksListener implements Listener {

    private final SkillManager skillManager;

    public MiningPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        int level = skillManager.getHolder(player).getSkill(SkillType.MINING).getLevel();
        int tier = Math.min(level / 10, 5);

        switch (tier) {
            case 0 -> applyMiningSpeedBoost(player);
            case 1 -> applyDoubleDropChance(event);
            case 2 -> giveNightVision(player);
            case 3 -> detectNearbyOres(player);
            case 4 -> applyTier4MiningBonus(player);
            case 5 -> mineArea(block, player);
        }
    }

    private void applyMiningSpeedBoost(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 100, 0, true, false));
    }

    private void applyDoubleDropChance(BlockBreakEvent event) {
        if (Math.random() < 0.20) {
            for (ItemStack drop : event.getBlock().getDrops(event.getPlayer().getInventory().getItemInMainHand())) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop.clone());
            }
        }
    }

    private void giveNightVision(Player player) {
        if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 30, 0, true, false));
        }
    }

    private void detectNearbyOres(Player player) {
        Block center = player.getLocation().getBlock();
        MiningValidator validator = new MiningValidator();
        int radius = 5;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block b = center.getRelative(x, y, z);
                    if (validator.isOre(b.getType())) {
                        Lumia plugin = JavaPlugin.getPlugin(io.github.andreavfh.lumia.Lumia.class);
                        LanguageConfig lang = plugin.getLanguageConfig();
                        MessageFormatter formatter = plugin.getMessageFormatter();
                        String message = lang.getRaw("nearby_mineral")
                                .replace("{mineral}", b.getType().name());

                        formatter.sendMessage(player, message);
                        return;
                    }
                }
            }
        }
    }

    private void applyTier4MiningBonus(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 200, 3, true, false));
    }

    private void mineArea(Block origin, Player player) {
        List<Block> nearbyBlocks = getNearbySameTypeBlocks(origin, 10);
        for (Block block : nearbyBlocks) {
            if (block.getType() == origin.getType()) {
                block.breakNaturally(player.getInventory().getItemInMainHand());
            }
        }
    }

    private List<Block> getNearbySameTypeBlocks(Block origin, int limit) {
        Set<Block> result = new HashSet<>();
        Queue<Block> queue = new LinkedList<>();
        queue.add(origin);

        while (!queue.isEmpty() && result.size() < limit) {
            Block current = queue.poll();
            if (result.contains(current)) continue;
            result.add(current);

            for (BlockFace face : BlockFace.values()) {
                Block neighbor = current.getRelative(face);
                if (neighbor.getType() == origin.getType()) {
                    queue.add(neighbor);
                }
            }
        }
        return new ArrayList<>(result);
    }



}
