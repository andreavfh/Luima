package io.github.andreavfh.lumia.listeners.mining;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.skill.*;
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

import java.util.*;

public class MiningPerksListener implements Listener {

    private final SkillManager skillManager;
    private final Lumia plugin;

    public MiningPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
        this.plugin = JavaPlugin.getPlugin(Lumia.class);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        int tier = skillManager.getHolder(player).getSkill(SkillType.MINING).getTier();
        if (tier <= 0) return;

        SkillMeta meta = SkillType.MINING.getMeta();
        SkillPerk perk = meta.getPerks().getPerk(tier);

        if (perk != null) {
            perk.apply(player, event);
        }
    }

    public static List<Block> getNearbySameTypeBlocks(Block origin, int limit) {
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

    public static void registerMiningPerks(SkillMeta meta) {
        SkillPerks perks = meta.getPerks();

        perks.setPerk(1, new SkillPerk(
                "Rapidez minera I",
                "Otorga Haste I durante 5 segundos al minar.",
                (player, context) -> {
                    if (context instanceof BlockBreakEvent) {
                        player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HASTE, 100, 0, true, false));
                    }
                }
        ));

        perks.setPerk(2, new SkillPerk(
                "Doble drop",
                "20% de probabilidad de obtener el doble de drop al minar.",
                (player, context) -> {
                    if (context instanceof BlockBreakEvent event) {
                        if (Math.random() < 0.20) {
                            Block block = event.getBlock();
                            ItemStack tool = player.getInventory().getItemInMainHand();
                            for (ItemStack drop : block.getDrops(tool)) {
                                block.getWorld().dropItemNaturally(block.getLocation(), drop.clone());
                            }
                        }
                    }
                }
        ));

        perks.setPerk(3, new SkillPerk(
                "Visión nocturna",
                "Obtienes visión nocturna por 30 segundos al minar.",
                (player, context) -> {
                    if (context instanceof BlockBreakEvent) {
                        if (!player.hasPotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION)) {
                            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION, 600, 0, true, false));
                        }
                    }
                }
        ));

        perks.setPerk(4, new SkillPerk(
                "Detector de minerales",
                "Detecta minerales cercanos en un radio de 5 bloques.",
                (player, context) -> {
                    if (!(context instanceof BlockBreakEvent)) return;
                    Block center = player.getLocation().getBlock();
                    int radius = 5;
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block b = center.getRelative(x, y, z);
                                if (new MiningValidator().isOre(b.getType())) {
                                    Lumia plugin = JavaPlugin.getPlugin(Lumia.class);
                                    var lang = plugin.getLanguageConfig();
                                    var formatter = plugin.getMessageFormatter();
                                    String message = lang.getRaw("nearby_mineral")
                                            .replace("{mineral}", b.getType().name());
                                    formatter.sendMessage(player, message);
                                    return;
                                }
                            }
                        }
                    }
                }
        ));

        perks.setPerk(5, new SkillPerk(
                "Minería en área",
                "Rompe hasta 10 bloques conectados del mismo tipo al minar.",
                (player, context) -> {
                    if (context instanceof BlockBreakEvent event) {
                        Block origin = event.getBlock();
                        List<Block> connected = MiningPerksListener.getNearbySameTypeBlocks(origin, 10);
                        for (Block b : connected) {
                            if (!b.equals(origin)) {
                                b.breakNaturally(player.getInventory().getItemInMainHand());
                            }
                        }
                    }
                }
        ));
    }

}
