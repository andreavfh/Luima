package io.github.andreavfh.lumia.listeners;

import io.github.andreavfh.lumia.commands.sub.Skills;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Set;

public class InventoryClick implements Listener {

        private static final Set<String> CUSTOM_MENU_TITLES = Set.of(
                Skills.MENU_TITLE
        );

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            String title = event.getView().getTitle();
            if (CUSTOM_MENU_TITLES.contains(title)) {
                event.setCancelled(true);
            }
        }
    }

