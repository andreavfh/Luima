package io.github.andreavfh.lumia.listeners.smithing;

import io.github.andreavfh.lumia.skill.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
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
        SkillMeta meta = SkillType.SMITHING.getMeta();
        SkillPerk perk = meta.getPerks().getPerk(tier);

        if (perk != null) {
            perk.apply(player, event);
        }
    }

    @EventHandler
    public void onItemDurabilityLoss(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        int tier = skillManager.getHolder(player).getSkill(SkillType.SMITHING).getTier();
        SkillMeta meta = SkillType.SMITHING.getMeta();
        SkillPerk perk = meta.getPerks().getPerk(tier);

        if (perk != null) {
            perk.apply(player, event);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;

        int tier = skillManager.getHolder(player).getSkill(SkillType.SMITHING).getTier();
        SkillMeta meta = SkillType.SMITHING.getMeta();
        SkillPerk perk = meta.getPerks().getPerk(tier);

        if (perk != null) {
            perk.apply(player, event);
        }
    }

    @EventHandler
    public void onAnvilBreak(BlockDamageEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.ANVIL && block.getType() != Material.CHIPPED_ANVIL && block.getType() != Material.DAMAGED_ANVIL) return;

        Player player = event.getPlayer();
        int tier = skillManager.getHolder(player).getSkill(SkillType.SMITHING).getTier();
        SkillMeta meta = SkillType.SMITHING.getMeta();
        SkillPerk perk = meta.getPerks().getPerk(tier);

        if (perk != null) {
            perk.apply(player, event);
        }
    }

    public static void registerSmithingPerks(SkillMeta meta) {
        SkillPerks perks = meta.getPerks();

        perks.setPerk(1, new SkillPerk("Reducción de coste de afilado", "Reduce el coste de reparación en un 10%.",
                (player, context) -> {
                    if (context instanceof PrepareAnvilEvent event) {
                        var view = event.getView();
                        if (view.getRepairCost() > 0) {
                            int newCost = (int) Math.floor(view.getRepairCost() * 0.9);
                            view.setRepairCost(Math.max(newCost, 1));
                        }
                    }
                }));

        perks.setPerk(2, new SkillPerk("Durabilidad mejorada", "10% de probabilidad de no perder durabilidad.",
                (player, context) -> {
                    if (context instanceof PlayerItemDamageEvent event) {
                        if (Math.random() < 0.10) {
                            event.setCancelled(true);
                        }
                    }
                }));

        perks.setPerk(4, new SkillPerk("Daño extra con armas", "Incrementa un 5% el daño con espada y hacha.",
                (player, context) -> {
                    if (context instanceof EntityDamageByEntityEvent event) {
                        ItemStack weapon = player.getInventory().getItemInMainHand();
                        if (weapon.getType().toString().endsWith("_SWORD") || weapon.getType().toString().endsWith("_AXE")) {
                            event.setDamage(event.getDamage() * 1.05);
                        }
                    }
                }));

        perks.setPerk(5, new SkillPerk("Anvils indestructibles", "Los yunques no se rompen al usarlos.",
                (player, context) -> {
                    if (context instanceof BlockDamageEvent event) {
                        Block block = event.getBlock();
                        if (block.getType() == Material.ANVIL
                                || block.getType() == Material.CHIPPED_ANVIL
                                || block.getType() == Material.DAMAGED_ANVIL) {
                            event.setCancelled(true);
                        }
                    }
                }));
    }
}
