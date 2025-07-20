package io.github.andreavfh.lumia.listeners.smithing;

import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class SmithingPerksListener implements Listener {

    private final SkillManager skillManager;

    public SmithingPerksListener(SkillManager skillManager) {
        this.skillManager = skillManager;
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        if (!(event.getView().getPlayer() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.SMITHING).getTier();


        var anvilView = event.getView();

        if (tier >= 1 && anvilView.getRepairCost() > 0) {
            int newCost = (int) Math.floor(anvilView.getRepairCost() * 0.9);
            anvilView.setRepairCost(Math.max(newCost, 1));
        }

        if (tier >= 5) {
            anvilView.setRepairCost(0);
        }
    }

    @EventHandler
    public void onItemDurabilityLoss(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        int tier = skillManager.getHolder(player).getSkill(SkillType.SMITHING).getTier();

        if (tier >= 2 && Math.random() < 0.10) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        int tier = skillManager.getHolder(player).getSkill(SkillType.SMITHING).getTier();

        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (tier >= 4 && (weapon.getType().toString().endsWith("_SWORD") || weapon.getType().toString().endsWith("_AXE"))) {
            event.setDamage(event.getDamage() * 1.05);
        }
    }

    @EventHandler
    public void onAnvilBreak(BlockDamageEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.ANVIL && block.getType() != Material.CHIPPED_ANVIL && block.getType() != Material.DAMAGED_ANVIL) return;

        Player player = event.getPlayer();
        int tier = skillManager.getHolder(player).getSkill(SkillType.SMITHING).getTier();

        if (tier >= 5) {
            event.setCancelled(true);
        }
    }
}
