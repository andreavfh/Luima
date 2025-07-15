package io.github.andreavfh.lumia.integrations;

import io.github.andreavfh.lumia.Lumia;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class LuckPermsHook {

    private final Lumia plugin;
    private LuckPerms luckPerms;

    public LuckPermsHook(Lumia plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        try {
            this.luckPerms = LuckPermsProvider.get();
            plugin.getLumiaLogger().info("LuckPerms detected and hooked.");
        } catch (IllegalStateException e) {
            this.luckPerms = null;
            plugin.getLumiaLogger().warning("LuckPerms not found. Permission-based features will be disabled.");
        }
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public boolean isHooked() {
        return luckPerms != null;
    }
}
