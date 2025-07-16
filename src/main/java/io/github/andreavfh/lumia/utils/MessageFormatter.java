package io.github.andreavfh.lumia.utils;

import io.github.andreavfh.lumia.Lumia;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class MessageFormatter {

    private final Lumia plugin;

    public MessageFormatter(Lumia plugin) {
        this.plugin = plugin;
    }

    /** Map de placeholders y sus valores */
    public Map<String, String> getReplacements() {
        Map<String, String> map = new HashMap<>();
        map.put("name", plugin.getDescription().getName());
        map.put("version", plugin.getDescription().getVersion());
        map.put("author", String.join(", ", plugin.getDescription().getAuthors()));
        map.put("description", plugin.getDescription().getDescription());
        map.put("website", plugin.getDescription().getWebsite() != null ? plugin.getDescription().getWebsite() : "N/A");
        return map;
    }

    /** Traduce códigos & a § para colores */
    public String translateColors(String input) {
        return input == null ? "" : input.replace("&", "§");
    }

    /**
     * Convierte el mensaje raw con placeholders a un Component con Adventure,
     * aplicando click events en links como {website}.
     */
    public Component formatMessageComponent(String rawMessage) {
        if (rawMessage == null || rawMessage.isEmpty()) return Component.empty();

        Map<String, String> replacements = getReplacements();

        Component result = Component.empty();
        int index = 0;
        while (index < rawMessage.length()) {
            int start = rawMessage.indexOf('{', index);
            if (start == -1) {
                result = result.append(Component.text(translateColors(rawMessage.substring(index))));
                break;
            }

            int end = rawMessage.indexOf('}', start);
            if (end == -1) {
                result = result.append(Component.text(translateColors(rawMessage.substring(index))));
                break;
            }

            if (start > index) {
                result = result.append(Component.text(translateColors(rawMessage.substring(index, start))));
            }

            String placeholder = rawMessage.substring(start + 1, end);
            String value = replacements.getOrDefault(placeholder, "{" + placeholder + "}");

            if (placeholder.equalsIgnoreCase("website") && !value.equalsIgnoreCase("N/A")) {
                result = result.append(Component.text(value)
                        .color(NamedTextColor.AQUA)
                        .clickEvent(ClickEvent.openUrl(value))
                        .hoverEvent(HoverEvent.showText(Component.text("GitHub")))
                        .decorate(TextDecoration.UNDERLINED));
            } else {
                result = result.append(Component.text(translateColors(value)));
            }

            index = end + 1;
        }

        return result;
    }

    /**
     * Envía el mensaje formateado al CommandSender usando Adventure.
     * @param sender El destinatario
     * @param rawMessage El mensaje raw con placeholders
     */
    public void sendMessage(CommandSender sender, String rawMessage) {
        if (rawMessage == null || rawMessage.isEmpty()) return;
        plugin.adventure().sender(sender).sendMessage(formatMessageComponent(rawMessage));
    }
}
